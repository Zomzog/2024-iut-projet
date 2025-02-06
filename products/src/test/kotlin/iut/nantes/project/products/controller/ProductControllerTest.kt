package iut.nantes.project.products.controller

import jakarta.transaction.Transactional
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import kotlin.test.Test


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class ProductControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun getProducts() {
        val jsonFamilyPayload = """
    {
        "name": "Scooby-Doo",
        "description": "Mystery machine"
    }
    """.trimIndent()

        val resultFamily = mockMvc.post("/api/v1/families") {
            contentType = APPLICATION_JSON
            content = jsonFamilyPayload
        }
            .andExpect {
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.id") { exists() }
                jsonPath("$.name") { value("Scooby-Doo") }
                jsonPath("$.description") { value("Mystery machine") }
            }
            .andReturn()

        val familyId = resultFamily.response.contentAsString.split("\"")[3]

        val jsonProductPayload = """
    {
        "name": "Mystery Van",
        "description": "A van used by Scooby and the gang",
        "price": {
            "amount": 9999.99,
            "currency": "USD"
        },
        "family": {
            "id": "$familyId",
            "name": "Scooby-Doo",
            "description": "Mystery machine"
        }
    }
    """.trimIndent()

        mockMvc.post("/api/v1/products") {
            contentType = APPLICATION_JSON
            content = jsonProductPayload
        }
            .andExpect {
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.name") { value("Mystery Van") }
                jsonPath("$.description") { value("A van used by Scooby and the gang") }
                jsonPath("$.price.amount") { value(9999.99) }
                jsonPath("$.price.currency") { value("USD") }
                jsonPath("$.family.id") { value(familyId) }
            }
    }

    @Test
    fun getProductById() {
        val jsonFamilyPayload = """
    {
        "name": "Scooby-Doo",
        "description": "Mystery machine"
    }
    """.trimIndent()

        val resultFamily = mockMvc.post("/api/v1/families") {
            contentType = APPLICATION_JSON
            content = jsonFamilyPayload
        }
            .andExpect {
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.id") { exists() }
                jsonPath("$.name") { value("Scooby-Doo") }
                jsonPath("$.description") { value("Mystery machine") }
            }
            .andReturn()

        val familyId = resultFamily.response.contentAsString.split("\"")[3]

        val jsonProductPayload = """
    {
        "name": "Mystery Van",
        "description": "A van used by Scooby and the gang",
        "price": {
            "amount": 9999.99,
            "currency": "USD"
        },
        "family": {
            "id": "$familyId",
            "name": "Scooby-Doo",
            "description": "Mystery machine"
        }
    }
    """.trimIndent()

        val resultProduct = mockMvc.post("/api/v1/products") {
            contentType = APPLICATION_JSON
            content = jsonProductPayload
        }
            .andExpect {
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.name") { value("Mystery Van") }
                jsonPath("$.description") { value("A van used by Scooby and the gang") }
                jsonPath("$.price.amount") { value(9999.99) }
                jsonPath("$.price.currency") { value("USD") }
                jsonPath("$.family.id") { value(familyId) }
            }
            .andReturn()

        val productId = resultProduct.response.contentAsString.split("\"")[3]

        mockMvc.get("/api/v1/products/$productId")
            .andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$.id") { value(productId) }
                jsonPath("$.name") { value("Mystery Van") }
                jsonPath("$.description") { value("A van used by Scooby and the gang") }
                jsonPath("$.price.amount") { value(9999.99) }
                jsonPath("$.price.currency") { value("USD") }
                jsonPath("$.family.id") { value(familyId) }
            }
    }

}