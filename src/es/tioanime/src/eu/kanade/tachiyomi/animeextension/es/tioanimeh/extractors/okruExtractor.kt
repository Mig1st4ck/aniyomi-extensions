package eu.kanade.tachiyomi.animeextension.es.tioanimeh.extractors

import eu.kanade.tachiyomi.animesource.model.Video
import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.util.asJsoup
import okhttp3.OkHttpClient
class okruExtractor(private val client: OkHttpClient) {
    fun videosFromUrl(url: String, qualityPrefix: String = ""): List<Video> {
        val document = client.newCall(GET(url)).execute().asJsoup()
        val videoList = mutableListOf<Video>()
        val videosString = document.select("div[data-options]").attr("data-options")
            .substringAfter("\\\"videos\\\":[{\\\"name\\\":\\\"")
            .substringBefore("]")
        videosString.split("{\\\"name\\\":\\\"").reversed().forEach {
            val videoUrl = it.substringAfter("url\\\":\\\"")
                .substringBefore("\\\"")
                .replace("\\\\u0026", "&")
            val videoQuality = qualityPrefix + "Okru: " + it.substringBefore("\\\"")
            if (videoUrl.startsWith("https://")) {
                videoList.add(Video(videoUrl, videoQuality, videoUrl, null))
            }
        }
        return videoList
    }
}