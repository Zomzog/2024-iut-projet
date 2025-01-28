package iut.nantes.project.gateway.Service

import iut.nantes.project.gateway.Entity.UserDTO
import iut.nantes.project.gateway.Entity.UserEntity
import iut.nantes.project.gateway.Repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class UserService(private val userRepository: UserRepository) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun createUser(user : UserDTO): UserEntity {
        val encodedPassword = passwordEncoder.encode(user.password)
        val user = UserEntity(null, user.login, encodedPassword,  user.isAdmin)
        return userRepository.save(user)
    }

    fun findByLogin(login: String): Optional<UserEntity> {
        return userRepository.findByLogin(login)
    }
}
