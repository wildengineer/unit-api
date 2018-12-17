package motiion.unitapi.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletWebRequest
import javax.servlet.http.HttpServletRequest

/**
 * Error controller for json error responses
 */
@RestController
@RequestMapping(value = "/error", produces = ["application/json"])
class SimpleErrorController(@Autowired val errorAttributes: ErrorAttributes) : ErrorController {

    override fun getErrorPath(): String = "/error"

    @RequestMapping
    fun error(aRequest: HttpServletRequest): Map<String, Any> = getErrorAttributes(aRequest, false)

    private fun getErrorAttributes(request: HttpServletRequest,
                                     includeStackTrace: Boolean): Map<String, Any> {
        val webRequest = ServletWebRequest(request)
        return this.errorAttributes.getErrorAttributes(webRequest, includeStackTrace).filterNot { it.key == "errors" }
    }
}
