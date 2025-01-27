package iut.nantes.project.stores.Service

import iut.nantes.project.stores.DTO.ContactDTO
import iut.nantes.project.stores.Entity.ContactEntity
import iut.nantes.project.stores.Exception.ContactException
import iut.nantes.project.stores.Repository.ContactRepository
import org.springframework.stereotype.Service

@Service
class ContactService(private val contactRepository: ContactRepository) {

    fun createContact(contact: ContactDTO): ContactDTO {
        val contactEntity = ContactEntity(
            email = contact.email,
            phone = contact.phone,
            address = contact.address.toEntity()
        )
        val savedContact = contactRepository.save(contactEntity)
        return savedContact.toDto()
    }

    fun getContactsByCity(city: String): List<ContactDTO> {
        val contacts = contactRepository.findByAddressCity(city)
        return contacts.map { it.toDto() }
    }

    fun getContactById(id: Int): ContactDTO {
        val contact = contactRepository.findById(id)
            .orElseThrow { ContactException.ContactNotFoundException("Contact not found with id: $id") }
        return contact.toDto()
    }

    fun updateContact(id: Int, contactDTO: ContactDTO): ContactDTO {
        val existingContact = contactRepository.findById(id)
            .orElseThrow { ContactException.ContactNotFoundException("Contact not found with id: $id") }

        existingContact.email = contactDTO.email
        existingContact.phone = contactDTO.phone
        existingContact.address = contactDTO.address.toEntity()

        val updatedContact = contactRepository.save(existingContact)
        return updatedContact.toDto()
    }

    fun deleteContact(id: Int) {
        val contact = contactRepository.findById(id)
            .orElseThrow { ContactException.ContactNotFoundException("Contact not found with id: $id") }

        contactRepository.delete(contact)
    }
}
