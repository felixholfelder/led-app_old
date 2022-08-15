package cloud.holfelder.led.app.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.model.Module

class ModuleDialog(private val item: Module?, private val pos: Int?) : AppCompatDialogFragment() {
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

        if (pos != null) {
            itemModuleName.setText(item?.name)
            itemModuleAddress.setText(item?.address)
            itemModuleMac.setText(item?.mac)

            builder.setView(view)
                .setTitle(getString(R.string.dialog_module))
                .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
                .setNeutralButton(getString(R.string.dialog_delete)) { _, _ ->
                    moduleItemListener.deleteModule(item?.id!!)
                }
                .setPositiveButton(getString(R.string.dialog_save)) { _, _ ->
                    val name = itemModuleName.text.toString()
                    val address = itemModuleAddress.text.toString()
                    val mac = itemModuleMac.text.toString()
                    if (pos == null) {
                        moduleItemListener.createModule(name, address, mac)
                    } else {
                        moduleItemListener.updateModule(name, address, mac, pos)
                    }
                }
        }

        builder.setView(view)
            .setTitle(getString(R.string.dialog_module))
            .setNegativeButton(getString(R.string.dialog_cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.dialog_save)) { _, _ ->
                val name = itemModuleName.text.toString()
                val address = itemModuleAddress.text.toString()
                val mac = itemModuleMac.text.toString()
                if (pos == null) {
                    moduleItemListener.createModule(name, address, mac)
                } else {
                    moduleItemListener.updateModule(name, address, mac, pos)
                }
            }
        return builder.create()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        moduleItemListener = activity as ModuleItemListener
    }

    interface ModuleItemListener {
        fun createModule(name: String, address: String, mac: String)
        fun updateModule(name: String, address: String, mac: String, pos: Int)
        fun deleteModule(id: Int)
    }
}