package motiion.unitapi.service

import motiion.unitapi.exception.DuplicateEntityException
import motiion.unitapi.exception.ResourceNotFoundException
import motiion.unitapi.model.*
import motiion.unitapi.repository.UnitDefinitionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UnitDefinitionService {

    companion object {
        val NOT_FOUND_ERROR_MESSAGE_SUPPLIER = { id: Long? -> "Could not find UnitDefinition with id: ${id}" }
    }

    @Autowired
    lateinit var unitDefinitionRepository: UnitDefinitionRepository

    //TODO: Add paging
    fun getDefinitions(): List<UnitDefinition> {
        return unitDefinitionRepository.findAll().toList()
    }

    fun save(unitDefinition: UnitDefinition): UnitDefinition {
        validateName(unitDefinition.name)
        return unitDefinitionRepository.save(unitDefinition)
    }

    private fun validateName(name: String) {
        if (unitDefinitionRepository.findByName(name).isNotEmpty()) {
            throw DuplicateEntityException("Unit definition with name $name already exists")
        }
    }

    fun copy(id: Long): UnitDefinition {
        val unitDefinition = getUnitDefinition(id)
        return unitDefinitionRepository.save(copy(unitDefinition, "${unitDefinition.name}-${System.currentTimeMillis()}"))
    }

    fun delete(id: Long) {
        unitDefinitionRepository.deleteById(id)
    }

    private fun copyAttributes(attributes: List<UnitAttributeDefinition>): MutableList<UnitAttributeDefinition> {
        return attributes.map {
            val constraintDefCopies = copyConstraintDefinitions(it.constraintDefinitions ?: mutableListOf())
            val unitAttributeDefinition = UnitAttributeDefinition(it.name, it.valueType, constraintDefCopies)
            constraintDefCopies.forEach { it2 -> it2.unitAttributeDefinition = unitAttributeDefinition }
            unitAttributeDefinition
        }.toMutableList()
    }

    private fun copyConstraintDefinitions(constraintDefinitions: MutableList<UnitAttributeConstraintDefinition>):
        MutableList<UnitAttributeConstraintDefinition> {
        return constraintDefinitions.map {
            when (it) {
                is RangedNumberUnitAttributeConstraintDefinition -> {
                    RangedNumberUnitAttributeConstraintDefinition(it.upper, it.lower)
                }
                is TextLengthUnitAttributeConstraintDefinition -> {
                    TextLengthUnitAttributeConstraintDefinition(it.maxLength)
                }
                is OptionsUnitAttributeConstraintDefinition -> {
                    val optionsConstraintDefinition = OptionsUnitAttributeConstraintDefinition(copyOptions(it.options))
                    optionsConstraintDefinition.options.forEach { it2 -> it2.constraint = optionsConstraintDefinition }
                    optionsConstraintDefinition
                }
                else -> throw IllegalArgumentException("Unknown constraint type $it")
            }
        }.toMutableList()
    }

    private fun copyOptions(options: Set<UnitAttributeOption>): Set<UnitAttributeOption> {
        return options.map {
            UnitAttributeOption(it.value)
        }.toSet()
    }

    private fun copy(unitDefinition: UnitDefinition, uniqueName: String): UnitDefinition {
        val attributes = unitDefinition.unitAttributeDefinitions
        val attributeCopies = copyAttributes(attributes ?: mutableListOf())
        val copy = UnitDefinition(uniqueName, attributeCopies)
        attributeCopies.forEach { it.unitDefinition = copy }
        return copy
    }

    fun getUnitDefinition(id: Long): UnitDefinition {
        return unitDefinitionRepository.findById(id).orElseThrow {
            throw ResourceNotFoundException(NOT_FOUND_ERROR_MESSAGE_SUPPLIER(id))
        }
    }

    fun getCurrentPublished(): List<UnitDefinition> = unitDefinitionRepository.findCurrentPublished()

    fun getPublishedUnitDefinition(name: String): UnitDefinition =
        unitDefinitionRepository.findFirstByNameOrderByPublishDateDesc(name)

    fun update(id: Long, unitDefinition: UnitDefinition): UnitDefinition {
        val persisted = getUnitDefinition(id)
        persisted.name = unitDefinition.name
        persisted.unitAttributeDefinitions?.clear()
        //TODO: Only allow if no units have been created
        persisted.unitAttributeDefinitions?.addAll(unitDefinition.unitAttributeDefinitions ?: mutableListOf())
        return unitDefinitionRepository.save(persisted)
    }

    fun publish(id: Long) {
        val unitDef = getUnitDefinition(id)
        val unitDefinition = copy(unitDef, unitDef.name)
        unitDefinition.publishDate = Date()
        unitDefinitionRepository.save(unitDefinition)
    }
}