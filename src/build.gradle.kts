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

apply(plugin = "cloudstream")

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
