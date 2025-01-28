package iut.nantes.project.stores

import iut.nantes.project.stores.Controller.ContactController
import iut.nantes.project.stores.DTO.ContactDTO
import iut.nantes.project.stores.DTO.AddressDTO
import iut.nantes.project.stores.Repository.StoreRepository
import iut.nantes.project.stores.Service.ContactService
import org.mockito.Mockito.doNothing
import org.mockito.kotlin.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {
  @Autowired
  private lateinit var mockMvc: MockMvc
  @MockBean
  private lateinit var contactService: ContactService

  @Test
  fun createContact() {
   val contactDto = ContactDTO(1, "test.contact@gmail.com", "0123456789", AddressDTO("7 rue du test", "Testing", "44200"))
   whenever(contactService.createContact(any())).thenReturn(contactDto)

   mockMvc.perform(
    post("/api/v1/contacts")
     .contentType(MediaType.APPLICATION_JSON)
     .content("""{"id": 1, "email": "test.contact@gmail.com", "phone": "0123456789", "address": {"street" : "7 rue du test", "city":"Testing", "postalCode": "44200"}}"""))
    .andExpect(status().isCreated)
  }

@Test
 fun getContactsByCity() {}

@Test
 fun getContactById() {}

@Test
 fun updateContact() {}

@Test
 fun deleteContact() {}
}