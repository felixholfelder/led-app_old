package cloud.holfelder.led.app.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.model.Module

class ModuleDialog(private val item: Module?) : AppCompatDialogFragment() {
  private lateinit var itemModuleName: EditText
  private lateinit var itemModuleAddress: EditText
  private lateinit var itemModuleMac: EditText
  private lateinit var moduleItemListener: ModuleItemListener

  override
  fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val builder = AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle)
    val inflater: LayoutInflater = activity?.layoutInflater!!
    val view: View? = inflater.inflate(R.layout.module_dialog, null)

    itemModuleName = view!!.findViewById(R.id.itemModuleName)
    itemModuleAddress = view.findViewById(R.id.itemModuleAddress)
    itemModuleMac = view.findViewById(R.id.itemModuleMac)
    itemModuleName.setText(item?.name)
    itemModuleAddress.setText(item?.address)
    itemModuleMac.setText(item?.mac)

    builder.setView(view)
      .setTitle(getString(R.string.dialog_module))
      .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
      .setPositiveButton(getString(R.string.dialog_delete)) { _, _ ->
        moduleItemListener.deleteModule(item?.id!!)
      }
    return builder.create()
  }

  override fun onAttach(activity: Activity) {
    super.onAttach(activity)
    moduleItemListener = activity as ModuleItemListener
  }

  interface ModuleItemListener {
    fun deleteModule(id: String)
  }
}