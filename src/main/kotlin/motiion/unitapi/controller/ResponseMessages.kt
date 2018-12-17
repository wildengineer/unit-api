package motiion.unitapi.controller

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import motiion.unitapi.model.ValueTypeEnum
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class PublishMessage(val newId: Long, val timestamp: Date)

data class DeleteMessage(val resourceType: String, val id: Long, val childrenDeleted: Boolean)
