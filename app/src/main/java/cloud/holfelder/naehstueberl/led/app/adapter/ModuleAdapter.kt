package cloud.holfelder.naehstueberl.led.app.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import cloud.holfelder.naehstueberl.led.app.R
import cloud.holfelder.naehstueberl.led.app.dialog.ModuleDialog
import cloud.holfelder.naehstueberl.led.app.model.Module
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper

class ModuleAdapter(val colorModes: ListWrapper<Module>, val context: Context, val supportFragmentManager: FragmentManager) : BaseAdapter() {
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var textModule: TextView
    private lateinit var btnSettings: ImageButton

    override fun getCount() = colorModes.content.size
    override fun getItem(position: Int) = colorModes.content[position]
    override fun getItemId(position: Int): Long = 0

    override
    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (view == null) {
            view = layoutInflater.inflate(R.layout.module_item, null)
        }
        textModule = view!!.findViewById(R.id.textModule)
        textModule.text = getItem(position).name

        btnSettings = view.findViewById(R.id.btnSettings)
        openSettings(position)
        return view
    }

    private fun openSettings(position: Int) {
        btnSettings.setOnClickListener {
            val moduleDialog = ModuleDialog(getItem(position), position)
            moduleDialog.show(supportFragmentManager, "moduleDialog")
        }
    }
}