package iut.nantes.project.gateway.Repository

import iut.nantes.project.gateway.Entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByLogin(login: String): Optional<UserEntity>
}
