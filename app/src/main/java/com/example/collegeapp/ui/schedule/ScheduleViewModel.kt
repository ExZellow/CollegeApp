package com.example.collegeapp.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeapp.R
import kotlinx.coroutines.Dispatchers





class ScheduleJsonAdapter(private val scheduleJsonList: List<Schedule>): RecyclerView.Adapter<ScheduleJsonAdapter.ScheduleJsonViewHolder>() {

    fun getResult() = scheduleJsonList

    class ScheduleJsonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun fillViews(group: String, lessons: List<Lesson>) {
            for (i in 0..lessons.size) { //TODO:
                itemView.findViewById<TextView>(R.id.text_group).text = group
                itemView.findViewById<TextView>(R.id.text_subject).text = lessons[0].toString()
                itemView.findViewById<TextView>(R.id.text_teacher).text = lessons[0].toString()
                itemView.findViewById<TextView>(R.id.text_classroom).text = lessons[0].toString()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleJsonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.specific_schedule, parent, false)
        return ScheduleJsonViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ScheduleJsonViewHolder,
        position: Int
    ) {
        val scheduleJsonValue = scheduleJsonList[position]
        holder.fillViews(scheduleJsonValue.group, scheduleJsonValue.lessons) //TODO:
    }

    override fun getItemCount(): Int = scheduleJsonList.size

}


class ScheduleViewModel : ViewModel() {

    val downloader = DownloadSchedule()
    //val parsedURL = ScheduleParser()

    private val _downloading: MutableLiveData<Boolean> = MutableLiveData()
    val downloading: LiveData<Boolean> = _downloading

    fun setDownloading(downloading: Boolean) {
        _downloading.value = downloading
    }

    var a = MutableLiveData<List<String>>()


    /*fun getResult() {
        viewModelScope.launch {
            val textExtract = TextExtract()
            val result = textExtract.getJson()
            a.value = listOf(ScheduleJson(result?.lessons?.lessonNumber,
                                          result?.group,
                                          listOf(listOf(result?.lessons?.subject!!,
                                                        result.lessons.teacher,
                                                        result.lessons.classroom
                                                       )
                                                )
                                          )


            )
                /*Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("") //TODO: Insert server url
                    .build()
                    .create(ParserResponse::class.java).getScheduleJson()
            )*/
        }
    }*/




    private val _text: LiveData<String> = liveData(Dispatchers.IO) {
        //val data = parsedURL.loadSchedule()//downloader.getScheduleURL() ?: "Hi"
        //emit(data)
        //val result = TextExtract().getJson()?.group!!
        //emit(result)
    }

    val text: LiveData<String> = _text


}
