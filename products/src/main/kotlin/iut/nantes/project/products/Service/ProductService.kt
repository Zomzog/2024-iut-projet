package iut.nantes.project.products

import iut.nantes.project.products.DTO.ProductDTO
import iut.nantes.project.products.Entity.ProductEntity
import iut.nantes.project.products.Repository.FamilyRepository
import iut.nantes.project.products.Repository.ProductRepository
import java.util.*

class ProductService(private val productRepository: ProductRepository, private val familyRepository: FamilyRepository) {

    fun createProduct(productDto: ProductDTO): ProductDTO {
        val familyId = productDto.family.id ?: throw FamilyException.InvalidIdFormatException()
        val family = familyRepository.findById(familyId)
            .orElseThrow { FamilyException.FamilyNotFoundException() }

        val product = ProductEntity(UUID.randomUUID(), productDto.name, productDto.description, productDto.price.toEntity(), family)
        productRepository.save(product)
        return product.toDto()
    }

    fun getAllProducts(familyName: String?, minPrice: Double?, maxPrice: Double?): List<ProductDTO> {
        if (minPrice != null && maxPrice != null && minPrice >= maxPrice) {
            throw IllegalArgumentException("minPrice doit être inférieur à maxPrice")
        }

        val products = productRepository.findAll().filter {
            (familyName == null || it.family.name == familyName) &&
                    (minPrice == null || it.price.amount >= minPrice) &&
                    (maxPrice == null || it.price.amount <= maxPrice)
        }
        return products.map { it.toDto() }
    }

    fun getProductById(id: UUID): ProductDTO {
        val product = productRepository.findById(id.toString())
            .orElseThrow { ProductException.ProductNotFoundException() }
        return product.toDto()
    }

    fun updateProduct(id: UUID, productDto: ProductDTO): ProductDTO {
        val product = productRepository.findById(id.toString())
            .orElseThrow { ProductException.ProductNotFoundException() }

        val familyId = productDto.family.id ?: throw FamilyException.InvalidIdFormatException()
        val family = familyRepository.findById(familyId)
            .orElseThrow { FamilyException.FamilyNotFoundException() }

        product.name = productDto.name
        product.description = productDto.description
        product.price = productDto.price.toEntity()
        product.family = family

        productRepository.save(product)
        return product.toDto()
    }

    fun deleteProduct(id: UUID) {
        val product = productRepository.findById(id.toString())
            .orElseThrow { ProductException.ProductNotFoundException() }

        if (productHasStock(product)) {
            throw ProductException.ProductHasStockException()
        }

        productRepository.delete(product)
    }

    private fun productHasStock(product: ProductEntity): Boolean {
        // TODO : A IMPLEMENTER QUAND STORE SERA IMPLEMENTE
        return false
    }
}
