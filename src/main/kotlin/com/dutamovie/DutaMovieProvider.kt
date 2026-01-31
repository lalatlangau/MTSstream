package com.dutamovie

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.mvvm.safeApiCall
import org.jsoup.nodes.Document

class DutaMovieProvider : MainAPI() {
    override var mainUrl = "https://hidekielectronics.com"
    override var name = "Dutamovie21 - Situs Nonton Film Bioskop LK21 CGVINDO Layarkaca21 Cepat, Mudah, Dan Lengkap"
    override val hasMainPage = true
    override var lang = "id"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.AsianDrama
    )

    override val mainPage = mainPageOf(
        "faq-frequently-asked-question" to "FAQ",
        "" to "Home",
        "movie" to "Movie",
        "serial-tv" to "Serial TV",
        "kelasbintang" to "Kelas Bintang",
        "javdutamovie21" to "JAV HD",
        "semi" to "Semi",
        "bokepindopc" to "Semi Indo",
        "semijav" to "Semi Jepang",
        "vivamax" to "Vivamax",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data}?page=$page").document
        val home = document.select("article, .item, .movie-item, .post").mapNotNull {
            it.toSearchResult()
        }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst("h2, h3, .title, .entry-title")?.text()?.trim() ?: return null
        val href = fixUrl(this.selectFirst("a")?.attr("href") ?: return null)
        val posterUrl = fixUrlNull(this.selectFirst("img")?.attr("src"))
        
        return newMovieSearchResponse(title, href, TvType.Movie) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("article, .item, .search-item").mapNotNull {
            it.toSearchResult()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document
        val title = document.selectFirst("h1, .entry-title")?.text()?.trim() ?: return null
        val poster = document.selectFirst("meta[property=og:image]")?.attr("content")
        val description = document.selectFirst(".synopsis, .description, [itemprop=description]")?.text()?.trim()
        val year = document.selectFirst(".year")?.text()?.toIntOrNull()
        val tags = document.select(".genre a, .category a").map { it.text() }
        
        val recommendations = document.select(".related article, .related .item").mapNotNull {
            it.toSearchResult()
        }

        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster
            this.year = year
            this.plot = description
            this.tags = tags
            this.recommendations = recommendations
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        
        // Extract iframe sources
        document.select("iframe").forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotEmpty()) {
                loadExtractor(src, data, subtitleCallback, callback)
            }
        }
        
        // Extract direct video sources
        document.select("video source, [data-src]").forEach { source ->
            val videoUrl = source.attr("src").ifEmpty { source.attr("data-src") }
            if (videoUrl.isNotEmpty()) {
                callback.invoke(
                    ExtractorLink(
                        this.name,
                        this.name,
                        videoUrl,
                        referer = data,
                        quality = Qualities.Unknown.value,
                    )
                )
            }
        }
        
        return true
    }
}
