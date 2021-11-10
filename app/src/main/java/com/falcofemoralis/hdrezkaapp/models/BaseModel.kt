package com.falcofemoralis.hdrezkaapp.models

import com.falcofemoralis.hdrezkaapp.objects.SettingsData
import org.jsoup.Connection
import org.jsoup.Jsoup
import javax.net.ssl.HttpsURLConnection

open class BaseModel {
    companion object{
        const val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36"
    }

    fun getJsoup(link: String?): Connection {
        return Jsoup.connect(link?.replace(" ", "")?.replace("\n", ""))
            .userAgent(userAgent)
            .ignoreContentType(true)
            //.ignoreHttpErrors(true)
            //.followRedirects(false)
            .timeout(30000)
            .header("Host", SettingsData.provider!!.replace("http://", "").replace("https://", "").replace(" ", "").replace("\n", ""))
            .header("Connection", "keep-alive")
//              .header("Content-Length", ""+c.request().requestBody().length())
            .header("Cache-Control", "no-cache")
            .header("Origin", SettingsData.provider)
            .header("Upgrade-Insecure-Requests", "1")
 //             .header("User-Agent", userAgent)
            .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
            .header("Accept-Encoding", "gzip, deflate")
            .header("Accept-Language", "ru-UA,ru;q=0.9,en-US;q=0.8,en;q=0.7,uk-UA;q=0.6,uk;q=0.5,ml-IN;q=0.4,ml;q=0.3,ru-RU;q=0.2")
    }
}