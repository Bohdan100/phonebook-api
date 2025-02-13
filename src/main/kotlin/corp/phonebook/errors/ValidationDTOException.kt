package corp.phonebook.errors

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException

@ControllerAdvice
@Order(1)
class ValidationDTOException {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = HashMap<String, String>()
        for (error in ex.bindingResult.fieldErrors) {
            errors[error.field] = error.defaultMessage ?: "Validation error"
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }
}