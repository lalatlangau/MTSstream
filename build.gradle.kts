plugins {
    kotlin("jvm") version "1.9.24"
    id("com.github.recloudstream") version "4.4.0"
}

repositories {
    mavenCentral()
    google()
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
