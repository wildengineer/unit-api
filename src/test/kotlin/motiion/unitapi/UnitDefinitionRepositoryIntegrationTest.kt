package motiion.unitapi

import motiion.unitapi.model.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.junit4.SpringRunner
import javax.persistence.EntityManager

@RunWith(SpringRunner::class)
@SpringBootTest
class UnitDefinitionRepositoryIntegrationTest {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var victim: CrudRepository<UnitDefinition, Long>

    @Test
    fun `Can find unit definition by id`() {
        //Arrange
        val unitDefinition = TestDataGenerator.buildInvalidUnitDefinition()

        entityManager.persist(unitDefinition)

        //Act
        val persistedOptional = victim.findById(unitDefinition.id!!)

        //Assert
        assertTrue(persistedOptional.isPresent)
    }
}