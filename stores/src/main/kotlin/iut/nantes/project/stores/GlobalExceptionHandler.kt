package iut.nantes.project.stores
import iut.nantes.project.products.Exception.ProductException
import iut.nantes.project.stores.Exception.ContactException
import iut.nantes.project.stores.Exception.StoreException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ContactException.ContactNotFoundException::class,
        StoreException.StoreNotFoundException::class,
        ProductException.ProductNotFoundException::class)
    fun handleContactNotFoundException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ContactException.ContactIsInAStoreException::class,
        IllegalStateException::class)
    fun handleContactIsInAStoreException(ex: ContactException.ContactIsInAStoreException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class,
        MethodArgumentNotValidException::class,
        ContactException.InvalidIdFormatException::class,
        StoreException.InvalidIdFormatException::class,
        IllegalArgumentException::class,
        ContactException.InvalidDataException::class,
        StoreException.InvalidDataException::class,
        HttpMessageNotReadableException::class)
    fun handleIncorrectArgument(ex: Exception): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity("Internal server error: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
