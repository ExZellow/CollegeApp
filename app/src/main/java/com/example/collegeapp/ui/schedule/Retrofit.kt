package com.example.collegeapp.ui.schedule

import retrofit2.Call
import retrofit2.http.GET

data class Schedule(
    val group: String,
    val lessons: List<Lesson>
)

data class Lesson(
    val lessonNumber: Int,
    //val lessonDescription: String?
    val subject: String?,
    val teacher: List<String?>,
    val classroom: List<String?>
)

class ScheduleMapper {

    val teacherPattern = Regex("""[A-ZА-ЯЁ][a-zа-яё]+\s+[A-ZА-ЯЁ][.]\s*[A-ZА-ЯЁ][.]""")
    val classroomPattern = Regex("""((?<![\.0])\d{1,3}([а-я])?)|(дист)""")
    //val classroomPattern = Regex("""((?<!.)\d{1,3}[а-я]?)|(дист)""")

    fun mapFromGsonResultToSchedule(gsonResult: Map<String, Map<String, String?>>) =
        gsonResult.map { it ->
            Schedule(
                it.key,
                it.value.map {
                    Lesson(
                        it.key.trim().toInt() + 1,
                        parseSubject(it.value),
                        parseTeacher(it.value),
                        parseClassroom(it.value)
                    )
                }
            )
        }

    private fun parseTeacher(lesson: String?): List<String?> {
        val teachersList: MutableList<String> = mutableListOf()
        var localLesson = lesson
        if (localLesson != null) {
            while(localLesson!!.contains(teacherPattern)) {
                teachersList.add(teacherPattern.find(localLesson)?.value.toString().trim())
                localLesson = localLesson.replaceFirst(teacherPattern, " ")
            }
        }
        return teachersList
    }

    private fun parseSubject(lesson: String?): String {
        var subject = ""
        var localLesson = lesson
        if (localLesson != null) {
            while (localLesson!!.contains(teacherPattern) || localLesson.contains(classroomPattern)) {
                localLesson = localLesson.replace(teacherPattern, " ")
                localLesson = localLesson.replace(classroomPattern, " ")
            }
            subject = localLesson.trim()
        }
        return subject
    }

    private fun parseClassroom(lesson: String?): List<String?> {
        val classroomList: MutableList<String> = mutableListOf()
        var localLesson = lesson
        if (localLesson != null) {
            while (localLesson!!.contains(classroomPattern)) {
                classroomList.add(classroomPattern.find(localLesson)?.value.toString().trim())
                localLesson = localLesson.replaceFirst(classroomPattern, " ")
            }
        }
        return classroomList
    }

/*    fun parseLesson(lesson: String?): List<String> {
        var res: MutableList<String> = mutableListOf()
        while(lesson!!.contains(composePattern)) {
            //teachersList.add(composePattern.findAll(lesson).map { it.value }.toList())
            res = composePattern.findAll(lesson).map { it.value.trim()}.toMutableList()
        }
        return res
    }*/
}


/*

typealias LessonsJson = Map<String, String>

class Retrofit {

    data class ScheduleJson (
        var scheduleJson: Map<String,  LessonsJson> = emptyMap()
    )

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

                    val scheduleJS = ScheduleJson()
                    */
/*schedule.group = scheduleList!!.scheduleJson[key].toString()
                    lesson.lessonNumber = lkey*//*

                    res.add(scheduleJS)
                }
            }
            return res
        }
    }
*/
interface ParserResponse {

    @GET("/")
    fun getScheduleJson(): Call<String>
}
