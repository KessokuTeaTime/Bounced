pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven { url = uri("https://maven.firstdark.dev/releases") } // modpublisher
        mavenCentral()
        gradlePluginPortal()
    }
}
