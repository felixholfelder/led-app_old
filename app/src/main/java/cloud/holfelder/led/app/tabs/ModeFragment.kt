package cloud.holfelder.led.app.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.fragment.app.Fragment
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.adapter.ModeGridViewAdapter
import cloud.holfelder.led.app.dialog.ErrorDialog
import cloud.holfelder.led.app.model.EspColorModel
import cloud.holfelder.led.app.model.Mode
import cloud.holfelder.led.app.store.Store
import cloud.holfelder.led.app.wrapper.ListWrapper
import led.rest.enums.ColorModeEnum

class ModeFragment : Fragment() {
  private lateinit var grdMode: GridView
  private var modes: ListWrapper<Mode> = ListWrapper(listOf())
  private lateinit var modeGridViewAdapter: ModeGridViewAdapter

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
    grdMode.onItemClickListener = OnItemClickListener { _, _, position, _ ->
      try {
        if (Store.socket == null) {
          throw IllegalStateException(getString(R.string.socket_no_connection))
        }
        Store.currentModeId = modes.content[position].modeId
        Store.socket?.sendText(
          EspColorModel(Store.currentModeId, null, null, null).toJson()
        )
        Store.isModeActive = true
      } catch (e: Exception) {
        val showTrace = e !is IllegalStateException
        showErrorDialog(e, showTrace)
      }
    }
  }

  private fun loadColorModes() {
    val modes = ColorModeEnum.values()
    val wrapper: ListWrapper<Mode> = ListWrapper(listOf())
    val list: MutableList<Mode> = mutableListOf()
    modes.forEachIndexed { index, mode -> list.add(Mode(index, mode.modeName)) }
    wrapper.content = list
    this.modes = wrapper
  }

  private fun setAdapter() {
    modeGridViewAdapter = ModeGridViewAdapter(modes, requireContext())
    grdMode.adapter = modeGridViewAdapter
  }

  private fun showErrorDialog(e: Exception, showTrace: Boolean) {
    val errorDialog = ErrorDialog(e, showTrace)
    errorDialog.show(parentFragmentManager, "errorDialog")
  }
}