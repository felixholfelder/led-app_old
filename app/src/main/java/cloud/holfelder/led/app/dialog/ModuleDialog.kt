package cloud.holfelder.led.app.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.model.Module

class ModuleDialog(private val item: Module?) : AppCompatDialogFragment() {
  private lateinit var itemModuleName: EditText
  private lateinit var itemModuleAddress: EditText

  override
  fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val builder = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle)
    val inflater: LayoutInflater = activity?.layoutInflater!!
    val view: View? = inflater.inflate(R.layout.module_dialog, null)

    itemModuleName = view!!.findViewById(R.id.itemModuleName)
    itemModuleAddress = view.findViewById(R.id.itemModuleAddress)
    itemModuleName.setText(item?.name)
    itemModuleAddress.setText(item?.address)

    builder.setView(view)
      .setTitle(getString(R.string.dialog_module))
      .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
    return builder.create()
  }
}