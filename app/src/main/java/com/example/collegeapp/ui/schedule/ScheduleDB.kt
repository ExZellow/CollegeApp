package com.example.collegeapp.ui.schedule

import androidx.room.*

class ScheduleDB {
    @Entity
    data class ScheduleRoom(

        @ColumnInfo(name="Группа")
        val group: String,

        @ColumnInfo(name="Занятия")
        val lessons: List<Lesson>

    )
    {
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
    }

    @Entity
    data class Lesson(

        val lessonNumber: Int,

        val subject: String,

        val teacher: String,

        val classroom: String,

        )
    {
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
    }




    @Dao
    interface ScheduleDao {
        @Query("SELECT * FROM ScheduleRoom")
        fun getData(): List<ScheduleRoom>

        @Insert
        fun insertData(vararg group_schedule: ScheduleRoom)

        @Query("DELETE FROM ScheduleRoom")
        fun deleteData()
    }

    @Database(entities = [ScheduleRoom::class, Lesson::class], version = 1)
    @TypeConverters(Converters::class)
    abstract class DB: RoomDatabase() {
        abstract fun getDao(): ScheduleDao
    }


}