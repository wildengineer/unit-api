package motiion.unitapi.service

import motiion.unitapi.exception.ResourceNotFoundException
import motiion.unitapi.model.UnitAttributeDefinition
import motiion.unitapi.repository.UnitAttributeDefinitionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UnitAttributeDefinitionService {

    companion object {
        val NOT_FOUND_ERROR_MESSAGE_SUPPLIER = { id: Long? -> "Could not find UnitAttributeDefinition with id: ${id}" }
    }

    @Autowired
    lateinit var repository: UnitAttributeDefinitionRepository

    fun getUnitAttributeDefinitions(): List<UnitAttributeDefinition> {
        return repository.findAll().toList()
    }

    fun save(unitAttributeDefinition: UnitAttributeDefinition): UnitAttributeDefinition {
        return repository.save(unitAttributeDefinition)
    }

    fun delete(id: Long) {
        repository.deleteById(id)
    }

    fun update(id: Long, unitAttributeDefinition: UnitAttributeDefinition): UnitAttributeDefinition {
        val persisted = getUnitAttributeDefinition(id)
        persisted.name = unitAttributeDefinition.name
        //TODO: Check that units are bound to this
        persisted.constraintDefinitions?.clear()
        persisted.constraintDefinitions?.addAll(unitAttributeDefinition.constraintDefinitions ?: mutableListOf())
        return repository.save(persisted)
    }

    private fun getUnitAttributeDefinition(id: Long): UnitAttributeDefinition {
        return repository.findById(id).orElseThrow {
            throw ResourceNotFoundException(NOT_FOUND_ERROR_MESSAGE_SUPPLIER(id))
        }
    }
}