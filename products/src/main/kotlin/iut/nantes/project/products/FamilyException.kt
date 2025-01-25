package iut.nantes.project.products


sealed class FamilyException(message: String) : RuntimeException(message) {
    class InvalidDataException(message: String = "Les données fournies sont invalides.") : FamilyException(message)
    class NameConflictException(message: String = "Le nom de la famille est déjà utilisé.") : FamilyException(message)
    class FamilyNotFoundException(message: String = "La famille n'a pas été trouvée.") : FamilyException(message)
    class InvalidIdFormatException(message: String = "Le format de l'ID est invalide.") : FamilyException(message)
    class FamilyHasProductsException(message: String = "La famille contient encore des produits.") : FamilyException(message)
}
