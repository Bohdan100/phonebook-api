package corp.phonebook.errors

import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.http.HttpStatus

@ResponseStatus(HttpStatus.CONFLICT)
class EmailAlreadyExistsException(message: String) : RuntimeException(message)