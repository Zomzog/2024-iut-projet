package iut.nantes.project.products.controller

import jakarta.transaction.Transactional
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.*


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class FamilyControllerTest {


    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun getFamily1() {
        val jsonPayload = """
        {
            "name": "Test Family",
            "description": "Family description"
        }
        """.trimIndent()

        mockMvc.post("/api/v1/families") {
            contentType = APPLICATION_JSON
            content = jsonPayload
        }


        mockMvc.get("/api/v1/families")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$") { isArray() }
                jsonPath("$[0].name") { value("Test Family") }
                jsonPath("$[0].description") { value("Family description") }
            }
    }

    @Test
    fun getFamily2() {
        var jsonPayload = """
        {
            "name": "Test Family",
            "description": "Family description"
        }
        """.trimIndent()

        mockMvc.post("/api/v1/families") {
            contentType = APPLICATION_JSON
            content = jsonPayload
        }


        jsonPayload = """
        {
            "name": "Scooby-Doo",
            "description": "Mystery machine"
        }
        """.trimIndent()

        mockMvc.post("/api/v1/families") {
            contentType = APPLICATION_JSON
            content = jsonPayload
        }

        mockMvc.get("/api/v1/families")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$") { isArray() }
                jsonPath("$[0].name") { value("Test Family") }
                jsonPath("$[0].description") { value("Family description") }
                jsonPath("$[1].name") { value("Scooby-Doo") }
                jsonPath("$[1].description") { value("Mystery machine") }
            }
    }

    @Test
    fun getFamily1ById() {
        val jsonPayload = """
        {
            "name": "Scooby-Doo",
            "description": "Mystery machine"
        }
        """.trimIndent()

        val result = mockMvc.post("/api/v1/families") {
            contentType = APPLICATION_JSON
            content = jsonPayload
        }
            .andExpect {
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.id") { exists() }
                jsonPath("$.name") { value("Scooby-Doo") }
                jsonPath("$.description") { value("Mystery machine") }
            }
            .andReturn()

        val familyId = result.response.contentAsString.split("\"")[3]

        mockMvc.get("/api/v1/families/$familyId")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.name") { value("Scooby-Doo") }
                jsonPath("$.description") { value("Mystery machine") }
            }
    }

    @Test
    fun putFamily() {
        val jsonPayload = """
    {
        "name": "Scooby-Doo",
        "description": "Mystery machine"
    }
    """.trimIndent()

        val result = mockMvc.post("/api/v1/families") {
            contentType = APPLICATION_JSON
            content = jsonPayload
        }
            .andExpect {
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.id") { exists() }
                jsonPath("$.name") { value("Scooby-Doo") }
                jsonPath("$.description") { value("Mystery machine") }
            }
            .andReturn()

        val familyId = result.response.contentAsString.split("\"")[3]

        val updatedJsonPayload = """
    {
        "name": "Scooby-Doo Updated",
        "description": "Updated Mystery machine"
    }
    """.trimIndent()

        mockMvc.put("/api/v1/families/$familyId") {
            contentType = APPLICATION_JSON
            content = updatedJsonPayload
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.name") { value("Scooby-Doo Updated") }
                jsonPath("$.description") { value("Updated Mystery machine") }
            }
    }

    @Test
    fun deleteFamily() {
        val jsonPayload = """
    {
        "name": "Scooby-Doo",
        "description": "Mystery machine"
    }
    """.trimIndent()

        val result = mockMvc.post("/api/v1/families") {
            contentType = APPLICATION_JSON
            content = jsonPayload
        }
            .andExpect {
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.id") { exists() }
                jsonPath("$.name") { value("Scooby-Doo") }
                jsonPath("$.description") { value("Mystery machine") }
            }
            .andReturn()

        val familyId = result.response.contentAsString.split("\"")[3]

        mockMvc.delete("/api/v1/families/$familyId")
            .andExpect {
                status { isNoContent() }
                content { string("La famille a été supprimée avec succès") }
            }

        mockMvc.get("/api/v1/families/$familyId")
            .andExpect {
                status { isNotFound() }
            }
    }

}

