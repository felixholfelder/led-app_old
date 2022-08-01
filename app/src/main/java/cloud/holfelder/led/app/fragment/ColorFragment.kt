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
import cloud.holfelder.led.app.adapter.ColorGridViewAdapter
import cloud.holfelder.led.app.model.Color
import cloud.holfelder.led.app.model.EspModel
import cloud.holfelder.led.app.rest.ColorApi
import cloud.holfelder.led.app.rest.EspApi
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

class ColorFragment : Fragment() {
    private lateinit var grdColor: GridView
    private var colors: ListWrapper<Color> = ListWrapper(listOf())
    private lateinit var colorGridViewAdapter: ColorGridViewAdapter
    private val RED = 0
    private val GREEN = 1
    private val BLUE = 2
    private val BASE = "https://led-rest.holfelder.cloud/api/"

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
        handleAction()
        setAdapter()
    }

    private fun handleAction() {
        grdColor.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            if (Store.isModeActive && Store.currentColorId != colors.content[position].id) {
                setMode(position)
                Store.isModeActive = true
            } else {
                setColor(position)
                Store.isModeActive = false
            }
            Store.currentColorId = colors.content[position].id
        }
    }

    private fun setColor(position: Int) {
        Store.socket.send(
            EspModel(
                null,
                getRGB(colors.content[position].hex)[RED],
                getRGB(colors.content[position].hex)[GREEN],
                getRGB(colors.content[position].hex)[BLUE]
            ).toJson()
        )
    }

    private fun setMode(position: Int) {
        Store.socket.send(
            EspModel(
                Store.currentModeId,
                getRGB(colors.content[position].hex)[RED],
                getRGB(colors.content[position].hex)[GREEN],
                getRGB(colors.content[position].hex)[BLUE]
            ).toJson()
        )
    }

    private fun setAdapter() {
        colorGridViewAdapter = ColorGridViewAdapter(colors, requireContext())
        grdColor.adapter = colorGridViewAdapter
    }

    private fun getRGB(rgb: String): IntArray {
        val r = rgb.substring(1, 3).toInt(16)
        val g = rgb.substring(3, 5).toInt(16)
        val b = rgb.substring(5, 7).toInt(16)
        return intArrayOf(r, g, b)
    }

    private fun loadColors() {
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(RequestController.getClient())
            .build()
        val colorApi: ColorApi = retrofit.create(ColorApi::class.java)
        val call: Call<ListWrapper<Color>> = colorApi.loadColors()
        call.enqueue(object : Callback<ListWrapper<Color>> {
            override
            fun onResponse(
                call: Call<ListWrapper<Color>>,
                response: Response<ListWrapper<Color>>
            ) {
                if (response.isSuccessful) {
                    colors = response.body()!!
                    colorGridViewAdapter.refresh(colors)
                }
            }

            override
            fun onFailure(call: Call<ListWrapper<Color>>, t: Throwable) =
                Toast.makeText(
                    context, getString(R.string.module_load_colors_failed),
                    Toast.LENGTH_LONG
                ).show()
        })
    }
}