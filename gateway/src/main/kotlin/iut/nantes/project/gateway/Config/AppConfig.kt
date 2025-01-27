package iut.nantes.project.gateway.Config

import iut.nantes.project.gateway.Repository.UserRepository
import iut.nantes.project.gateway.Service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun userService(userRepository: UserRepository): UserService {
        return UserService(userRepository)
    }


}
