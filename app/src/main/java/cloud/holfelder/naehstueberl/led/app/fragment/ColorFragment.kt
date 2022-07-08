package cloud.holfelder.naehstueberl.led.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.fragment.app.Fragment
import cloud.holfelder.naehstueberl.led.app.R
import cloud.holfelder.naehstueberl.led.app.adapter.ColorGridViewAdapter
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper

class ColorFragment : Fragment() {
    private lateinit var grdColor: GridView
    private lateinit var colors: ListWrapper<Int>

    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.color_fragment, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        grdColor = requireView().findViewById(R.id.grdColor)
        loadColors()
        setColor()
        setAdapter()
    }

    private fun setColor() {
        grdColor.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            // TODO: set current color globally
        }
    }

    private fun setAdapter() {
        val colorGridViewAdapter = ColorGridViewAdapter(colors, requireContext())
        grdColor.adapter = colorGridViewAdapter
    }

    private fun loadColors() {
        // TODO: request for loading colors
        colors = ListWrapper(listOf())
    }
}