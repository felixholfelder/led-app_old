package cloud.holfelder.led.app.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.model.Color
import cloud.holfelder.led.app.wrapper.ListWrapper

class ColorGridViewAdapter(var colors: ListWrapper<Color>, val context: Context) : BaseAdapter() {
  private lateinit var layoutInflater: LayoutInflater
  private lateinit var colorCard: LinearLayout

  override fun getCount() = colors.content.size
  override fun getItem(position: Int) = colors.content[position]
  override fun getItemId(position: Int): Long = 0

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    var view = convertView
    layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    if (view == null) {
      view = layoutInflater.inflate(R.layout.color_grid_item, null)
    }
    colorCard = view!!.findViewById(R.id.colorCard)
    val colorFilter = PorterDuffColorFilter(
      android.graphics.Color.parseColor(colors.content[position].hex), PorterDuff.Mode.SRC_ATOP)
    colorCard.background.colorFilter = colorFilter
    return view
  }

  fun refresh(colors: ListWrapper<Color>) {
    this.colors = colors
    notifyDataSetChanged()
  }
}