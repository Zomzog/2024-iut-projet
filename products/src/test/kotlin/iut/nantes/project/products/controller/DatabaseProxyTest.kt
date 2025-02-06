import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Validator
import kotlin.test.assertEquals
import kotlin.test.assertNull


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Transactional
class DatabaseProxyTest {


    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var databaseProxy: DatabaseProxy

    private lateinit var familleRepo: FamilleRepository
    private lateinit var productRepo: ProductRepository
    private lateinit var familleDto: FamilleDto

    @BeforeEach
    fun setup() {
        // Initialisation des mocks
        familleRepo = mock(FamilleRepository::class.java)
        productRepo = mock(ProductRepository::class.java)

        // Initialisation de l'objet DatabaseProxy
        databaseProxy = DatabaseProxy(familleRepo, productRepo)

        // Initialisation d'un exemple de FamilleDto
        familleDto = FamilleDto(
            name = "FamilleTest",
            description = "Description de test"
        )
    }

    @Test
    fun `should save famille successfully`() {
        // Mocking la méthode findByName pour qu'elle renvoie null (aucune famille avec ce nom)
        `when`(familleRepo.findByName(familleDto.name)).thenReturn(null)

        // Mocking la méthode save pour qu'elle renvoie une FamilleEntity validée
        val familleEntity = FamilleEntity(name = familleDto.name, description = familleDto.description)
        `when`(familleRepo.save(familleEntity)).thenReturn(familleEntity)

        // Appel de la méthode saveFamille
        val result = databaseProxy.saveFamille(familleDto)

        // Vérification que la famille a été sauvegardée et retournée sous forme de DTO
        assertEquals(familleDto.name, result?.name)
        assertEquals(familleDto.description, result?.description)
    }

    @Test
    fun `should return null when famille name already exists`() {
        // Mocking la méthode findByName pour qu'elle renvoie une famille existante
        `when`(familleRepo.findByName(familleDto.name)).thenReturn(FamilleEntity(name = familleDto.name, description = "Existing famille"))

        // Appel de la méthode saveFamille
        val result = databaseProxy.saveFamille(familleDto)

        // Vérification que null est retourné si le nom existe déjà
        assertNull(result)
    }

    @Test
    fun `should return null when validation fails`() {
        // Simuler une validation échouée (on peut ajouter des erreurs de validation si nécessaire)
        val invalidFamilleDto = FamilleDto(id = "", name = "", description = "")

        // Appel de la méthode saveFamille avec un DTO invalide
        val result = databaseProxy.saveFamille(invalidFamilleDto)

        // Vérification que null est retourné si la validation échoue
        assertNull(result)
    }
}
