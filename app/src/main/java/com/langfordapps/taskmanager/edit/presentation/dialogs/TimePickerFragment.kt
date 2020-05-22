package com.langfordapps.taskmanager.edit.presentation.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.langfordapps.taskmanager.commons.domain.model.DateType
import java.util.Calendar

class TimePickerFragment : DialogFragment() {

    private lateinit var callback: OnDatePickerResultListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = if (context is OnDatePickerResultListener) {
            context
        } else {
            throw ClassCastException(
                context.toString()
                        + " must implement OnDatePickerResultListener"
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val tag = arguments?.getSerializable(KEY_DATE_TYPE) as DateType
        val initialDate = arguments?.getSerializable(KEY_INITIAL_DATE) as Calendar

        return TimePickerDialog(
            activity!!,
            { _, hourOfDay, minute ->
                val newDate = Calendar.getInstance().apply { timeInMillis = initialDate.timeInMillis }
                newDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                newDate.set(Calendar.MINUTE, minute)
                callback.onDateSet(tag, newDate)
            },
            initialDate[Calendar.HOUR_OF_DAY],
            initialDate[Calendar.MINUTE],
            true
        )
    }

    companion object {
        private const val KEY_DATE_TYPE = "KEY_DATE_TYPE"
        private const val KEY_INITIAL_DATE = "KEY_INITIAL_DATE"

        fun newInstance(dateType: DateType, initialDialogDate: Calendar): TimePickerFragment {
            return TimePickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_DATE_TYPE, dateType)
                    putSerializable(KEY_INITIAL_DATE, initialDialogDate)
                }
            }
        }
    }

}