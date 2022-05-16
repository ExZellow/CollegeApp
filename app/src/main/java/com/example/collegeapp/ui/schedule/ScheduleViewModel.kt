package com.example.collegeapp.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.Dispatchers

/*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
*/


@Entity
data class ScheduleRoom(

    @ColumnInfo(name="Номер пары")
    val lessonNumber: Int,

    @ColumnInfo(name="Дисциплина")
    val subject: String,

    @ColumnInfo(name="Преподаватель")
    val teacher: String,

    @ColumnInfo(name="Время начала")
    val startTime: String,

    @ColumnInfo(name="Время окончания")
    val endTime: String,

    @ColumnInfo(name="Номер аудитории")
    val audience: String,

    @ColumnInfo(name="Пересдача")
    val specialLesson: Boolean

)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


class ScheduleViewModel : ViewModel() {

    val downloader = DownloadSchedule()
    val parsedURL = ScheduleParser()

    private val _downloading: MutableLiveData<Boolean> = MutableLiveData()
    val downloading: LiveData<Boolean> = _downloading

    fun setDownloading(downloading: Boolean) {
        _downloading.value = downloading
    }

    private val _text: LiveData<String> = liveData(Dispatchers.IO) {
        val data = parsedURL.loadSchedule()//downloader.getScheduleURL() ?: "Hi"
        //emit(data)
    }

    val text: LiveData<String> = _text


}
