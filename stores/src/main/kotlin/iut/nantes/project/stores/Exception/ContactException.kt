package iut.nantes.project.stores.Exception


sealed class ContactException(message: String) : RuntimeException(message) {
    class InvalidDataException(message: String = "Les données fournies sont invalides.") : ContactException(message)
    class ContactNotFoundException(message: String = "Le contact n'a pas été trouvé.") : ContactException(message)
    class ContactIsInAStoreException(message: String = "Le contact ne peut pas être supprimé s'il est dans un store.") : ContactException(message)
    class InvalidIdFormatException(message: String = "Le format de l'ID est invalide.") : ContactException(message)
    class ProductHasStockException(message: String = "Le produit a encore du stock.") : ContactException(message)
}
