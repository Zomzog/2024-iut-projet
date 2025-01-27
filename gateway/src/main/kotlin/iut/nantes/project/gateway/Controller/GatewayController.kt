package iut.nantes.project.gateway.Controller


import iut.nantes.project.products.DTO.ProductDTO
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient

@RestController
@RequestMapping("/api/v1")
class GatewayController {

    private val webClient: WebClient = WebClient.create()

    @PostMapping("/user")
    fun createUser(@RequestBody user: SecurityProperties.User): ResponseEntity<SecurityProperties.User> {
        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }

    @GetMapping("/products")
    fun getProducts(@RequestHeader("X-User") user: String): ResponseEntity<List<ProductDTO>> {
        val response = webClient.get().uri("http://products-service/api/v1/products")
            .header("X-User", user)
            .retrieve()
            .bodyToFlux(ProductDTO::class.java)
            .collectList()
            .block()
        return ResponseEntity.ok(response)
    }
}
