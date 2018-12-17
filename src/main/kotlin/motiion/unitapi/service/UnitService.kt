package motiion.unitapi.service

import motiion.unitapi.exception.InvalidUnitException
import motiion.unitapi.exception.ResourceNotFoundException
import motiion.unitapi.model.*
import motiion.unitapi.model.Unit
import motiion.unitapi.repository.UnitRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class UnitService(private val unitRepository: UnitRepository,
                  private val unitDefinitionService: UnitDefinitionService) {

    private val typeAttributeMapping: (attr: UnitAttribute) -> ValueTypeEnum = {
        when (it) {
            is TextUnitAttribute -> ValueTypeEnum.TEXT
            is NumericUnitAttribute -> ValueTypeEnum.NUMERIC
            is DateUnitAttribute -> ValueTypeEnum.DATETIME
            is UserUnitAttribute -> ValueTypeEnum.USER
            is ProductUnitAttribute -> ValueTypeEnum.PRODUCT
            is CreateDateUnitAttribute -> ValueTypeEnum.CREATE_TIME
            else -> throw IllegalStateException("Unknown attribute type")
        }
    }

    fun create(unit: Unit): Unit {
        val unitDefinition = Optional.of(
            unitDefinitionService.getPublishedUnitDefinition(unit.name)).orElseThrow {
            throw ResourceNotFoundException("No published unit definition for name ${unit.name}")
        }
        validateAgainstDefinition(unit, unitDefinition)
        return unitRepository.save(unit)
    }

    fun getUnits(): List<Unit> {
        return unitRepository.findAll().toList()
    }

    fun getUnitsById(id: Long): Unit {
        return unitRepository.findById(id).orElseThrow { throw ResourceNotFoundException("Unit with $id not found") }
    }

    private fun validateAgainstDefinition(unit: Unit, unitDefinition: UnitDefinition) {

        if (unitDefinition.unitAttributeDefinitions?.size != unit.unitAttributes.size) {
            throw InvalidUnitException("Attribute count is incorrect. There are" +
                " ${unitDefinition.unitAttributeDefinitions?.size} defined by name ${unitDefinition.name}")
        }
        val attributeDefinitions = unitDefinition.unitAttributeDefinitions
        val unitAttributes = unit.unitAttributes

        attributeDefinitions?.sortBy { it.name }
        unitAttributes.sortBy { it.name }

        val badAttributes = attributeDefinitions!!.zip(unitAttributes).filterNot {
            val def = it.first
            val attr = it.second
            def.name == attr.name && typeAttributeMapping(attr) == def.valueType
                && def.constraintDefinitions?.all { it2 ->
                when(it2) {
                    is TextLengthUnitAttributeConstraintDefinition -> {
                        it2.maxLength > (attr as TextUnitAttribute).value.length
                    }
                    is OptionsUnitAttributeConstraintDefinition -> {
                        it2.options.any { it3 -> (attr as TextUnitAttribute).value == it3.value }
                    }
                    is RangedNumberUnitAttributeConstraintDefinition -> {
                        val value = (attr as NumericUnitAttribute).value
                        it2.lower <= value && it2.upper >= value
                    }
                    else -> throw IllegalStateException("Unknown constraint type.")
                }
            }!!
        }.map{it.second.name}

        if (badAttributes.isNotEmpty()) {
            throw InvalidUnitException("${badAttributes.joinToString()} do not match the published unit definition ${unitDefinition.name}")
        }

        //validate with constraints
    }

    fun delete(id: Long) {
        unitRepository.deleteById(id)
    }
}

