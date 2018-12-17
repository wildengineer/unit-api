package motiion.unitapi.controller

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import motiion.unitapi.model.ValueTypeEnum
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UnitDefinitionDTO(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: Long?,
    @field:Size(min = 1, max = 50)
    val name: String,
    @field:Size(max = 100)
    @field:UnitAttributeDefinitionDTOConstraint
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val attributeDefinitions: List<UnitAttributeDefinitionDTO>?,
    val publishDate: Date?
)

data class UnitAttributeDefinitionDTO(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: Long?,
    @field:Size(min = 1, max = 50)
    @field:NotNull
    val name: String,
    @field:NotNull
    val type: ValueTypeEnum,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val constraintDefinitions: List<AttributeConstraintDefinitionDTO>?
)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = RangedAttributeConstraintDefinitionDTO::class, name = "range"),
    JsonSubTypes.Type(value = TextLengthAttributeConstraintDefinitionDTO::class, name = "textLength"),
    JsonSubTypes.Type(value = OptionsUnitAttributeConstraintDefinitionDTO::class, name = "options"))
interface AttributeConstraintDefinitionDTO

data class RangedAttributeConstraintDefinitionDTO(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: Long?,
    @field:NotNull
    val lower: Double,
    @field:NotNull
    val upper: Double
) : AttributeConstraintDefinitionDTO

data class TextLengthAttributeConstraintDefinitionDTO(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: Long?,
    @field:NotNull
    val maxLength: Int
) : AttributeConstraintDefinitionDTO

data class OptionsUnitAttributeConstraintDefinitionDTO(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: Long?,
    @field:NotNull
    @field:Size(min = 1, max = 100)
    val options: List<AttributeOptionDTO>
) : AttributeConstraintDefinitionDTO

data class AttributeOptionDTO(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: Long?,
    @field:NotNull
    @field:Size(min = 1, max = 250)
    val value: String
){
    companion object {
        fun of(vararg ts: String): List<AttributeOptionDTO> = ts.map { AttributeOptionDTO(null, it) }
    }
}


