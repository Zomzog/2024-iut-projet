package iut.nantes.project.products

class ProductController(
    private val productService: ProductService
) {
    fun createProduct(productDTO: ProductDTO): ProductEntity {
        val product = productDTO.toEntity()
        return productService.saveProduct(product)
    }

    fun getProductById(id: String): ProductDTO {
        val product = productService.getProductById(id)
        return product.toDTO()
    }
}

fun ProductDTO.toEntity(): ProductEntity {
    val product = ProductEntity(id,name, description, price, family)
    return product
}

fun ProductEntity.toDTO(): ProductDTO {
    val product = ProductDTO(id,name, description, price, family)
    return product
}
