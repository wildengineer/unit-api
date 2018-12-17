package motiion.unitapi.controller

import motiion.unitapi.controller.DefinitionModelConverter.convertToDTO
import motiion.unitapi.controller.DefinitionModelConverter.convertToEntity
import motiion.unitapi.model.UnitAttributeDefinition
import motiion.unitapi.model.UnitDefinition
import motiion.unitapi.service.UnitAttributeDefinitionService
import motiion.unitapi.service.UnitDefinitionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/{id}/attributes")
class V1AttributeDefinitionController(@Autowired val unitAttributeDefinitionService: UnitAttributeDefinitionService) :
        V1BaseUnitDefinitionController() {

    @PostMapping
    fun addAttributeDefinition(@PathVariable("id") id: Long, @RequestBody @Valid dto: UnitAttributeDefinitionDTO):
            UnitAttributeDefinitionDTO = convert(unitAttributeDefinitionService.save(convert(dto)))

    @PutMapping("/{id}")
    fun updateAttributeDefinition(@PathVariable("id") id: Long, @Valid dto: UnitAttributeDefinitionDTO):
        UnitAttributeDefinitionDTO = convert(unitAttributeDefinitionService.update(id, convert(dto)))

    @DeleteMapping("/{id}")
    fun deleteAttributeDefinition(@PathVariable("id") id: Long): ResponseEntity<DeleteMessage> {
        unitAttributeDefinitionService.delete(id)
        ResponseEntity.ok("{ success: true }")
        return ResponseEntity.ok(DeleteMessage("UnitAttributeDefinition", id, true))
    }

    private fun convert(dto: UnitAttributeDefinitionDTO): UnitAttributeDefinition {
        return convertToEntity(dto)
    }

    private fun convert(e: UnitAttributeDefinition): UnitAttributeDefinitionDTO {
        return convertToDTO(e)
    }
}