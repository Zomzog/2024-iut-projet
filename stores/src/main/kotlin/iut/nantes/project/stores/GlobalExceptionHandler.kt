package iut.nantes.project.stores
import iut.nantes.project.stores.Exception.ContactException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ContactException.ContactNotFoundException::class)
    fun handleContactNotFoundException(ex: ContactException.ContactNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ContactException.ContactIsInAStoreException::class)
    fun handleContactIsInAStoreException(ex: ContactException.ContactIsInAStoreException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class,
        MethodArgumentNotValidException::class,
        ContactException.InvalidIdFormatException::class,
        IllegalArgumentException::class,
        ContactException.InvalidDataException::class)
    fun handleIncorrectArgument(ex: Exception): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity("Internal server error: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
