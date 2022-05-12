package com.example.collegeapp.ui.schedule

import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
/*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
*/
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.roundToInt


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
    val pdfFile = File("shit").canRead()

    private val _downloading: MutableLiveData<Boolean> = MutableLiveData()
    val downloading: LiveData<Boolean> = _downloading

    fun setDownloading(downloading: Boolean) {
        _downloading.value = downloading
    }


    /*private val _text = MutableLiveData<String>().apply {
        value = pdfFile.toString()//json.nextString()


        //.readLines().component1()// .read().toString()//.lines().findFirst().toString()//"Фрагмент с расписанием"

    }*/
   // val text: LiveData<String> = _text


}
