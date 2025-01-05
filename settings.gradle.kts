/*
 * Composit gradle build
 * to hold the microservices of this hypothetical ecommerce event-driven system.
 *
 * See also
 *  - https://docs.gradle.org/current/userguide/composite_builds.html
 *  - https://docs.gradle.org/current/samples/sample_composite_builds_basics.html
 */
rootProject.name = "EDA-ecommerce"

includeBuild("product-mgmt")
includeBuild("order-mgmt")
