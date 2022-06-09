package com.example.collegeapp.ui.schedule

//import com.pdftron.*
import com.beust.klaxon.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.StringReader
import java.text.DecimalFormat
import java.util.*

class TextExtract {

    val jsonSchedule =
        """
        {
            "group": "ПКС-405",
            "lessons": [
                {
                    "lesson_number" : "1"
                    "subject": "Разработка мобильных приложений",
                    "teacher": "Глускер А.И.",
                    "classroom": "103",
                },
                {
                    "lesson_number" : "2"
                    "subject": "Разработка мобильных приложений",
                    "teacher": "Глускер А.И.",
                    "classroom": "29",
                },
                {
                    "lesson_number" : "3"
                    "subject": "Английский язык",
                    "teacher": "Короткова В.П.",
                    "classroom": "46",
                }
            ]
        }
        """
/*
    fun readJson(objectString: String) {
        JsonReader(StringReader(objectString)).use { reader ->
            reader.beginObject {
                var group: String? = null
                val lessons: List<Retrofit.Lesson>? = null
                while (reader.hasNext()) {
                    val readName = reader.nextName()
                    when (readName) {
                        "group" -> group = reader.nextString()
                        "lesson_number" -> lessons?.get(0)?.lessonNumber = reader.nextInt()
                        "subject" -> lessons?.get(0)?.subject = reader.nextString()
                        "teacher" -> lessons?.get(0)?.teacher = reader.nextString()
                        "classroom" -> lessons?.get(0)?.classroom = reader.nextString()
                        else -> println("Unexpected name: $readName")//Assert.fail("Unexpected name: $readName")
                    }
                }
            }
        }
    }

    val res = readJson(jsonSchedule)
*/
/*
    fun getJson(): Schedule? {
        return Klaxon().parse<Schedule>(jsonSchedule)
    }

    private fun parse(name: String) : Any? {
        val cls = Parser::class.java
        return cls.getResourceAsStream(name)?.let { inputStream ->
            return Parser.default().parse(inputStream)
        }
    }

    private val array = parse(jsonSchedule) as JsonArray<JsonObject>

    //val result = array.forEach { println(it) }

    /*
    class Response(json: String) : JSONObject(json) {
        val type: String? = this.optString("type")
        val data = this.optJSONArray("data")
            ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject
            ?.map { Foo(it.toString()) } // transforms each JSONObject of the array into Foo


    }

    class Foo(json: String) : JSONObject(json) {
        val id = this.optInt("id")
        val title: String? = this.optString("title")
    }

    val result = Response(jsonSchedule)*/

*/

}