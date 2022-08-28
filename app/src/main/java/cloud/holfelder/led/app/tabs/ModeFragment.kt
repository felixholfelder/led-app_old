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
import cloud.holfelder.led.app.rest.ModeApi
import cloud.holfelder.led.app.store.Store
import cloud.holfelder.led.app.utils.RequestUtils
import cloud.holfelder.led.app.wrapper.ListWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModeFragment : Fragment(), Callback<ListWrapper<Mode>> {
    private lateinit var grdMode: GridView
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
        val modeApi: ModeApi = retrofit.create(ModeApi::class.java)
        val call: Call<ListWrapper<Mode>> = modeApi.loadModes()
        call.enqueue(this)
    }

    private fun setAdapter() {
        modeGridViewAdapter = ModeGridViewAdapter(modes, requireContext())
        grdMode.adapter = modeGridViewAdapter
    }

    override
    fun onResponse(call: Call<ListWrapper<Mode>>, response: Response<ListWrapper<Mode>>) {
        if (response.isSuccessful) {
            modes = response.body()!!
            modeGridViewAdapter.refresh(modes)
        }
    }

    override fun onFailure(call: Call<ListWrapper<Mode>>, t: Throwable) {
        val e = java.lang.Exception(t)
        showErrorDialog(e, true)
    }

    private fun showErrorDialog(e: Exception, showTrace: Boolean) {
        val errorDialog = ErrorDialog(e, showTrace)
        errorDialog.show(parentFragmentManager, "errorDialog")
    }
}