package iut.nantes.project.products

class ProductService(
    private val repository: ProductRepository
) {
    fun saveProduct(product: ProductEntity): ProductEntity {
        repository.save(product)
        return product
    }

    fun getProductById(id: String): ProductEntity {
        return repository.findById(id).get()
    }
}
