package motiion.unitapi.controller

import motiion.unitapi.controller.DefinitionModelConverter.convertToDTO
import motiion.unitapi.service.UnitDefinitionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class V1PublishDefinitionController(val unitDefinitionService: UnitDefinitionService): V1BaseUnitDefinitionController() {

    @PostMapping("/{id}/publish")
    fun publish(@PathVariable("id") id: Long): ResponseEntity<PublishMessage> {
        val requestTimestamp = Date()
        unitDefinitionService.publish(id)
        return ResponseEntity.ok(PublishMessage(id, requestTimestamp))
    }

    @GetMapping("/published/current")
    fun getCurrentPublishedUnitDefinitions(): List<UnitDefinitionDTO> {
        return unitDefinitionService.getCurrentPublished().map { convertToDTO(it) }
    }
}