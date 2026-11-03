plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "fr.flux"
version = "0.1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

javafx {
    version = "21.0.5"
    modules("javafx.controls")
}

application {
    mainClass = "fr.flux.App"
}