package com.example.collegeapp.ui.schedule

import retrofit2.Call
import retrofit2.http.GET

typealias LessonsJson = Map<Int, String>

class Retrofit {

    data class ScheduleJson (
        var scheduleJson: Map<String,  LessonsJson> = emptyMap()
    )

    class Lesson {

        var lessonNumber: Int? = 1

        var subject: String = ""

        var teacher: String = ""

        var classroom: String = ""
    }

    class Schedule {

        var group: String = ""

        var lessons: List<Lesson> = mutableListOf()

    }

    class ScheduleResponse {

        val teacherPattern = Regex("""\s*\.*\s*[A-ZА-ЯЁ][a-zа-яё]+\s+[A-ZА-ЯЁ][.]\s*[A-ZА-ЯЁ][.]\s*\.*\s*""")
        val groupPattern = Regex("""\s*\.*\s*([А-Я]{1,3}-\d{3}(,\s*\d{3})*\s*$)|([А-Я]-\d{6}-\d[а-я]-\d{2}\/\d)\s*\.*\s*""")
        val classroomPattern = Regex("""\s*\.*\s*(\d{1,3}[а-я])|(дист)\s*\.*\s*""")

        fun getSubject(amalgam: String): String {
            var result: String
            amalgam.apply {
                val teachers_matches = teacherPattern.findAll(amalgam)
                val teachers = teachers_matches.map { x -> x.value }.joinToString()
                result = amalgam.replace(teachers, "")
            }
            return result
        }

        var scheduleList: ScheduleJson? = null
        var lessons: LessonsJson? = null

        var db: ScheduleDB.ScheduleRoom? = null

        fun getSchedule(): ArrayList<ScheduleJson> {
            val res: ArrayList<ScheduleJson> = ArrayList(scheduleList!!.scheduleJson.size)
            for (key in scheduleList!!.scheduleJson.keys) {
                for (lkey in lessons!!.keys) {
                    val schedule = Schedule()
                    val lesson = Lesson()
                    val scheduleJS = ScheduleJson()
                    schedule.group = scheduleList!!.scheduleJson[key].toString()
                    lesson.lessonNumber = lkey
                    res.add(scheduleJS)
                }
            }
            return res
        }
    }

    interface ParserResponse {

        @GET(".")
        fun getScheduleJson(): Call<ScheduleResponse>
    }



}
