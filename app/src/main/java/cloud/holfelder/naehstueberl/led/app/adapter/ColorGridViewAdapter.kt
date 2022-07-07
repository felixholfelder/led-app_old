package cloud.holfelder.naehstueberl.led.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import cloud.holfelder.naehstueberl.led.app.R
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper

class ColorGridViewAdapter(val colors: ListWrapper<Int>, val context: Context) : BaseAdapter() {
    private lateinit var layoutInflater: LayoutInflater
    private lateinit var colorCard: LinearLayout

    override fun getCount() = colors.content.size
    override fun getItem(position: Int) = null
    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (view == null) {
            view = layoutInflater.inflate(R.layout.color_grid_item, null)
        }
        colorCard = view!!.findViewById(R.id.colorCard)
        colorCard.setBackgroundColor(colors.content[position])
        return view
    }
}