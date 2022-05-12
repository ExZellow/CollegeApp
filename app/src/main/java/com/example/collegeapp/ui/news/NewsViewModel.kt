package com.example.collegeapp.ui.news

import androidx.lifecycle.*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.*

@Entity
data class NewsRoom(

    @ColumnInfo(name="Заголовок")
    val title: String,

    @ColumnInfo(name="Содержание")
    val content: String,

    @ColumnInfo(name="Путь к картинке")
    val imagePath: String

)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

class NewsViewModel : ViewModel() {

    private val newsParser = NewsParser()

    private val _text: LiveData<String> = liveData(Dispatchers.IO) {
        val data = newsParser.getResult()
        emit(data)
    }

    val text: LiveData<String> = _text

}