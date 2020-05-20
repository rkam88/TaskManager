package net.rusnet.taskmanager.tasksdisplay.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.tasksdisplay.presentation.model.ViewTask

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
        val backgroundLayout: ConstraintLayout = itemView.findViewById(R.id.task_item_background_layout)
        val foregroundLayout: FrameLayout = itemView.findViewById(R.id.task_item_foreground_layout)

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
        val taskNameTextView = holder.taskNameTextView
        val taskDateTextView = holder.taskDateTextView
        val backgroundLayout = holder.backgroundLayout
        val foregroundLayout = holder.foregroundLayout

        taskNameTextView.text = task.name

        taskDateTextView.visibility = task.dateViewVisibility
        taskDateTextView.text = task.date

        taskDateTextView.setTextColor(taskDateTextView.resources.getColor(task.dateColor, null))

    }

}