package iut.nantes.project.products
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun familleJpa(): FamilleJpa {
        return familleJpa()
    }

    @Bean
    fun databaseProxy(familleJpa: FamilleJpa): DatabaseProxy {
        return DatabaseProxy(familleJpa) // Crée une instance de DatabaseProxy avec FamilleJpa injecté
    }
}