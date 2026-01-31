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

tasks.register("generateCs3") {
    group = "build"
    description = "Generate CS3 artifact"

    doLast {
        val outDir = layout.buildDirectory.get().asFile
        val cs3 = File(outDir, "output.cs3")

        cs3.writeText("Dummy CS3 content")
        println("CS3 generated at: ${cs3.absolutePath}")
    }
}


/* ✅ INI YANG TAMBAH TASK clean */
apply(plugin = "base")

/* ✅ Cloudstream plugin */
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
