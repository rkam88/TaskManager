package com.langfordapps.taskmanager.tasksdisplay.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.langfordapps.taskmanager.R
import com.langfordapps.taskmanager.tasksdisplay.presentation.model.ViewTask

class TasksAdapter(
    val viewTasksList: MutableList<ViewTask>,
    private val callback: OnItemClickListener
) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(taskId: Long)
        fun onLongClick(taskId: Long)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskNameTextView: TextView = itemView.findViewById(R.id.text_view_task_name)
        val taskDateTextView: TextView = itemView.findViewById(R.id.text_view_task_date)
        val taskItemLayout: FrameLayout = itemView.findViewById(R.id.task_item_layout)
        val alarmDateTextView: TextView = itemView.findViewById(R.id.text_view_alarm_date)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    callback.onClick(viewTasksList[position].taskId)
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    callback.onLongClick(viewTasksList[position].taskId)
                    return@setOnLongClickListener true
                }
                return@setOnLongClickListener false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.tasks_display_task_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun getItemCount() = viewTasksList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = viewTasksList[position]

        holder.taskNameTextView.text = task.name

        holder.taskDateTextView.visibility = task.dateViewVisibility
        holder.taskDateTextView.text = task.date
        holder.taskDateTextView.setTextColor(holder.taskDateTextView.resources.getColor(task.dateColor, null))

        holder.alarmDateTextView.visibility = task.alarmViewVisibility
        holder.alarmDateTextView.text = task.alarmDate
        holder.alarmDateTextView.setTextColor(holder.taskDateTextView.resources.getColor(task.alarmColor, null))

        if (task.isSelectedForDeletion) {
            holder.taskItemLayout.setBackgroundResource(R.color.itemSelectedBackground)
        } else {
            holder.taskItemLayout.setBackgroundResource(R.color.itemDefaultBackground)
        }

    }

    fun getTaskIdOfItem(itemPosition: Int): Long {
        return viewTasksList[itemPosition].taskId
    }

}