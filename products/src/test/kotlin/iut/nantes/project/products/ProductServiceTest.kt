package iut.nantes.project.products

import iut.nantes.project.products.DTO.FamilyDTO
import iut.nantes.project.products.DTO.PriceDTO
import iut.nantes.project.products.DTO.ProductDTO
import iut.nantes.project.products.Entity.FamilyEntity
import iut.nantes.project.products.Entity.PriceEntity
import iut.nantes.project.products.Entity.ProductEntity
import iut.nantes.project.products.Exception.FamilyException
import iut.nantes.project.products.Exception.ProductException
import iut.nantes.project.products.Repository.FamilyRepositoryCustom
import iut.nantes.project.products.Repository.ProductRepositoryCustom
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.util.*

class ProductServiceTest {

    private val productRepository: ProductRepositoryCustom = mock()
    private val familyRepository: FamilyRepositoryCustom = mock()
    private val productService = ProductService(productRepository, familyRepository)

    @Test
    fun createProductShouldThrowInvalidIdFormatExceptionWhenFamilyIdIsNull() {
        val productDto = ProductDTO(UUID.randomUUID(), "Test Product", "Description", PriceDTO(100.0, "EUR"), FamilyDTO(null, "Test Family", "Description"))

        assertThrows<FamilyException.InvalidIdFormatException> {
            productService.createProduct(productDto)
        }
    }

    @Test
    fun createProductShouldThrowFamilyNotFoundExceptionWhenFamilyDoesNotExist() {
        val productDto = ProductDTO(UUID.randomUUID(), "Test Product", "Description",  PriceDTO(100.0, "EUR"), FamilyDTO(UUID.randomUUID(), "Test Family", "Description"))
        whenever(familyRepository.findById(any())).thenReturn(Optional.empty())

        assertThrows<FamilyException.FamilyNotFoundException> {
            productService.createProduct(productDto)
        }
    }

    @Test
    fun createProductShouldSaveAndReturnProductDTOWhenFamilyExists() {
        val familyEntity = FamilyEntity(UUID.randomUUID(), "Test Family", "Description")
        val productDto = ProductDTO(UUID.randomUUID(), "Test Product", "Description",  PriceDTO(100.0, "EUR"), familyEntity.toDto())
        val productEntity = ProductEntity(UUID.randomUUID(), productDto.name, productDto.description, productDto.price.toEntity(), familyEntity)

        whenever(familyRepository.findById(any())).thenReturn(Optional.of(familyEntity))
        doAnswer {
            val argument = it.getArgument(0) as ProductEntity
            assertNotNull(argument)
            assertEquals(productDto.name, argument.name)
            assertEquals(productDto.description, argument.description)
            assertEquals(productDto.price.amount, argument.price.amount)
            productEntity
        }.whenever(productRepository).save(any())

        val createdProduct = productService.createProduct(productDto)

        assertNotNull(createdProduct)
        assertEquals(productDto.name, createdProduct.name)
    }

    @Test
    fun getAllProductsShouldThrowIllegalArgumentExceptionWhenMinPriceIsGreaterThanOrEqualToMaxPrice() {
        assertThrows<IllegalArgumentException> {
            productService.getAllProducts(null, 100.0, 50.0)
        }
    }

    @Test
    fun getAllProductsShouldReturnProductDTOListWhenConditionsAreMet() {
        val familyEntity = FamilyEntity(UUID.randomUUID(), "Test Family", "Description")
        val productEntity = ProductEntity(UUID.randomUUID(), "Test Product", "Description", PriceEntity(100.0, "EUR"), familyEntity)
        whenever(productRepository.findAll()).thenReturn(listOf(productEntity))

        val products = productService.getAllProducts(null, null, null)

        assertFalse(products.isEmpty())
        assertEquals(1, products.size)
        assertEquals("Test Product", products[0].name)
    }

    @Test
    fun getProductByIdShouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        val id = UUID.randomUUID()
        whenever(productRepository.findById(id.toString())).thenReturn(Optional.empty())

        assertThrows<ProductException.ProductNotFoundException> {
            productService.getProductById(id)
        }
    }

    @Test
    fun getProductByIdShouldReturnProductDTOWhenProductExists() {
        val id = UUID.randomUUID()
        val familyEntity = FamilyEntity(UUID.randomUUID(), "Test Family", "Description")
        val productEntity = ProductEntity(id, "Test Product", "Description", PriceEntity(100.0, "EUR"), familyEntity)
        whenever(productRepository.findById(id.toString())).thenReturn(Optional.of(productEntity))

        val productDto = productService.getProductById(id)

        assertNotNull(productDto)
        assertEquals(productEntity.name, productDto.name)
    }

    @Test
    fun deleteProductShouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        val id = UUID.randomUUID()
        whenever(productRepository.findById(id.toString())).thenReturn(Optional.empty())

        assertThrows<ProductException.ProductNotFoundException> {
            productService.deleteProduct(id)
        }
    }
}
