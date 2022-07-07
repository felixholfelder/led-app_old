package cloud.holfelder.naehstueberl.led.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cloud.holfelder.naehstueberl.led.app.R
import cloud.holfelder.naehstueberl.led.app.adapter.ColorGridViewAdapter
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper

class ColorActivity : AppCompatActivity() {
    private lateinit var grdColor: GridView

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color)
        grdColor = findViewById(R.id.grdColor)
        val colors: ListWrapper<Int> = ListWrapper(listOf(0xfff00000.toInt(), 0xfff00010.toInt(), 0xfff00100.toInt(), 0xfff20000.toInt()))
        val colorGridViewAdapter = ColorGridViewAdapter(colors, this)
        grdColor.adapter = colorGridViewAdapter
        grdColor.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                applicationContext, "${colors.content[position]} selected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override
    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.color_activity_items, menu)
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.itemColorMode -> openColorModes()
            R.id.itemColorRefresh -> loadColors()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openColorModes() {
        startActivity(Intent(this, ColorModeActivity::class.java))
    }

    private fun loadColors() {
        Toast.makeText(this, "Load Colors", Toast.LENGTH_LONG).show()
    }
}