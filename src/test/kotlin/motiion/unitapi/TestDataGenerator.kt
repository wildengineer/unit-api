@file:Suppress("UNCHECKED_CAST")

package motiion.unitapi

import motiion.unitapi.controller.*
import motiion.unitapi.model.*
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Assert.assertEquals
import java.time.LocalDate
import java.util.*

object TestDataGenerator {

    private const val seed = 51L
    private val random: Random = Random(seed)

    fun generateName() = UUID.randomUUID().toString()
    fun generateLong() = random.nextLong()
    fun generateDouble() = random.nextDouble()
    fun generateInt() = random.nextInt()

    fun generateDaysFromNow(days: Int): Date {
        val date = Calendar.getInstance()   // 19-01-2018
        date.add(Calendar.DATE, days)
        return date.time
    }

    fun buildValidUnitDefinition(): UnitDefinition {
        val rangeConstraint = RangedNumberUnitAttributeConstraintDefinition(generateDouble(),
            generateDouble())
        val textLengthConstraint = TextLengthUnitAttributeConstraintDefinition(generateInt())
        val options = UnitAttributeOption.setOf("Arabic", "Robusto", "Gheisha")
        val optionsConstraint = OptionsUnitAttributeConstraintDefinition(options)
        options.forEach { it.constraint = optionsConstraint }

        val textConstraints = mutableListOf(textLengthConstraint, optionsConstraint)
        val textAttributeDefinition = UnitAttributeDefinition(generateName(), ValueTypeEnum.TEXT,
            textConstraints)
        textConstraints.forEach {it -> it.unitAttributeDefinition = textAttributeDefinition }

        val numericConstraints: MutableList<UnitAttributeConstraintDefinition> = mutableListOf(rangeConstraint)
        val numericUnitAttributeDefinition = UnitAttributeDefinition(generateName(), ValueTypeEnum.NUMERIC,
            numericConstraints)
        numericConstraints.forEach {it -> it.unitAttributeDefinition = numericUnitAttributeDefinition }

        val dateTimeUnitAttributeDefinition = UnitAttributeDefinition(generateName(), ValueTypeEnum.DATETIME,
            null)

        val definitionName = TestDataGenerator.generateName()
        val attributeDefinitions = mutableListOf(textAttributeDefinition, numericUnitAttributeDefinition, dateTimeUnitAttributeDefinition)
        val unitDefinition = UnitDefinition(definitionName, attributeDefinitions)
        attributeDefinitions.forEach { it.unitDefinition = unitDefinition}
        return unitDefinition
    }

    fun buildUnitDefWithDupeAttributes(): UnitDefinitionDTO {
        val attributeDTOs = mutableListOf(UnitAttributeDefinitionDTO(null, "foo", ValueTypeEnum.DATETIME, null),
            UnitAttributeDefinitionDTO(null, "foo", ValueTypeEnum.DATETIME, null),
            UnitAttributeDefinitionDTO(null, "foo", ValueTypeEnum.DATETIME, null))

        return UnitDefinitionDTO(null, "DuplicateAttrDef", attributeDTOs, null)
    }

    fun buildInvalidUnitDefinition(): UnitDefinition {
        val rangeConstraint = RangedNumberUnitAttributeConstraintDefinition(generateDouble(),
                generateDouble())
        val textLengthConstraint = TextLengthUnitAttributeConstraintDefinition(generateInt())
        val options = UnitAttributeOption.setOf("Arabic", "Robusto", "Gheisha")
        val optionsConstraint = OptionsUnitAttributeConstraintDefinition(options)
        options.forEach { it.constraint = optionsConstraint }

        val optionConstraints = mutableListOf(rangeConstraint, textLengthConstraint, optionsConstraint)
        val unitAttributeDefinition = UnitAttributeDefinition(generateName(), ValueTypeEnum.TEXT, optionConstraints)
        optionConstraints.forEach {it -> it.unitAttributeDefinition = unitAttributeDefinition }

        val definitionName = TestDataGenerator.generateName()
        val attributeDefinitions = mutableListOf(unitAttributeDefinition)
        val unitDefinition = UnitDefinition(definitionName, attributeDefinitions)
        attributeDefinitions.forEach { it.unitDefinition = unitDefinition}
        return unitDefinition
    }

