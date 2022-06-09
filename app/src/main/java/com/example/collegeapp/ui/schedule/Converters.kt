package com.example.collegeapp.ui.schedule

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type


class Converters {

    @TypeConverter
    fun fromLesson(lessons: List<ScheduleDB.Lesson>): String {
        return Gson().toJson(lessons)
    }

    @TypeConverter
    fun toLessons(lessons: String): List<ScheduleDB.Lesson> {
        return Gson().fromJson(lessons, object : TypeToken<List<ScheduleDB.Lesson>>() {}.type)
    }

}

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: retrofit2.Retrofit
    ): Converter<ResponseBody, *> {
        val delegate: Converter<ResponseBody, *> =
            retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter { body -> if (body.contentLength() == 0L) null else delegate.convert(body) }
    }
}