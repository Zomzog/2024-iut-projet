package iut.nantes.project.stores.Exception

sealed class StoreException(message: String) : RuntimeException(message) {
    class InvalidDataException(message: String = "Les données fournies sont invalides.") : StoreException(message)
    class StoreNotFoundException(message: String = "Le store n'a pas été trouvé.") : StoreException(message)
    class InvalidIdFormatException(message: String = "Le format de l'ID est invalide.") : StoreException(message)
    class StoreHasProductsException(message: String = "Le magasin a encore du stock.") : StoreException(message)
}
