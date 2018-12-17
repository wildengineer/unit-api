package motiion.unitapi.controller

import motiion.unitapi.controller.UnitModelConverter.convertToDto
import motiion.unitapi.controller.UnitModelConverter.convertToEntity
import motiion.unitapi.service.UnitService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class V1UnitController(@Autowired val unitService: UnitService)
    : V1BaseUnitController() {

    @GetMapping
    fun getUnits(): List<UnitDTO> {
        return unitService.getUnits().map(UnitModelConverter::convertToDto).toList()
    }

    @GetMapping("/{id}")
    fun getUnit(@PathVariable("id") id: Long): UnitDTO {
        return convertToDto(unitService.getUnitsById(id))
    }

    @PostMapping
    fun createUnit(@Valid @RequestBody unitDTO: UnitDTO): UnitDTO =
        convertToDto(unitService.create(convertToEntity(unitDTO)))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<DeleteMessage> {
        unitService.delete(id)
        return ResponseEntity.ok(DeleteMessage("Unit", id, true))
    }
}