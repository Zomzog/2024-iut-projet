package iut.nantes.project.gateway.Entity

data class UserDTO(
    val login: String,
    val password: String,
    val isAdmin: Boolean
)