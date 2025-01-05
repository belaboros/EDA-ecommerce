defaultTasks("clean", "build", "run")

tasks.register("clean") {
    dependsOn(gradle.includedBuild("product-mgmt").task(":app-product-mgmt:clean"))
    dependsOn(gradle.includedBuild("order-mgmt").task(":app-order-mgmt:clean"))
}

tasks.register("build") {
    dependsOn(gradle.includedBuild("product-mgmt").task(":app-product-mgmt:build"))
    dependsOn(gradle.includedBuild("order-mgmt").task(":app-order-mgmt:build"))
}

tasks.register("run") {
    dependsOn(gradle.includedBuild("product-mgmt").task(":app-product-mgmt:run"))
    dependsOn(gradle.includedBuild("order-mgmt").task(":app-order-mgmt:run"))
}

tasks.register("checkAll") {
    dependsOn(gradle.includedBuild("product-mgmt").task(":app-product-mgmt:check"))
    dependsOn(gradle.includedBuild("order-mgmt").task(":app-order-mgmt:check"))
}

