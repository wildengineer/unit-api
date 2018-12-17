package motiion.unitapi.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String?) : RuntimeException(message)

@ResponseStatus(value = HttpStatus.CONFLICT)
class DuplicateEntityException(message: String?) : RuntimeException(message)

@ResponseStatus
class InvalidUnitException(message: String?) : RuntimeException(message)