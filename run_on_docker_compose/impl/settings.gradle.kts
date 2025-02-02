/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 * For more detailed information on multi-project builds, please refer to https://docs.gradle.org/8.11.1/userguide/multi_project_builds.html in the Gradle documentation.
 *
 * See also
 *   - https://docs.gradle.org/current/userguide/composite_builds.html
 */

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "EDA-ecommerce"
include("product-mgmt")
include("order-mgmt")

