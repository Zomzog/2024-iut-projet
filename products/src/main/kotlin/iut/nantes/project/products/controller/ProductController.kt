package iut.nantes.project.products.controller

import iut.nantes.project.products.DatabaseProxy
import iut.nantes.project.products.ProductsEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ProductController(val db: DatabaseProxy){

    @PostMapping("/api/v1/products")
    fun createProduct(@RequestBody product: ProductDto): ResponseEntity<Any> {
        val result = db.saveProduct(product)
        return if (result != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(result)
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Données de produit invalides")
        }
    }


    @GetMapping("api/v1/products")
    fun getProducts(
        @RequestParam familyname: String?,
        @RequestParam minprice: Int?,
        @RequestParam maxprice: Int?
    ): ResponseEntity<Any> {
        val products = db.findProductsByFamilyNameAndPriceRange(familyname, minprice, maxprice)
        return if (products.isNotEmpty()) {
            ResponseEntity.status(HttpStatus.OK).body(products)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun produit trouvé pour ces critères")
        }
    }

    @GetMapping("api/v1/products/{id}")
    fun getProductById(@PathVariable id: UUID): ResponseEntity<Any> {
        val result = db.findProductById(id)
        return if (result != null){
            ResponseEntity.status(HttpStatus.OK).body(result)
        }else{
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Données de produit invalides")
        }
    }

    @PutMapping("/api/v1/products/{id}")
    fun putProduct(@PathVariable id: UUID, @RequestBody product: ProductDto): ResponseEntity<Any> {
        val updatedProduct = db.updateProduct(id, product)

        return if (updatedProduct != null) {
            ResponseEntity.status(HttpStatus.OK).body(updatedProduct)
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La mise à jour a échoué. Vérifiez les données.")
        }
    }


    @DeleteMapping("/api/v1/products/{id}")
    fun deleteProduct(@PathVariable id: UUID): ResponseEntity<Any> {
        val result = db.findFamilleById(id)
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La famille n'existe pas")
        }

        if (db.findProductByFamilleId(id).isNotEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Au moins 1 produit de cette famille existe encore")
        }

        db.deleteFamilleById(id)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("La famille a été supprimée avec succès")
    }

}