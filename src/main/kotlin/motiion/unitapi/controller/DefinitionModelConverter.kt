package motiion.unitapi.controller

import motiion.unitapi.model.*

object DefinitionModelConverter {

    // UnitDefinitionDTO --> UnitDefinition
    fun convertToEntity(dto: UnitDefinitionDTO): UnitDefinition {
        val unitDefinition = UnitDefinition(dto.name, dto.attributeDefinitions?.map { convertToEntity(it) }?.toMutableList())
        unitDefinition.id = dto.id
        unitDefinition.publishDate = dto.publishDate
        unitDefinition.unitAttributeDefinitions?.forEach { it.unitDefinition = unitDefinition }
        return unitDefinition
    }

    // UnitDefinition --> UnitDefinitionDTO
    fun convertToDTO(t: UnitDefinition): UnitDefinitionDTO {
        return UnitDefinitionDTO(t.id, t.name, t.unitAttributeDefinitions?.map {
            convertToDTO(it)
        }, t.publishDate)
    }

    // UnitAttributeDefinition --> UnitAttributeDefinitionDTO
    fun convertToDTO(t: UnitAttributeDefinition): UnitAttributeDefinitionDTO {
        return UnitAttributeDefinitionDTO(t.id, t.name, t.valueType, t.constraintDefinitions?.map {
            when (it) {
                is RangedNumberUnitAttributeConstraintDefinition -> convertToDTO(it)
                is TextLengthUnitAttributeConstraintDefinition -> convertToDTO(it)
                is OptionsUnitAttributeConstraintDefinition -> convertToDTO(it)
                else -> throw IllegalStateException("Unknown constraint type ${it::class}")
            }
        })
    }

    // UnitAttributeDefinitionDTO --> UnitAttributeDefinition
    fun convertToEntity(dto: UnitAttributeDefinitionDTO): UnitAttributeDefinition {
        val constraintDefs = dto.constraintDefinitions?.map {
            when (it) {
                is RangedAttributeConstraintDefinitionDTO -> convertToEntity(it)
                is TextLengthAttributeConstraintDefinitionDTO -> convertToEntity(it)
                is OptionsUnitAttributeConstraintDefinitionDTO -> convertToEntity(it)
                else -> throw IllegalStateException("Unknown constraint type ${it::class}")
            }
        }?.toMutableList()
        val unitAttributeDefinition = UnitAttributeDefinition(dto.name, dto.type, constraintDefs)
        unitAttributeDefinition.id = dto.id
        unitAttributeDefinition.constraintDefinitions?.forEach { it.unitAttributeDefinition = unitAttributeDefinition }
        return unitAttributeDefinition
    }

    // RangedAttributeConstraintDefinitionDTO --> RangedNumberUnitAttributeConstraintDefinition
    fun convertToEntity(dto: RangedAttributeConstraintDefinitionDTO): RangedNumberUnitAttributeConstraintDefinition {
        val ranged = RangedNumberUnitAttributeConstraintDefinition(dto.lower, dto.upper)
        ranged.id = dto.id
        return ranged
    }
    // RangedNumberUnitAttributeConstraintDefinition --> RangedAttributeConstraintDefinitionDTO
    fun convertToDTO(t: RangedNumberUnitAttributeConstraintDefinition): RangedAttributeConstraintDefinitionDTO {
        return RangedAttributeConstraintDefinitionDTO(t.id, t.lower, t.upper)
    }

    // TextLengthAttributeConstraintDefinitionDTO -->  TextLengthUnitAttributeConstraintDefinition
    fun convertToEntity(dto: TextLengthAttributeConstraintDefinitionDTO): TextLengthUnitAttributeConstraintDefinition {
        val obj = dto
        val textLengthConstraint = TextLengthUnitAttributeConstraintDefinition(obj.maxLength)
        textLengthConstraint.id = obj.id
        return textLengthConstraint
    }

    // TextLengthUnitAttributeConstraintDefinition --> TextLengthAttributeConstraintDefinitionDTO
    fun convertToDTO(t: TextLengthUnitAttributeConstraintDefinition): TextLengthAttributeConstraintDefinitionDTO {
        return TextLengthAttributeConstraintDefinitionDTO(t.id, t.maxLength)
    }

    // OptionsUnitAttributeConstraintDefinitionDTO --> OptionsUnitAttributeConstraintDefinition
    fun convertToEntity(dto: OptionsUnitAttributeConstraintDefinitionDTO): OptionsUnitAttributeConstraintDefinition {
        val obj = dto
        val options = OptionsUnitAttributeConstraintDefinition(obj.options.map { convertToEntity(it) }.toSet())
        options.id = obj.id
        options.options.map { it.constraint = options }
        return options
    }

    // OptionsUnitAttributeConstraintDefinition --> OptionsUnitAttributeConstraintDefinitionDTO
    fun convertToDTO(t: OptionsUnitAttributeConstraintDefinition): OptionsUnitAttributeConstraintDefinitionDTO {
        return OptionsUnitAttributeConstraintDefinitionDTO(t.id, t.options.map { convertToDTO(it) })
    }

    //AttributeOptionDTO --> UnitAttributeOption
    fun convertToEntity(dto: AttributeOptionDTO): UnitAttributeOption {
        val unitAttributeOption = UnitAttributeOption(dto.value)
        unitAttributeOption.id = dto.id
        return unitAttributeOption
    }

    //UnitAttributeOption --> AttributeOptionDTO
    fun convertToDTO(t: UnitAttributeOption): AttributeOptionDTO {
        return AttributeOptionDTO(t.id, t.value)
    }
}