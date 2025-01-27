package iut.nantes.project.stores.Controller

import iut.nantes.project.stores.DTO.ContactDTO
import iut.nantes.project.stores.Entity.ContactEntity
import iut.nantes.project.stores.Exception.ContactException
import iut.nantes.project.stores.Service.ContactService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/contacts")
class ContactController(private val contactService: ContactService) {

    @PostMapping
    fun createContact(@RequestBody contact: ContactDTO): ContactDTO {
        return contactService.createContact(contact)
    }

    @GetMapping
    fun getContactsByCity(@RequestParam city: String): List<ContactDTO> {
        return contactService.getContactsByCity(city)
    }


    @GetMapping("/{id}")
    fun getContactById(@PathVariable id: Int): ContactDTO? {
        return contactService.getContactById(id)
    }

    @PutMapping("/{id}")
    fun updateContact(@PathVariable id: Int, @RequestBody contactEntity: ContactDTO): ContactDTO? {
        return contactService.updateContact(id, contactEntity)
    }

    @DeleteMapping("/{id}")
    fun deleteContact(@PathVariable id: Int) {
        return contactService.deleteContact(id)

    }
}
