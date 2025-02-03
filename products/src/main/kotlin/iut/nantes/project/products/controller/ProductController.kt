package iut.nantes.project.products.controller

import iut.nantes.project.products.DatabaseProxy
import iut.nantes.project.products.ProductsEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(val db: DatabaseProxy){

    @PostMapping("/api/v1/product")
    fun createProduct(@RequestBody product: ProductDto): ResponseEntity<Any> {
        val result = db.saveProduct(product)
        return if (result != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(result)
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product data")
        }
    }
}