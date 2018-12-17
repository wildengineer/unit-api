package motiion.unitapi.controller

import motiion.unitapi.model.ValueTypeEnum
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass


@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AttributeDefinitionDTOValidator::class])
annotation class UnitAttributeDefinitionDTOConstraint(
    val message: String = "Attribute not valid",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = []
)

open class AttributeDefinitionDTOValidator : ConstraintValidator<UnitAttributeDefinitionDTOConstraint, List<UnitAttributeDefinitionDTO>> {

    override fun isValid(dtos: List<UnitAttributeDefinitionDTO>,
                         cxt: ConstraintValidatorContext): Boolean {
        return dtos.map {
            val valueType = it.type
            return it.constraintDefinitions?.map { it2 ->
                return when (it2) {
                    is RangedAttributeConstraintDefinitionDTO -> {
                        ValueTypeEnum.NUMERIC == valueType && it2.lower < it2.upper
                    }
                    is OptionsUnitAttributeConstraintDefinitionDTO -> {
                        ValueTypeEnum.TEXT == valueType
                    }
                    is TextLengthAttributeConstraintDefinitionDTO -> {
                        ValueTypeEnum.TEXT == valueType
                    }
                    else -> throw IllegalStateException("Unknown constraint type $it2")
                }
            }?.all { true } ?: false
        }.all { true } && dtos.groupBy { it.name }.all { it.value.size == 1 }
    }
}
