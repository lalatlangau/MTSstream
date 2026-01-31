buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.github.recloudstream:gradle:4.4.0")
    }
}


plugins {
    base       // beri task clean
    java       // beri build, jar, test
}

group = "com.mtsstream"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}


repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
}

cloudstream {
    language = "id"
    authors = listOf("YourName")

    tvTypes = listOf(
        "Movie",
        "TvSeries"
    )

    iconUrl = "https://hidekielectronics.com/favicon.ico"
}
