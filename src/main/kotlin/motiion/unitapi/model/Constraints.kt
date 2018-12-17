package motiion.unitapi.model

interface ValueConstraint<in T> {
    fun test(value: T): Boolean
    fun errorMessage(name: String, value: T): String
}

data class RangedNumberValueConstraint(private val upper: Double, private val lower: Double) : ValueConstraint<Number> {
    override fun test(value: Number): Boolean {
        return value.toDouble() in lower..upper
    }

    override fun errorMessage(name: String, value: Number): String {
        return "Input {} is not in range".format(value)
    }
}

data class OptionValueConstraint<T>(private val options: Set<T>): ValueConstraint<T> {
    override fun test(value: T): Boolean {
        return value in options
    }

    override fun errorMessage(name: String, value: T): String {
        return "Input {} is not in {}".format(value, options.joinToString { it.toString() })
    }
}

data class TextLengthConstraint(private val maxLength: Int): ValueConstraint<String> {
    override fun test(value: String): Boolean {
        return value.length <= maxLength
    }

    override fun errorMessage(name: String, value: String): String {
        return "Input {} length is greater than {}".format(name, maxLength)
    }
}

