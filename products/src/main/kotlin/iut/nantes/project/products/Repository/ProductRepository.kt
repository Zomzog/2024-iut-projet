package iut.nantes.project.products.Repository

import iut.nantes.project.products.Entity.ProductEntity
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

// Interface custom
interface ProductRepositoryCustom {
    fun save(product: ProductEntity)
    fun findById(id: String): Optional<ProductEntity>
    fun findAll(): List<ProductEntity>
    fun existsByName(name: String): Boolean
    fun delete(product: ProductEntity)
}

// Interface JPA
interface ProductJpaRepository : JpaRepository<ProductEntity, UUID> {
    fun existsByName(name: String): Boolean
}

// Implémentation JPA
@Profile("!dev")
class ProductRepositoryJPA(private val productJpaRepository: ProductJpaRepository) : ProductRepositoryCustom {
    override fun save(product: ProductEntity) {
        productJpaRepository.save(product)
    }

    override fun findById(id: String): Optional<ProductEntity> {
        val uuid = UUID.fromString(id)
        return productJpaRepository.findById(uuid)
    }

    override fun findAll(): List<ProductEntity> {
        return productJpaRepository.findAll()
    }

    override fun existsByName(name: String): Boolean {
        return productJpaRepository.existsByName(name)
    }

    override fun delete(product: ProductEntity) {
        productJpaRepository.delete(product)
    }
}

// Implémentation InMemory
@Profile("dev")
class ProductRepositoryInMemory : ProductRepositoryCustom {
    private val products = mutableMapOf<UUID, ProductEntity>()

    override fun save(product: ProductEntity) {
        products[product.id] = product
    }

    override fun findById(id: String): Optional<ProductEntity> {
        return try {
            val uuid = UUID.fromString(id)
            Optional.ofNullable(products[uuid])
        } catch (e: IllegalArgumentException) {
            Optional.empty()
        }
    }

    override fun findAll(): List<ProductEntity> {
        return products.values.toList()
    }

    override fun existsByName(name: String): Boolean {
        return products.values.any { it.name == name }
    }

    override fun delete(product: ProductEntity) {
        products.remove(product.id)
    }
}
