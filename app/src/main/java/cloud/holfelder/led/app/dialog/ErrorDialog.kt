package cloud.holfelder.led.app.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import cloud.holfelder.led.app.R

class ErrorDialog(var e: Exception, var showTrace: Boolean) : AppCompatDialogFragment() {
    override
    fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle)
        val inflater: LayoutInflater = activity?.layoutInflater!!
        val view: View? = inflater.inflate(R.layout.error_dialog, null)

        if (showTrace) {
            val errorMessage = view?.findViewById<TextView>(R.id.txtErrorMessage)
            errorMessage?.text = e.stackTraceToString()
        }

        builder.setView(view)
            .setTitle(e.message)
            .setNegativeButton(getString(R.string.dialog_close)) { _, _ -> }
        return builder.create()
    }
}