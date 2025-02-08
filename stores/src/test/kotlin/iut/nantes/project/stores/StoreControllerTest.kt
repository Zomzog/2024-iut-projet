package iut.nantes.project.stores

import iut.nantes.project.products.DTO.FamilyDTO
import iut.nantes.project.products.DTO.PriceDTO
import iut.nantes.project.products.DTO.ProductDTO
import iut.nantes.project.products.Exception.ProductException
import iut.nantes.project.stores.DTO.AddressDTO
import iut.nantes.project.stores.DTO.ContactDTO
import iut.nantes.project.stores.DTO.ProductStoreDTO
import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Exception.StoreException
import iut.nantes.project.stores.Service.StoreService
import org.mockito.Mockito.doNothing
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class StoreControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var storeService: StoreService

    private val contactDto = ContactDTO(1, "my@email.com", "0123456789", AddressDTO("Rue truc", "Nantes", "44300"))
    final val productID = UUID.randomUUID()
    private val productStoreDTO = ProductStoreDTO(productID, "Test Product", 1)

    val storeDto =
        StoreDTO(1, "Atlantis", contactDto, listOf(ProductStoreDTO(productID, "RC 500", 1)))

    @Test
    fun createValidStore() {
        whenever(storeService.createStore(any())).thenReturn(storeDto)

        mockMvc.perform(
            post("/api/v1/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name": "Atlantis",
                    "contact": {
                        "id": 1,
                        "email": "my@email.com",
                        "phone": "0123456789",
                        "address": {
                            "street": "Rue truc",
                            "city": "Nantes",
                            "postalCode": "44300"
                        }
                    },  "products": [{
                            "id": "$productID",
                            "name": "RC 500",
                            "quantity": 1
                        }]}"""))
            .andExpect(status().isCreated)
    }

    @Test
    fun createInvalidStore() {
        whenever(storeService.createStore(any())).thenThrow(StoreException.InvalidDataException())

        mockMvc.perform(
            post("/api/v1/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name": "At",
                    "contact": {
                        "id": 1,
                        "email": "my@email.com",
                        "phone": "0123456789",
                        "address": {
                            "street": "Rue truc",
                            "city": "Nantes",
                            "postalCode": "44300"
                        }
                    },  "products": [{
                            "id": "$productID",
                            "name": "RC 500",
                            "quantity": 1
                        }]}"""))
            .andExpect(status().`is`(400))
    }

    @Test
    fun getStoreByID() {
        whenever(storeService.getStoreById(any())).thenReturn(storeDto)

        mockMvc.perform(
            get("/api/v1/stores/1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name").value("Atlantis"))
    }

    @Test
    fun getStoreByInvalidID() {
        whenever(storeService.getStoreById(any())).thenThrow(StoreException.InvalidIdFormatException())

        mockMvc.perform(
            get("/api/v1/stores/a"))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun getNotExistingStoreByID() {
        whenever(storeService.getStoreById(any())).thenThrow(StoreException.StoreNotFoundException())

        mockMvc.perform(
            get("/api/v1/stores/2"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateStoreSuccess() {
        whenever(storeService.updateStore(eq(1), any())).thenReturn(storeDto)

        mockMvc.perform(
            put("/api/v1/stores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name": "Atlantis",
                    "contact": {
                        "id": 1,
                        "email": "my@email.com",
                        "phone": "0123456789",
                        "address": {
                            "street": "Rue truc",
                            "city": "Nantes",
                            "postalCode": "44300"
                        }
                    },  "products": [{
                            "id": "$productID",
                            "name": "RC 500",
                            "quantity": 1
                        }]}"""))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Atlantis"))
    }

    @Test
    fun updateStoreFailure() {
        whenever(storeService.updateStore(eq(1), any())).thenReturn(storeDto)

        mockMvc.perform(
            put("/api/v1/stores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name": "At",
                    "contact": {
                        "id": 1,
                        "email": "my@email.com",
                        "phone": "0",
                        "address": {
                            "street": "Rue",
                            "city": "Nan",
                            "postalCode": "0"
                        }
                    },  "products": [{
                            "id": "$productID",
                            "name": "RC 500",
                            "quantity": 1
                        }]}"""))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun deleteStoreSuccess() {
        doNothing().whenever(storeService).deleteStore(any())

        mockMvc.perform(
            delete("/api/v1/stores/1"))
            .andExpect(status().`is`(204))
    }

    @Test
    fun deleteStore_InvalidID() {
        doNothing().whenever(storeService).deleteStore(any())

        mockMvc.perform(
            delete("/api/v1/stores/a"))
            .andExpect(status().`is`(400))
    }

    @Test
    fun deleteStore_DoesntExist() {
        whenever(storeService.deleteStore(any())).thenThrow(StoreException.StoreNotFoundException())

        mockMvc.perform(
            delete("/api/v1/stores/2"))
            .andExpect(status().`is`(404))
    }

    /*------------------- Tests pour stock ---------------------*/

    @Test
    fun addValidStock() {
        whenever(storeService.addProductToStore(eq(1), any(), eq(1)))
            .thenReturn(productStoreDTO)

        mockMvc.perform(
            post("/api/v1/stores/1/products/$productID/add"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Product"))

    }

    @Test
    fun addInvalidStock() {
        whenever(storeService.addProductToStore(eq(1), any(), eq(1)))
            .thenThrow(IllegalArgumentException())

        mockMvc.perform(
            post("/api/v1/stores/1/products/$productID/add"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun addStock_StoreDoesntExist() {
        whenever(storeService.addProductToStore(eq(1), any(), eq(1)))
            .thenThrow(StoreException.StoreNotFoundException())

        mockMvc.perform(
            post("/api/v1/stores/1/products/$productID/add"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun removeValidStock() {
        whenever(storeService.removeProductFromStore(eq(1), any(), eq(1)))
            .thenReturn(productStoreDTO)

        mockMvc.perform(
            post("/api/v1/stores/1/products/$productID/remove"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Product"))
    }

    @Test
    fun removeInvalidStock() {
        whenever(storeService.removeProductFromStore(eq(1), any(), eq(1)))
            .thenThrow(IllegalArgumentException())

        mockMvc.perform(
            post("/api/v1/stores/1/products/$productID/remove"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun removeStock_StoreDoesntExist() {
        whenever(storeService.removeProductFromStore(eq(1), any(), eq(1)))
            .thenThrow(StoreException.StoreNotFoundException())

        mockMvc.perform(
            post("/api/v1/stores/1/products/$productID/remove"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun removeStock_ProductNotInStore() {
        whenever(storeService.removeProductFromStore(eq(1), any(), eq(1)))
            .thenThrow(ProductException.ProductNotFoundException())

        mockMvc.perform(
            post("/api/v1/stores/1/products/$productID/remove"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun deleteStocksSucces(){
        doNothing().whenever(storeService).deleteProductsFromStore(eq(1), any())

        mockMvc.perform(
            delete("/api/v1/stores/1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""["e437f62a-432e-4aef-a440-6c86d3b09901",
                          "9a852ad2-4b0a-4f74-8e23-5305b733c263"]"""))
            .andExpect(status().`is`(204))
    }

    @Test
    fun deleteStocksInvalid(){
        doNothing().whenever(storeService).deleteProductsFromStore(eq(1), any())

        mockMvc.perform(
            delete("/api/v1/stores/1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""["1",
                          "9a852ad2-4b0a-4f74-8e23-5305b733c263"]"""))
            .andExpect(status().`is`(400))
    }

    @Test
    fun deleteStocks_StoreDoesntExist(){
        whenever(storeService.deleteProductsFromStore(eq(1), any())).thenThrow(StoreException.StoreNotFoundException())

        mockMvc.perform(
            delete("/api/v1/stores/1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""["e437f62a-432e-4aef-a440-6c86d3b09901",
                          "9a852ad2-4b0a-4f74-8e23-5305b733c263"]"""))
            .andExpect(status().`is`(404))
    }
}