    fun verifyUnitDefinition(expected: UnitDefinition, actual: UnitDefinition, withIds: Boolean = true) {

        if (withIds) Assert.assertThat(actual.id, Matchers.`is`(expected.id!!))
        Assert.assertThat(actual.name, Matchers.`is`(expected.name))
        Assert.assertTrue(actual.unitAttributeDefinitions?.isNotEmpty()!!)

        //Compare Attribute Definitions
        val expectedAttrDefList = expected.unitAttributeDefinitions!!
        val actualAttrDefList = actual.unitAttributeDefinitions!!
        Assert.assertNotNull(expectedAttrDefList)
        Assert.assertNotNull(actualAttrDefList)
        Assert.assertEquals(expectedAttrDefList.size, actualAttrDefList.size)
        (expectedAttrDefList zip actualAttrDefList).forEach {

            val expectedAttrDef: UnitAttributeDefinition = it.first
            val actualAttrDef: UnitAttributeDefinition = it.second

            if (withIds) Assert.assertEquals(expectedAttrDef.id, actualAttrDef.id)
            Assert.assertEquals(expectedAttrDef.name, actualAttrDef.name)
            Assert.assertEquals(expectedAttrDef.valueType, actualAttrDef.valueType)

            Assert.assertTrue(expectedAttrDef.constraintDefinitions?.isNotEmpty()!!)
            Assert.assertTrue(actualAttrDef.constraintDefinitions?.isNotEmpty()!!)

            val expectedConstraintDefinitions = expectedAttrDef.constraintDefinitions!!
            val actualConstraintDefinitions = actualAttrDef.constraintDefinitions!!
            (expectedConstraintDefinitions zip actualConstraintDefinitions).forEach { it2 ->
                val (expectedConstraint, actualConstraint) = it2
                Assert.assertNotNull(actualConstraint)
                Assert.assertNotNull(expectedConstraint)

                if (withIds) assertEquals(expectedConstraint.id, actualConstraint.id)

                when (actualConstraint) {
                    is RangedNumberUnitAttributeConstraintDefinition -> {
                        val actualRanged = actualConstraint as? RangedNumberUnitAttributeConstraintDefinition
                        val expectedRanged = expectedConstraint as? RangedNumberUnitAttributeConstraintDefinition
                        Assert.assertNotNull(expectedRanged)
                        Assert.assertEquals(expectedRanged?.lower, actualRanged?.lower)
                        Assert.assertEquals(expectedRanged?.upper, actualRanged?.upper)
                    }
                    is TextLengthUnitAttributeConstraintDefinition -> {
                        val actualTextConstraint = actualConstraint as? TextLengthUnitAttributeConstraintDefinition
                        val expectedTextConstraint = expectedConstraint as? TextLengthUnitAttributeConstraintDefinition
                        Assert.assertNotNull(expectedTextConstraint)
                        Assert.assertEquals(expectedTextConstraint?.maxLength, actualTextConstraint?.maxLength)
                    }
                    is OptionsUnitAttributeConstraintDefinition -> {
                        val actualOptionsConstraint = actualConstraint as? OptionsUnitAttributeConstraintDefinition
                        val expectedOptionsConstraint = expectedConstraint as? OptionsUnitAttributeConstraintDefinition
                        Assert.assertNotNull(expectedOptionsConstraint)
                        Assert.assertNotNull(actualOptionsConstraint?.options)
                        Assert.assertNotNull(expectedOptionsConstraint?.options)
                        (expectedOptionsConstraint!!.options zip actualOptionsConstraint!!.options).forEach { it3 ->
                            val (expectedOption, actualOption) = it3
                            if (withIds) assertEquals(expectedOption.id, actualOption.id)
                            Assert.assertEquals(expectedOption.value, actualOption.value)
                        }
                    }
                }
            }
        }
    }

    fun buildUnitAndDefinitionPair(): Pair<UnitDefinitionDTO, UnitDTO> {

        val descAttrDef = UnitAttributeDefinitionDTO(null, "Description of unit", ValueTypeEnum.TEXT,
            listOf(TextLengthAttributeConstraintDefinitionDTO(null, 200)))
        val percentAttrDef = UnitAttributeDefinitionDTO(null, "Fat Percentage", ValueTypeEnum.NUMERIC,
            listOf(RangedAttributeConstraintDefinitionDTO(null, 0.toDouble(), 3.toDouble())))
        val soldByAttrDef = UnitAttributeDefinitionDTO(null, "Sell by Date", ValueTypeEnum.DATETIME, null)
        val creatorAttrDef = UnitAttributeDefinitionDTO(null, "Unit Creator", ValueTypeEnum.USER, null)
        val regionAttrDef = UnitAttributeDefinitionDTO(null, "Region of Origin", ValueTypeEnum.TEXT,
            listOf(OptionsUnitAttributeConstraintDefinitionDTO(null, AttributeOptionDTO.of("New York, New Jersey, Connecticut"))))
        val productAttrDef = UnitAttributeDefinitionDTO(null, "Product", ValueTypeEnum.PRODUCT, null)
        val createTimeAttrDef = UnitAttributeDefinitionDTO(null, "Creation Date", ValueTypeEnum.CREATE_TIME, null)
        val unitDef = UnitDefinitionDTO(null, "Milk",
            listOf(descAttrDef, percentAttrDef, soldByAttrDef, creatorAttrDef,
                regionAttrDef, productAttrDef, createTimeAttrDef), null)

        val descAttr = TextUnitAttributeDTO(null, "Description of unit", "A gallon of milk from Eddy's Farm")
        val percentAttr = NumericUnitAttributeDTO(null, "Fat Percentage", 0.2)
        val soldByAttr = DateUnitAttributeDTO(null, "Sell by Date", generateDaysFromNow(15))
        val creatorAttr = UserUnitAttributeDTO(null, "Unit Creator", null)
        val regionAttr = TextUnitAttributeDTO(null, "Region of Origin", "New York")
        val productAttr = ProductUnitAttributeDTO(null, "Product", null)
        val createTimeAttr = CreateDateUnitAttributeDTO(null, "Creation Date", null)

        val unit = UnitDTO(null, "Milk", null,
            listOf(descAttr, percentAttr, soldByAttr, creatorAttr, regionAttr, productAttr, createTimeAttr), null, null)
        return Pair(unitDef, unit)
    }

}