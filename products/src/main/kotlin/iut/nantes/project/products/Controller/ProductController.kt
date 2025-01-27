package iut.nantes.project.products.Controller

import iut.nantes.project.products.DTO.ProductDTO
import iut.nantes.project.products.Exception.FamilyException
import iut.nantes.project.products.Exception.ProductException
import iut.nantes.project.products.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1")

class ProductController(private val productService: ProductService) {

    @PostMapping("/products")
    fun createProduct(@RequestBody productDto: ProductDTO): ResponseEntity<ProductDTO> {
        return try {
            val createdProduct = productService.createProduct(productDto)
            ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
        } catch (e: FamilyException.FamilyNotFoundException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping("/products")
    fun getAllProducts(
        @RequestParam(required = false) familyName: String?,
        @RequestParam(required = false) minPrice: Double?,
        @RequestParam(required = false) maxPrice: Double?
    ): ResponseEntity<List<ProductDTO>> {
        return try {
            val products = productService.getAllProducts(familyName, minPrice, maxPrice)
            ResponseEntity.ok(products)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping("/products/{id}")
    fun getProductById(@PathVariable id: UUID): ResponseEntity<ProductDTO> {
        return try {
            val product = productService.getProductById(id)
            ResponseEntity.ok(product)
        } catch (e: ProductException.ProductNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PutMapping("/products/{id}")
    fun updateProduct(@PathVariable id: UUID, @RequestBody productDto: ProductDTO): ResponseEntity<ProductDTO> {
        return try {
            val updatedProduct = productService.updateProduct(id, productDto)
            ResponseEntity.ok(updatedProduct)
        } catch (e: FamilyException.FamilyNotFoundException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: ProductException.ProductNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @DeleteMapping("/v1/products/{id}")
    fun deleteProduct(@PathVariable id: UUID): ResponseEntity<Void> {
        return try {
            productService.deleteProduct(id)
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } catch (e: ProductException.ProductNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: ProductException.ProductHasStockException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}
