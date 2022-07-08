package cloud.holfelder.naehstueberl.led.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import cloud.holfelder.naehstueberl.led.app.R
import cloud.holfelder.naehstueberl.led.app.model.Mode
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper

class ModeGridViewAdapter(var modes: ListWrapper<Mode>, val context: Context) : BaseAdapter() {
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var colorModeText: TextView

    override fun getCount() = modes.content.size
    override fun getItem(position: Int) = null
    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (view == null) {
            view = layoutInflater.inflate(R.layout.mode_grid_item, null)
        }
        colorModeText = view!!.findViewById(R.id.modeText)
        colorModeText.text = modes.content[position].modeName
        return view
    }

    fun refresh(modes: ListWrapper<Mode>) {
        this.modes = modes
        notifyDataSetChanged()
    }
}