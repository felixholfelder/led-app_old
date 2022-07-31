package cloud.holfelder.led.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.adapter.ModeGridViewAdapter
import cloud.holfelder.led.app.model.EspModel
import cloud.holfelder.led.app.model.Mode
import cloud.holfelder.led.app.rest.EspApi
import cloud.holfelder.led.app.rest.ModeApi
import cloud.holfelder.led.app.rest.RequestController
import cloud.holfelder.led.app.store.Store
import cloud.holfelder.led.app.wrapper.ListWrapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModeFragment : Fragment(), Callback<ListWrapper<Mode>> {
    private lateinit var grdMode: GridView
    private var modes: ListWrapper<Mode> = ListWrapper(listOf())
    private lateinit var modeGridViewAdapter: ModeGridViewAdapter
    private val BASE = "https://led-rest.holfelder.cloud/api/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
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
            val BASE = "http://${Store.currentModuleAddress}/api/"
            val gson: Gson = GsonBuilder().setLenient().create()
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(RequestController.getClient())
                .build()
            val espApi: EspApi = retrofit.create(EspApi::class.java)
            val call: Call<Any> = espApi.setMode(
                EspModel(modes.content[position].modeId,null, null, null)
            )
            call.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {}
                override fun onFailure(call: Call<Any>, t: Throwable) {}
            })
            Store.currentModeId = modes.content[position].modeId
            Store.isModeActive = true
        }
    }

    private fun loadColorModes() {
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(RequestController.getClient())
            .build()
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

    override
    fun onFailure(call: Call<ListWrapper<Mode>>, t: Throwable) =
        Toast.makeText(context, getString(R.string.module_load_modes_failed), Toast.LENGTH_LONG)
            .show()
}