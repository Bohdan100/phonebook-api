package corp.phonebook.errors

import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.security.access.AccessDeniedException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import java.util.Date

@RestControllerAdvice
class GlobalExceptionHandler: ResponseEntityExceptionHandler() {
    @ExceptionHandler(EmailAlreadyExistsException::class)
    fun handleEmailAlreadyExistsException(
        ex: EmailAlreadyExistsException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = HttpStatus.CONFLICT.reasonPhrase,
            message = ex.message ?: "Email already exists",
            timestamp = Date()
        )
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    data class ErrorResponse(
        val status: Int,
        val error: String,
        val message: String,
        val timestamp: Date
    )

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<Any> {
        val body = LinkedHashMap<String, Any>()
        body["status"] = HttpStatus.NOT_FOUND.value()
        body["error"] = "Not Found"
        body["message"] = ex.message ?: "Resource not found"
        body["timestamp"] = System.currentTimeMillis()

        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<Any> {
        val body = LinkedHashMap<String, Any>()
        body["status"] = HttpStatus.FORBIDDEN.value()
        body["error"] = "Access Denied"
        body["message"] = ex.message ?: "You do not have permission to access this resource"
        body["timestamp"] = System.currentTimeMillis()

        return ResponseEntity(body, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Any> {
        val body = LinkedHashMap<String, Any>()
        body["status"] = HttpStatus.BAD_REQUEST.value()
        body["error"] = "Bad Request"
        body["message"] = ex.message ?: "Invalid input"
        body["timestamp"] = System.currentTimeMillis()

        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors = HashMap<String, String>()
        for (error in ex.bindingResult.fieldErrors) {
            errors[error.field] = error.defaultMessage ?: "Validation error"
        }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val body = LinkedHashMap<String, Any>()
        body["status"] = status.value()
        body["error"] = "Malformed JSON request"
        body["message"] = ex.message ?: "Invalid JSON format"
        body["timestamp"] = System.currentTimeMillis()

        return ResponseEntity(body, headers, status)
    }
}