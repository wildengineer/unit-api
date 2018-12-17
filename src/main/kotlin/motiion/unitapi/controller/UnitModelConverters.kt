package motiion.unitapi.controller

import motiion.unitapi.model.*
import motiion.unitapi.model.Unit

object UnitModelConverter {

    // UnitDTO --> Unit
    fun convertToEntity(dto: UnitDTO): Unit {
        val unit = Unit(dto.name, null, dto.unitAttributes?.map {
            when (it) {
                is TextUnitAttributeDTO -> convertToEntity(it)
                is NumericUnitAttributeDTO -> convertToEntity(it)
                is UserUnitAttributeDTO -> convertToEntity(it)
                is DateUnitAttributeDTO -> convertToEntity(it)
                is ProductUnitAttributeDTO -> convertToEntity(it)
                is CreateDateUnitAttributeDTO -> convertToEntity(it)
                else -> throw IllegalArgumentException("Unknown attribute type.")
            }
        }!!.toMutableList())
        unit.unitAttributes.forEach { it.unit = unit }
        return unit
    }

    fun convertToEntity(dto: TextUnitAttributeDTO): TextUnitAttribute {
        val attribute = TextUnitAttribute(dto.name, dto.value)
        attribute.id = dto.id
        return attribute
    }

    fun convertToEntity(dto: NumericUnitAttributeDTO): NumericUnitAttribute {
        val attribute = NumericUnitAttribute(dto.name, dto.value)
        attribute.id = dto.id
        return attribute
    }

    fun convertToEntity(dto: DateUnitAttributeDTO): DateUnitAttribute {
        val attribute = DateUnitAttribute(dto.name, dto.value)
        attribute.id = dto.id
        return attribute
    }

    fun convertToEntity(dto: UserUnitAttributeDTO): UserUnitAttribute {
        val attribute = UserUnitAttribute(dto.name)
        attribute.id = dto.id
        return attribute
    }

    fun convertToEntity(dto: ProductUnitAttributeDTO): ProductUnitAttribute {
        val attribute = ProductUnitAttribute(dto.name, null)
        attribute.id = dto.id
        return attribute
    }

    fun convertToEntity(dto: CreateDateUnitAttributeDTO): CreateDateUnitAttribute {
        val attribute = CreateDateUnitAttribute(dto.name)
        attribute.value = dto.value
        attribute.id = dto.id
        return attribute
    }

    fun convertToEntity(dto: ProductDTO): Product {
        val product = Product(dto.name)
        product.id = dto.id
        return product
    }

    fun convertToDto(unit: Unit): UnitDTO =
        UnitDTO(unit.id, unit.name, UnitDefinitionDTO(unit.id, unit.name, null, null),
            unit.unitAttributes.map { convertToDto(it) }.toMutableList(), unit.createdDate, null)

    fun convertToDto(attribute: UnitAttribute): UnitAttributeDTO {
        return when (attribute) {
            is TextUnitAttribute -> convertToDto(attribute)
            is NumericUnitAttribute -> convertToDto(attribute)
            is DateUnitAttribute -> convertToDto(attribute)
            is ProductUnitAttribute -> convertToDto(attribute)
            is CreateDateUnitAttribute -> convertToDto(attribute)
            is UserUnitAttribute -> convertToDto(attribute)
            else -> throw IllegalArgumentException("Unknown attribute type")
        }
    }

    fun convertToDto(attribute: TextUnitAttribute): TextUnitAttributeDTO =
        TextUnitAttributeDTO(attribute.id, attribute.name, attribute.value)

    fun convertToDto(attribute: NumericUnitAttribute): NumericUnitAttributeDTO =
        NumericUnitAttributeDTO(attribute.id, attribute.name, attribute.value)

    fun convertToDto(attribute: DateUnitAttribute): DateUnitAttributeDTO =
        DateUnitAttributeDTO(attribute.id, attribute.name, attribute.value)

    fun convertToDto(attribute: CreateDateUnitAttribute): CreateDateUnitAttributeDTO =
        CreateDateUnitAttributeDTO(attribute.id, attribute.name, attribute.value!!)

    fun convertToDto(attribute: ProductUnitAttribute): ProductUnitAttributeDTO =
        ProductUnitAttributeDTO(attribute.id, attribute.name, convertToDto(attribute.product!!))

    fun convertToDto(product: Product): ProductDTO = ProductDTO(product.id, product.name)

    fun convertToDto(attribute: UserUnitAttribute): UserUnitAttributeDTO =
        UserUnitAttributeDTO(attribute.id, attribute.name, attribute.user)
}