package iut.nantes.project.gateway.Service

import iut.nantes.project.gateway.Entity.UserEntity
import iut.nantes.project.gateway.Repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UserService(private val userRepository: UserRepository) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun createUser(login: String, password: String, isAdmin: Boolean): UserEntity {
        val encodedPassword = passwordEncoder.encode(password)
        val user = UserEntity(null, login, encodedPassword,  isAdmin)
        return userRepository.save(user)
    }

    fun findByLogin(login: String): UserEntity? {
        return userRepository.findByLogin(login)
    }
}
