rootProject.name = "project"

include("gateway")
include("products")
include("stores")

include(":gateway", ":products")
include(":stores", ":products")
