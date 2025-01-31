package iut.nantes.project.stores

import iut.nantes.project.stores.DTO.AddressDTO
import iut.nantes.project.stores.DTO.ContactDTO
import iut.nantes.project.stores.DTO.ProductStoreDTO
import iut.nantes.project.stores.DTO.StoreDTO
import iut.nantes.project.stores.Exception.StoreException
import iut.nantes.project.stores.Service.ContactService
import iut.nantes.project.stores.Service.StoreService
import org.mockito.kotlin.any
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
    final val id = UUID.randomUUID()
    val storeDto =
        StoreDTO(1, "Atlantis", contactDto, listOf(ProductStoreDTO(id, "RC 500", 1)))

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
                            "id": "$id",
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
                            "id": "$id",
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
                .andExpect(status().`is`(400))
    }

    @Test
    fun getNotExistingStoreByID() {
        whenever(storeService.getStoreById(any())).thenThrow(StoreException.StoreNotFoundException())

        mockMvc.perform(
            get("/api/v1/stores/2"))
            .andExpect(status().isNotFound)
    }
}