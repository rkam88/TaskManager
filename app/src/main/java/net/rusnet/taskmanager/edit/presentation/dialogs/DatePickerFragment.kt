package net.rusnet.taskmanager.edit.presentation.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import net.rusnet.taskmanager.commons.domain.model.DateType
import java.util.Calendar

class DatePickerFragment : DialogFragment() {

    private lateinit var callback: OnDatePickerDialogResultListener

    interface OnDatePickerDialogResultListener {
        fun onDateSet(dateType: DateType, newDate: Calendar)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = if (context is OnDatePickerDialogResultListener) {
            context
        } else {
            throw ClassCastException(
                context.toString()
                        + " must implement DatePickerFragment.OnDatePickerDialogResultListener"
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val tag = arguments?.getSerializable(KEY_DATE_TYPE) as DateType
        val initialDate = arguments?.getSerializable(KEY_INITIAL_DATE) as Calendar

        return DatePickerDialog(
            activity!!,
            { _, year, month, dayOfMonth ->
                val newDate = Calendar.getInstance().apply { timeInMillis = initialDate.timeInMillis }
                newDate.set(year, month, dayOfMonth)
                callback.onDateSet(tag, newDate)
            },
            initialDate[Calendar.YEAR],
            initialDate[Calendar.MONTH],
            initialDate[Calendar.DAY_OF_MONTH]

        )
    }

    companion object {
        private const val KEY_DATE_TYPE = "KEY_DATE_TYPE"
        private const val KEY_INITIAL_DATE = "KEY_INITIAL_DATE"

        fun newInstance(dateType: DateType, initialDialogDate: Calendar): DatePickerFragment {
            return DatePickerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_DATE_TYPE, dateType)
                    putSerializable(KEY_INITIAL_DATE, initialDialogDate)
                }
            }
        }
    }

}