package com.example.collegeapp.ui.schedule

import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.currentCoroutineContext
import org.jsoup.Jsoup
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import kotlin.coroutines.coroutineContext

/*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.OutputStream
import kotlin.math.roundToInt
*/
class DownloadSchedule {

    private val schedulePageURL = "https://www.mgkit.ru/studentu/raspisanie-zanatij"
    private val base = "https://drive.google.com/uc?export=download&id="
    //private val fileUrl = "https://drive.google.com/file/d/1gnuY-mNkG921Eq2AJNl4fVkYvnu-x5-a/view"

    val downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
    val downloadedPath = downloadPath + "/schedule.pdf"

    fun getScheduleURL(): String? {
        val doc = Jsoup.connect(schedulePageURL).get()

        val listNews = doc.select("div[class=tyJCtd mGzaTb baZpAe] > h2 > span > a:contains(Изменения в расписании учебных занятий)")

        //val scheduleViewUrls = listNews.attr("href").
        val url = listNews.attr("href")
        return url
    }


    /*fun downloadSchedule(url: URL, outputFileName: String) {
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(outputFileName).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }
        }
    }*/
/*
    fun downloadSchedule(): Boolean {
        var isDownloaded: Boolean
        val url = URL(getScheduleURL()?.let { buildDirectUrl(it) })
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(downloadedPath).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                    fos.channel.close()
                    isDownloaded = true
                }
            }
        }
        return isDownloaded
    }

    private fun buildDirectUrl(url: String): String
    {
        val splitted = url.split("/")
        return base + splitted[5]
    }
*/
}