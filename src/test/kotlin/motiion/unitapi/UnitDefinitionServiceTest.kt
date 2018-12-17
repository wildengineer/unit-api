package motiion.unitapi

import motiion.unitapi.exception.ResourceNotFoundException
import motiion.unitapi.repository.UnitDefinitionRepository
import motiion.unitapi.service.UnitDefinitionService
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
class UnitDefinitionServiceTest {

    @Autowired
    lateinit var unitDefinitionService: UnitDefinitionService

    @Autowired
    lateinit var unitDefinitionRepository: UnitDefinitionRepository

    @Test
    fun `Can create unit definition`() {
        val unitDefinition = TestDataGenerator.buildInvalidUnitDefinition()
        unitDefinitionService.save(unitDefinition)

        val persisted = unitDefinitionRepository.findById(unitDefinition.id!!)
        assertTrue(persisted.isPresent)
    }

    @Test
    fun `Can delete unit definition`() {
        val unitDefinition = TestDataGenerator.buildInvalidUnitDefinition()
        unitDefinitionRepository.save(unitDefinition)

        val id = unitDefinition.id!!
        var persisted = unitDefinitionRepository.findById(id)
        assertTrue(persisted.isPresent)

        unitDefinitionService.delete(id)
        persisted =  unitDefinitionRepository.findById(id)
        assertTrue(!persisted.isPresent)
    }

    @Test
    @Transactional
    fun `Can copy unit definition`() {
        val unitDefinition = TestDataGenerator.buildInvalidUnitDefinition()
        unitDefinitionRepository.save(unitDefinition)
        val id = unitDefinition.id!!
        val copy = unitDefinitionService.copy(id)
        TestDataGenerator.verifyUnitDefinition(unitDefinition, copy, withIds = false)
    }

    @Test(expected = ResourceNotFoundException::class)
    fun `Throws ResourceNotFoundException when no UnitDefinition is found`() {
        unitDefinitionService.copy(55555L)
    }
}