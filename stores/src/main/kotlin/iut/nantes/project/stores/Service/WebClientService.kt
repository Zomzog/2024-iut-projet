package iut.nantes.project.stores.Service


import iut.nantes.project.products.DTO.ProductDTO
import iut.nantes.project.products.Exception.ProductException
import iut.nantes.project.stores.DTO.StoreDTO
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


@Service
class WebClientService {

    private val productWebClient = WebClient.create("http://localhost:8081")
    private val storeWebClient = WebClient.create("http://localhost:8082")

    fun getProductInfo(productId: String): ProductDTO {
        return productWebClient.get()
            .uri("/api/v1/products/$productId")
            .retrieve()
            .bodyToMono(ProductDTO::class.java)
            .block() ?: throw ProductException.ProductNotFoundException()
    }

    fun getAllStoreInfo(): List<StoreDTO> {
        return storeWebClient.get()
            .uri("/api/v1/stores")
            .retrieve()
            .bodyToMono(List::class.java)
            .block() as List<StoreDTO>
    }
}
