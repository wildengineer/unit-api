package motiion.unitapi.controller

import motiion.unitapi.controller.DefinitionModelConverter.convertToDTO
import motiion.unitapi.controller.DefinitionModelConverter.convertToEntity
import motiion.unitapi.model.UnitDefinition
import motiion.unitapi.service.UnitDefinitionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class V1UnitDefinitionController(@Autowired val unitDefinitionService: UnitDefinitionService)
    : V1BaseUnitDefinitionController() {

    @GetMapping
    fun getDefinitions(): List<UnitDefinitionDTO> {
        return unitDefinitionService.getDefinitions().map { convert(it) }
    }

    @GetMapping("/{id}")
    fun getDefinitions(@PathVariable("id") id: Long): UnitDefinitionDTO =
        convert(unitDefinitionService.getUnitDefinition(id))

    @PostMapping
    fun addUnitDefinition(@Valid @RequestBody dto: UnitDefinitionDTO): UnitDefinitionDTO =
        convert(unitDefinitionService.save(convert(dto)))

    @PostMapping("/{id}/copy")
    fun copyUnitDefinition(@PathVariable("id") id: Long): UnitDefinitionDTO =
        convert(unitDefinitionService.copy(id))

    @PutMapping("/{id}")
    fun updateUnitDefinition(@PathVariable("id") id: Long, @Valid @RequestBody unitDefinition: UnitDefinition):
        UnitDefinitionDTO = convert(unitDefinitionService.update(id, unitDefinition))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<DeleteMessage> {
        unitDefinitionService.delete(id)
        return ResponseEntity.ok(DeleteMessage("UnitDefinition", id, true))
    }

    private fun convert(dto: UnitDefinitionDTO): UnitDefinition {
        return convertToEntity(dto)
    }

    private fun convert(e: UnitDefinition): UnitDefinitionDTO {
        return convertToDTO(e)
    }
}