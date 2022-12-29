package cloud.holfelder.led.app.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.adapter.ModeGridViewAdapter
import cloud.holfelder.led.app.dialog.ErrorDialog
import cloud.holfelder.led.app.model.EspColorModel
import cloud.holfelder.led.app.model.Mode
import cloud.holfelder.led.app.rest.ModeApi
import cloud.holfelder.led.app.store.Store
import cloud.holfelder.led.app.utils.RequestUtils
import cloud.holfelder.led.app.wrapper.ListWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModeFragment : Fragment() {
  private lateinit var grdMode: GridView
  private lateinit var loadingSpinner: ProgressBar
  private lateinit var tvResourcesNotFound: TextView
  private var modes: ListWrapper<Mode> = ListWrapper(listOf())
  private lateinit var modeGridViewAdapter: ModeGridViewAdapter
  private val retrofit = RequestUtils.retrofit

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.mode_fragment, container, false)
  }

  override
  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    grdMode = requireView().findViewById(R.id.grdMode)
    loadingSpinner = requireView().findViewById(R.id.loadingSpinner)
    tvResourcesNotFound = requireView().findViewById(R.id.tvResourcesNotFound)
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
    loadingSpinner.isVisible = true
    tvResourcesNotFound.isVisible = false
    CoroutineScope(Main).launch {
      val modeApi: ModeApi = retrofit.create(ModeApi::class.java)
      val call: Call<ListWrapper<Mode>> = modeApi.loadModes()
      call.enqueue(object : Callback<ListWrapper<Mode>> {
        override
        fun onResponse(call: Call<ListWrapper<Mode>>, response: Response<ListWrapper<Mode>>) {
          if (response.isSuccessful) {
            modes = response.body()!!
            modeGridViewAdapter.refresh(modes)
          } else {
            tvResourcesNotFound.isVisible = true
          }

          if (response.body() == null) {
            tvResourcesNotFound.isVisible = true
          }
          loadingSpinner.isVisible = false
        }

        override fun onFailure(call: Call<ListWrapper<Mode>>, t: Throwable) {
          val e = java.lang.Exception(t)
          showErrorDialog(e, true)
          loadingSpinner.isVisible = false
          tvResourcesNotFound.isVisible = false
        }
      })
    }
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