package motiion.unitapi.model

import java.util.*
import javax.persistence.*

@Entity
data class Product (
    @Column(nullable = false)
    val name: String
) : Identifiable()

@Entity
data class Unit (

    @Column(nullable = false)
    var name: String,

    @ManyToOne
    @JoinColumn(name = "unitDefinitionId")
    var unitDefinition: UnitDefinition?,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "unit", orphanRemoval = true)
    @Column
    var unitAttributes: MutableList<UnitAttribute>

) : Identifiable() {
    @Column(nullable = true)
    var createdDate: Date? = null

    @PrePersist
    fun prePersist() {
        this.createdDate = Date()
    }
}

@Entity
@DiscriminatorColumn(name = "valueType", discriminatorType = DiscriminatorType.INTEGER)
abstract class UnitAttribute(@Column val name: String) : Identifiable() {
    @ManyToOne
    @JoinColumn(name = "unitId", nullable = false)
    var unit: Unit? = null
}

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("1")
class NumericUnitAttribute(

    name: String,

    @Column(name = "numericValue")
    val value: Double

) : UnitAttribute(name)

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("2")
class TextUnitAttribute (

    name: String,

    @Column(name="textValue")
    val value: String

) : UnitAttribute(name)

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("3")
class DateUnitAttribute (

    name: String,

    @Column(name="dateValue", nullable = false)
    val value: Date
) : UnitAttribute(name)

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("4")
class CreateDateUnitAttribute(name: String) : UnitAttribute(name){

    @Column(name="dateValue", nullable = false)
    var value: Date? = null

    @PrePersist
    fun prePersist() {
        this.value = Date()
    }
}

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("5")
class ProductUnitAttribute (

    name: String,

    @ManyToOne
    @JoinColumn(name = "productId")
    val product: Product?

) : UnitAttribute(name)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("6")
class UserUnitAttribute(

    name: String

) : UnitAttribute(name) {
    @Column(name = "textValue")
    var user: String? = null
}

@Entity
open class LocationEvent (
    @Column(nullable = false)
    val longitude: Double,
    @Column(nullable = false)
    val latitude: Double,
    @ManyToOne
    @JoinColumn(name = "unitId")
    val unit: `Unit`
) : Identifiable(){
    @Column(nullable = false)
    var createdDate: Date? = null

    @PrePersist
    fun prePersist() {
        this.createdDate = Date()
    }
}




