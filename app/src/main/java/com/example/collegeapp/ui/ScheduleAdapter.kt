package com.example.collegeapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeapp.R
import com.example.collegeapp.ui.schedule.Schedule

class ScheduleAdapter: RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    private var list: List<Schedule> = listOf()

    fun setGroupSchedule(group: String, newList: List<Schedule>) {
        list = newList.filter { it.group.trim().contains(group) }
        notifyDataSetChanged()
    }

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        var group: TextView? = item.findViewById(R.id.group)
        var lesson1: TextView? = item.findViewById(R.id.lesson1)
        var lesson2: TextView? = item.findViewById(R.id.lesson2)
        var lesson3: TextView? = item.findViewById(R.id.lesson3)
        var lesson4: TextView? = item.findViewById(R.id.lesson4)
        var lesson5: TextView? = item.findViewById(R.id.lesson5)
        var lesson6: TextView? = item.findViewById(R.id.lesson6)
        var lesson7: TextView? = item.findViewById(R.id.lesson7)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.group?.text = list[position].group

        holder.apply {
            lesson1?.text =
                holder.itemView.context.getString(
                    R.string.lesson,
                    list[position].lessons[0].lessonNumber,
                    list[position].lessons[0].subject + " " + list[position].lessons[0].teacher + " " + list[position].lessons[0].classroom
                )
            lesson2?.text =
                holder.itemView.context.getString(
                    R.string.lesson,
                    list[position].lessons[1].lessonNumber,
                    list[position].lessons[1].subject + " " + list[position].lessons[1].teacher + " " + list[position].lessons[1].classroom
                )
            lesson3?.text =
                holder.itemView.context.getString(
                    R.string.lesson,
                    list[position].lessons[2].lessonNumber,
                    list[position].lessons[2].subject + " " + list[position].lessons[2].teacher + " " + list[position].lessons[2].classroom
                )
            lesson4?.text =
                holder.itemView.context.getString(
                    R.string.lesson,
                    list[position].lessons[3].lessonNumber,
                    list[position].lessons[3].subject + " " + list[position].lessons[3].teacher + " " + list[position].lessons[3].classroom
                )
            lesson5?.text =
                holder.itemView.context.getString(
                    R.string.lesson,
                    list[position].lessons[4].lessonNumber,
                    list[position].lessons[4].subject + " " + list[position].lessons[4].teacher + " " + list[position].lessons[4].classroom
                )
            lesson6?.text =
                holder.itemView.context.getString(
                    R.string.lesson,
                    list[position].lessons[5].lessonNumber,
                    list[position].lessons[5].subject + " " + list[position].lessons[5].teacher + " " + list[position].lessons[5].classroom
                )
            lesson7?.text =
                holder.itemView.context.getString(
                    R.string.lesson,
                    list[position].lessons[6].lessonNumber,
                    list[position].lessons[6].subject + " " + list[position].lessons[6].teacher + " " + list[position].lessons[6].classroom
                )

        }
    }
    override fun getItemCount() = list.size
}