import iut.nantes.project.gateway.Service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(private val userService: UserService) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeRequests()
            .requestMatchers("/api/v1/user").permitAll() // Permet la création d'un utilisateur
            .requestMatchers("/api/v1/products/**", "/api/v1/stores/**").hasRole("ADMIN") // Accès aux API uniquement pour ADMIN
            .anyRequest().authenticated()
            .and()
            .formLogin {

                it.loginPage("/login").permitAll()
            }
            .httpBasic {}
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            userService.findByLogin(username)?.let {
                User.builder()
                    .username(it.login)
                    .password(it.password)
                    .roles(if (it.isAdmin) "ADMIN" else "USER")
                    .build()
            } ?: throw UsernameNotFoundException("User not found")
        }
    }
}
