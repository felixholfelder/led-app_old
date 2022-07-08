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
import cloud.holfelder.naehstueberl.led.app.adapter.ColorGridViewAdapter
import cloud.holfelder.naehstueberl.led.app.model.Color
import cloud.holfelder.naehstueberl.led.app.rest.ColorApi
import cloud.holfelder.naehstueberl.led.app.rest.RequerstController
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ColorFragment : Fragment(), Callback<ListWrapper<Color>> {
    private lateinit var grdColor: GridView
    private var colors: ListWrapper<Color> = ListWrapper(listOf())
    private lateinit var colorGridViewAdapter: ColorGridViewAdapter

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
        colorGridViewAdapter = ColorGridViewAdapter(colors, requireContext())
        grdColor.adapter = colorGridViewAdapter
    }

    private fun loadColors() {
        val BASE = "https://led-rest.holfelder.cloud/api/"
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(RequerstController.getClient())
            .build()
        val colorApi: ColorApi = retrofit.create(ColorApi::class.java)
        val call: Call<ListWrapper<Color>> = colorApi.loadColors()
        call.enqueue(this)
    }

    override
    fun onResponse(call: Call<ListWrapper<Color>>, response: Response<ListWrapper<Color>>) {
        if (response.isSuccessful) {
            colors = response.body()!!
            colorGridViewAdapter.refresh(colors)
        }
    }

    override
    fun onFailure(call: Call<ListWrapper<Color>>, t: Throwable) {
        Toast.makeText(context, "Ladung der Farben fehlgeschlagen!", Toast.LENGTH_LONG).show()
    }
}