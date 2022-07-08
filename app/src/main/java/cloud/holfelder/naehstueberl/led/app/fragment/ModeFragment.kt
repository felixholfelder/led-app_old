package cloud.holfelder.naehstueberl.led.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cloud.holfelder.naehstueberl.led.app.R
import cloud.holfelder.naehstueberl.led.app.adapter.ModeGridViewAdapter
import cloud.holfelder.naehstueberl.led.app.model.Color
import cloud.holfelder.naehstueberl.led.app.model.Mode
import cloud.holfelder.naehstueberl.led.app.rest.ColorApi
import cloud.holfelder.naehstueberl.led.app.rest.ModeApi
import cloud.holfelder.naehstueberl.led.app.rest.RequerstController
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper
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
        val BASE = "https://led-rest.holfelder.cloud/api/"
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(RequerstController.getClient())
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
    fun onFailure(call: Call<ListWrapper<Mode>>, t: Throwable) {
        Toast.makeText(context, "Ladung der Modi fehlgeschlagen!", Toast.LENGTH_LONG).show()
    }
}