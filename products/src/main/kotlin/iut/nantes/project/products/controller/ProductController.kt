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
        if (db.findFamilleByName(product.family.name)==null){
            return ResponseEntity.badRequest().body("La famille du produit n'existe pas")
        }
        val violationsProduct = db.validateProduct(product)
        val violationsFamille = db.validateFamille(product.family)
        if (violationsFamille.isNotEmpty() || violationsProduct.isNotEmpty()) {
            return ResponseEntity.badRequest().body("Erreurs dans les données fournies")
        }
        db.saveProduct(product).let { return ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }
}