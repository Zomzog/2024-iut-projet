package iut.nantes.project.stores.Service


import iut.nantes.project.products.DTO.ProductDTO
import iut.nantes.project.products.Exception.ProductException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


@Service
class WebClientService {

    private val webClient = WebClient.create("http://localhost:8080")

    fun getProductInfo(productId: String): ProductDTO {
        return webClient.get()
            .uri("/api/v1/products/$productId")
            .retrieve()
            .bodyToMono(ProductDTO::class.java)
            .block() ?: throw ProductException.ProductNotFoundException()
    }
}
