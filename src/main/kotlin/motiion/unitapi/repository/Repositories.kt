package motiion.unitapi.repository

import motiion.unitapi.model.UnitAttributeConstraintDefinition
import motiion.unitapi.model.UnitAttributeDefinition
import motiion.unitapi.model.UnitDefinition
import motiion.unitapi.model.Unit
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UnitDefinitionRepository: CrudRepository<UnitDefinition, Long> {
    fun findByName(name: String): List<UnitDefinition>
    fun findFirstByNameOrderByPublishDateDesc(name: String): UnitDefinition
    @Query("SELECT d1 FROM UnitDefinition d1 WHERE d1.publishDate in (SELECT MAX(d2.publishDate) FROM UnitDefinition d2 WHERE d2.id = d1.id)")
    fun findCurrentPublished(): List<UnitDefinition>
}

@Repository
interface UnitAttributeDefinitionRepository: CrudRepository<UnitAttributeDefinition, Long>

@Repository
interface UnitRepository: CrudRepository<Unit, Long> {
    fun findByName(name: String): List<Unit>
}

