package cloud.holfelder.naehstueberl.led.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.fragment.app.Fragment
import cloud.holfelder.naehstueberl.led.app.R
import cloud.holfelder.naehstueberl.led.app.adapter.ModeGridViewAdapter
import cloud.holfelder.naehstueberl.led.app.model.Mode
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper

class ModeFragment : Fragment() {
    private lateinit var grdMode: GridView
    private lateinit var modes: ListWrapper<Mode>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mode_fragment, container, false)
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        grdMode = requireView().findViewById(R.id.grdMode)
        loadColorModes()
        setAdapter()
        setColorMode()
    }

    private fun setColorMode() {
        grdMode.onItemClickListener = OnItemClickListener { _, _, pos, _ ->
            // TODO: set colormode globally
        }
    }

    private fun loadColorModes() {
        modes = ListWrapper(listOf())
    }

    private fun setAdapter() {
        val modeGridViewAdapter = ModeGridViewAdapter(modes, requireContext())
        grdMode.adapter = modeGridViewAdapter
    }
}