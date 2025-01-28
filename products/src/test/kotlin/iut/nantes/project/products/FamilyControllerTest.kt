package iut.nantes.project.products


import iut.nantes.project.products.Controller.FamilyController
import iut.nantes.project.products.DTO.FamilyDTO
import iut.nantes.project.products.Service.FamilyService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(FamilyController::class)
class FamilyControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var familyService: FamilyService

    @Test
    fun createFamilyShouldReturnCreatedStatus() {
        val familyDto = FamilyDTO(UUID.randomUUID(), "Test Family", "Description")
        whenever(familyService.createFamily(any())).thenReturn(familyDto)

        mockMvc.perform(post("/api/v1/families")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{"name": "Test Family", "description": "Description"}"""))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Test Family"))
    }

    @Test
    fun getAllFamiliesShouldReturnOkStatus() {
        val familyDto = FamilyDTO(UUID.randomUUID(), "Test Family", "Description")
        whenever(familyService.getAllFamilies()).thenReturn(listOf(familyDto))

        mockMvc.perform(get("/api/v1/families"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Test Family"))
    }

    @Test
    fun getFamilyByIdShouldReturnOkStatus() {
        val id = UUID.randomUUID()
        val familyDto = FamilyDTO(id, "Test Family", "Description")
        whenever(familyService.getFamilyById(id)).thenReturn(familyDto)

        mockMvc.perform(get("/api/v1/families/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Family"))
    }

    @Test
    fun updateFamilyShouldReturnOkStatus() {
        val id = UUID.randomUUID()
        val familyDto = FamilyDTO(id, "Updated Family", "Updated Description")
        whenever(familyService.updateFamily(eq(id), any())).thenReturn(familyDto)

        mockMvc.perform(put("/api/v1/families/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""{"name": "Updated Family", "description": "Updated Description"}"""))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Family"))
    }

    @Test
    fun deleteFamilyShouldReturnNoContentStatus() {
        val id = UUID.randomUUID()
        doNothing().whenever(familyService).deleteFamily(id)

        mockMvc.perform(delete("/api/v1/families/$id"))
            .andExpect(status().isNoContent)
    }
}
