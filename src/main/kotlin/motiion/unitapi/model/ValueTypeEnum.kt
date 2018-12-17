package motiion.unitapi.model

import motiion.unitapi.model.ValueTypeEnum.NUMERIC
import motiion.unitapi.model.ValueTypeEnum.TEXT
import java.time.LocalDateTime

enum class ValueTypeEnum {
    NUMERIC, TEXT, DATETIME, USER, PRODUCT, CREATE_TIME
}

interface TypeDefinition<T> {
    val key: ValueTypeEnum
    val constraints: Set<ValueConstraint<T>>
}

class NumberTypeDefinition(override val constraints: Set<ValueConstraint<Number>>) : TypeDefinition<Number> {
    override val key: ValueTypeEnum = NUMERIC
}

class TextTypeDefinition(override val constraints: Set<ValueConstraint<String>>) : TypeDefinition<String> {
    override val key: ValueTypeEnum = TEXT
}

class DateTypeDefinition(override val constraints: Set<ValueConstraint<LocalDateTime>>) : TypeDefinition<LocalDateTime> {
    override val key: ValueTypeEnum = ValueTypeEnum.DATETIME
}
