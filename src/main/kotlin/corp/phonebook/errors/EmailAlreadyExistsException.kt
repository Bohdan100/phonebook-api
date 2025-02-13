package corp.phonebook.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class EmailAlreadyExistsException(message: String) : RuntimeException(message)