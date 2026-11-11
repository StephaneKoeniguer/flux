plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "fr.flux"
version = "0.1.0"

repositories {
    mavenCentral()
    maven("https://dl.frostwire.com/maven") {
        content {
            includeGroup("com.frostwire")
        }
    }
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

val jlibtorrentVersion = "2.0.15.3"

dependencies {
    implementation("io.github.mkpaz:atlantafx-base:2.0.1")
    implementation("com.frostwire:jlibtorrent:$jlibtorrentVersion")
    implementation("com.frostwire:jlibtorrent-macosx-arm64:$jlibtorrentVersion")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.4.0")
    implementation("org.kordamp.ikonli:ikonli-feather-pack:12.4.0")
}