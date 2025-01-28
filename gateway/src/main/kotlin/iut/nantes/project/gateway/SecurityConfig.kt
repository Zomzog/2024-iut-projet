import iut.nantes.project.gateway.Repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
class SecurityConfig(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val env: org.springframework.core.env.Environment
) {

    // Bean PasswordEncoder
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    // Configuration de l'authentification
    @Autowired
    fun configureAuthentication(auth: AuthenticationManagerBuilder) {
        val securityMode = env.getProperty("gateway.security") ?: "database"
        if (securityMode == "inmemory") {
            auth.inMemoryAuthentication()
                .withUser("ADMIN")
                .password(passwordEncoder().encode("ADMIN"))
                .roles("ADMIN")
        } else {
            auth.userDetailsService(userDetailsService())
        }
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { login: String ->
            val user = userRepository.findByLogin(login).orElseThrow {
                UsernameNotFoundException("User not found: $login")
            }
            org.springframework.security.core.userdetails.User(
                user.login,
                user.password,
                listOf(
                    if (user.isAdmin) SimpleGrantedAuthority("ROLE_ADMIN")
                    else SimpleGrantedAuthority("ROLE_USER")
                )
            )
        }
    }

    // Configuration de la sécurité HTTP
    @Throws(Exception::class)
    fun configureHttpSecurity(http: HttpSecurity) {
        http
            .authorizeRequests()
            .requestMatchers("/api/v1/user").permitAll()
            .requestMatchers("/**").hasRole("ADMIN")

    }
}
