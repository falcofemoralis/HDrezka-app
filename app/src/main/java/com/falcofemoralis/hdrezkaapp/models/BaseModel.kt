package com.falcofemoralis.hdrezkaapp.models

import com.falcofemoralis.hdrezkaapp.BuildConfig
import com.falcofemoralis.hdrezkaapp.objects.SettingsData
import com.google.android.exoplayer2.metadata.icy.IcyHeaders
import org.jsoup.Connection
import org.jsoup.Jsoup

object BaseModel {
    fun getJsoup(link: String?): Connection {
        val connection = Jsoup.connect(link?.replace(" ", "")?.replace("\n", ""))
            .userAgent(SettingsData.mobileUserAgent)
            .ignoreContentType(true)
            //.ignoreHttpErrors(true)
            //.followRedirects(false)
            .timeout(30000)
            .header(SettingsData.APP_HEADER, IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE)
            .header(SettingsData.APP_VERSION, BuildConfig.VERSION_NAME)

        return connection
    }

}