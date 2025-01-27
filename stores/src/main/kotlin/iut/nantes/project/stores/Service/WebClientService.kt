package iut.nantes.project.stores.Service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class WebClientService {

    private val webClient = WebClient.create("http://localhost:8080")

    fun getProductInfo(productId: String): Mono<String> {
        return webClient.get()
            .uri("/api/v1/products/$productId")
            .retrieve()
            .bodyToMono(String::class.java)
    }
}
