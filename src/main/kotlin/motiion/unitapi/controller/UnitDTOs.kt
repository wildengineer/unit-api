package motiion.unitapi.controller

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

abstract class IdentifiableDTO (
    var id : Long?
)

class ProductDTO(
    
    id: Long?,

    @Size(min = 1, max = 50)
    val name: String
) : IdentifiableDTO(id)

class UnitDTO (

    id: Long?,

    @Size(min = 1, max = 50)
    val name: String,

    val unitDefinitionDTO: UnitDefinitionDTO?,

    @Size(max = 200)
    val unitAttributes: List<UnitAttributeDTO>?,

    @NotNull
    val createdDate: Date?,

    @Size(max = 100)
    val locationHistory: MutableList<LocationEventDTO>?

) : IdentifiableDTO(id)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = NumericUnitAttributeDTO::class, name = "numeric"),
    JsonSubTypes.Type(value = TextUnitAttributeDTO::class, name = "text"),
    JsonSubTypes.Type(value = DateUnitAttributeDTO::class, name = "date"),
    JsonSubTypes.Type(value = CreateDateUnitAttributeDTO::class, name = "createDate"),
    JsonSubTypes.Type(value = ProductUnitAttributeDTO::class, name = "product"),
    JsonSubTypes.Type(value = UserUnitAttributeDTO::class, name = "user"))
abstract class UnitAttributeDTO(id: Long?, @NotNull @Size(min = 1, max = 50) val name: String) : IdentifiableDTO(id)

class NumericUnitAttributeDTO(id: Long?, name: String, @NotNull val value: Double) : UnitAttributeDTO(id, name)

class TextUnitAttributeDTO (id: Long?, name: String, @NotNull @Size(min = 1, max = 250)val value: String) : UnitAttributeDTO(id, name)

class DateUnitAttributeDTO (id: Long?, name: String, @NotNull val value: Date) : UnitAttributeDTO(id, name)

class CreateDateUnitAttributeDTO (id: Long?, name: String, @NotNull var value: Date?) : UnitAttributeDTO(id, name)

class ProductUnitAttributeDTO (id: Long?, name: String, @NotNull @Size(min = 1, max = 50) val value: ProductDTO?) : UnitAttributeDTO(id, name)

class UserUnitAttributeDTO (id: Long?, name: String, @NotNull @Size(min = 1, max = 50) val value: String?) : UnitAttributeDTO(id, name)

data class LocationEventDTO (
    @NotNull
    val longitude: Double,
    @NotNull
    val latitude: Double,
    @NotNull
    val createdDate: Date
)


