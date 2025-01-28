package iut.nantes.project.products

import iut.nantes.project.products.Controller.ProductController
import iut.nantes.project.products.DTO.FamilyDTO
import iut.nantes.project.products.DTO.PriceDTO


import iut.nantes.project.products.DTO.ProductDTO
import iut.nantes.project.products.Exception.FamilyException
import iut.nantes.project.products.Service.FamilyService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach

@WebMvcTest(ProductController::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var productService: ProductService

    @MockBean
    private lateinit var familleService: FamilyService

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
    }

    @Test
    fun createProductShouldReturnCreatedStatus() {
        val famille = FamilyDTO(UUID.randomUUID(), "Test Family", "Description")
        whenever(familleService.createFamily(any())).thenReturn(famille)
        val productDto = ProductDTO(UUID.randomUUID(), "Test Product", "Description", PriceDTO(100.0, "EUR"),famille )
        whenever(productService.createProduct(any())).thenReturn(productDto)

        mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{"name": "Test Product", "description": "Description", "price": {"amount": 100.0, "currency": "EUR"}, "family": {"id": "${productDto.family.id}", "name" :"Test Family", "description" : "Description" }}"""))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Test Product"))
    }


    @Test
    fun createProductShouldReturnError() {
        val productDto = ProductDTO(UUID.randomUUID(), "Test Product", "Description", PriceDTO(100.0, "EUR"), FamilyDTO(null, "testest", "lala"))
        whenever(productService.createProduct(any())).thenThrow(FamilyException.FamilyNotFoundException::class.java)

        mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDto)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun getAllProductsShouldReturnOkStatus() {
        val productDto = ProductDTO(UUID.randomUUID(), "Test Product", "Description", PriceDTO(100.0, "EUR"), FamilyDTO(UUID.randomUUID(), "Test Family", "Description"))
        whenever(productService.getAllProducts(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(listOf(productDto))

        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Test Product"))
    }

    @Test
    fun getProductByIdShouldReturnOkStatus() {
        val id = UUID.randomUUID()
        val productDto = ProductDTO(id, "Test Product", "Description", PriceDTO(100.0, "EUR"), FamilyDTO(UUID.randomUUID(), "Test Family", "Description"))
        whenever(productService.getProductById(id)).thenReturn(productDto)

        mockMvc.perform(get("/api/v1/products/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Product"))
    }

    @Test
    fun updateProductShouldReturnOkStatus() {
        val id = UUID.randomUUID()
        val productDto = ProductDTO(id, "Updated Product", "Updated Description", PriceDTO(200.0, "EUR"), FamilyDTO(UUID.randomUUID(), "Test Family", "Updated Description"))
        whenever(productService.updateProduct(eq(id), any())).thenReturn(productDto)

        mockMvc.perform(put("/api/v1/products/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{"name": "Test Product", "description": "Description", "price": {"amount": 200.0, "currency": "EUR"}, "family": {"id": "${productDto.family.id}", "name" :"Test Family", "description" : "Description" }}"""))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Product"))
    }

    @Test
    fun deleteProductShouldReturnNoContentStatus() {
        val id = UUID.randomUUID()
        doNothing().whenever(productService).deleteProduct(id)

        mockMvc.perform(delete("/api/v1/products/$id"))
            .andExpect(status().isNoContent)
    }
}
