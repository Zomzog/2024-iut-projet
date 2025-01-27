package iut.nantes.project.stores.Repository

import iut.nantes.project.stores.Entity.ContactEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ContactRepository : JpaRepository<ContactEntity, Long>
