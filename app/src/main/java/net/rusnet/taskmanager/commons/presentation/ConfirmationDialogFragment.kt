package net.rusnet.taskmanager.commons.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import net.rusnet.taskmanager.R

class ConfirmationDialogFragment : DialogFragment() {

    private lateinit var callback: ConfirmationDialogListener

    interface ConfirmationDialogListener {
        fun onPositiveResponse(dialogTag: String)
        fun onNegativeResponse(dialogTag: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = if (context is ConfirmationDialogListener) {
            context
        } else {
            throw ClassCastException(
                context.toString()
                        + " must implement ConfirmationDialogFragment.ConfirmationDialogListener"
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogTitle = arguments?.getString(KEY_TITLE) as String
        val dialogTag = arguments?.getString(KEY_TAG) as String

        return AlertDialog.Builder(requireContext())
            .setTitle(dialogTitle)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                callback.onPositiveResponse(dialogTag)
//                dialog.dismiss()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                callback.onNegativeResponse(dialogTag)
//                dialog.dismiss()
            }.create()
    }

    companion object {
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_TAG = "KEY_TAG"

        fun newInstance(dialogTitle: String, dialogTag: String): ConfirmationDialogFragment {
            return ConfirmationDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TITLE, dialogTitle)
                    putString(KEY_TAG, dialogTag)
                }
            }
        }
    }
}