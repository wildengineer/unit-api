package motiion.unitapi.model

import java.util.*
import javax.persistence.*
import kotlin.collections.toSet

//TODO: Add base data class for user info and entity dates

@Entity
data class UnitAttributeDefinition(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    val valueType: ValueTypeEnum,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "unitAttributeDefinition")
    @Column
    var constraintDefinitions: MutableList<UnitAttributeConstraintDefinition>?
) : Identifiable() {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unitDefinitionId", nullable = false)
    var unitDefinition: UnitDefinition? = null
}

@MappedSuperclass
abstract class Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}

@Entity
@DiscriminatorColumn(name = "constraintType", discriminatorType = DiscriminatorType.INTEGER)
abstract class UnitAttributeConstraintDefinition : Identifiable() {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unitAttributeDefinitionId", nullable = false)
    var unitAttributeDefinition: UnitAttributeDefinition? = null
}

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("1")
data class RangedNumberUnitAttributeConstraintDefinition(

    @Column
    val lower: Double,

    @Column
    val upper: Double

) : UnitAttributeConstraintDefinition()

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("2")
data class TextLengthUnitAttributeConstraintDefinition(

    @Column
    val maxLength: Int

) : UnitAttributeConstraintDefinition()

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("3")
data class OptionsUnitAttributeConstraintDefinition(

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "constraint")
    @Column
    val options: Set<UnitAttributeOption>

) : UnitAttributeConstraintDefinition()

@Entity
data class UnitAttributeOption(
    @Column(name = "value")

    var value: String
) : Identifiable() {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unitAttributeConstraintDefinitionId", nullable = false)
    var constraint: OptionsUnitAttributeConstraintDefinition? = null

    companion object {
        fun setOf(vararg ts: String) = ts.map { s -> UnitAttributeOption(s) }.toSet()
    }
}

@Entity
data class UnitDefinition(

    @Column(nullable = false)
    var name: String,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "unitDefinition")
    @Column
    var unitAttributeDefinitions: MutableList<UnitAttributeDefinition>?
) : Identifiable() {
    @Column(nullable = true)
    var publishDate: Date? = null

    //TODO: Prevent all write operations on a published unitdefinition
    fun isPublished(): Boolean = this.publishDate != null
}


