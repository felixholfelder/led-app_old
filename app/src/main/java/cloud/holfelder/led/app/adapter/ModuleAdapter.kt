package cloud.holfelder.led.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.activity.HomeActivity
import cloud.holfelder.led.app.dialog.ModuleDialog
import cloud.holfelder.led.app.model.Module
import cloud.holfelder.led.app.store.Store
import cloud.holfelder.led.app.wrapper.ListWrapper

class ModuleAdapter(var modules: ListWrapper<Module>, val context: Context,
                    val supportFragmentManager: FragmentManager) : BaseAdapter() {
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var textModule: TextView
    private lateinit var btnSettings: ImageButton

    override fun getCount() = modules.content.size
    override fun getItem(position: Int) = modules.content[position]
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
        setModule(position)

        btnSettings = view.findViewById(R.id.btnSettings)
        openSettings(position)
        return view
    }

    private fun setModule(position: Int) {
        textModule.setOnClickListener {
            Store.currentModuleAddress = getItem(position).address
            val intent = Intent(context, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }

    private fun openSettings(position: Int) {
        btnSettings.setOnClickListener {
            val moduleDialog = ModuleDialog(getItem(position), position)
            moduleDialog.show(supportFragmentManager, "moduleDialog")
        }
    }

    fun refresh(modules: ListWrapper<Module>) {
        this.modules = modules
        notifyDataSetChanged()
    }
}