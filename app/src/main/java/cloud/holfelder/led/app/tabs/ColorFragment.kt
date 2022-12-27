package cloud.holfelder.led.app.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.adapter.ColorGridViewAdapter
import cloud.holfelder.led.app.dialog.ErrorDialog
import cloud.holfelder.led.app.model.Color
import cloud.holfelder.led.app.model.EspColorModel
import cloud.holfelder.led.app.model.Mode
import cloud.holfelder.led.app.rest.ColorApi
import cloud.holfelder.led.app.rest.ModeApi
import cloud.holfelder.led.app.store.Store
import cloud.holfelder.led.app.utils.RequestUtils
import cloud.holfelder.led.app.wrapper.ListWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ColorFragment : Fragment() {
  private lateinit var grdColor: GridView
  private lateinit var loadingSpinner: ProgressBar
  private var colors: ListWrapper<Color> = ListWrapper(listOf())
  private lateinit var colorGridViewAdapter: ColorGridViewAdapter
  private val RED = 0
  private val GREEN = 1
  private val BLUE = 2
  private val retrofit = RequestUtils.retrofit

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
    loadingSpinner = requireView().findViewById(R.id.loadingSpinner)
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
    try {
      if (Store.socket == null) {
        throw IllegalStateException(getString(R.string.socket_no_connection))
      }
      Store.socket?.sendText(
        EspColorModel(
          null,
          getRGB(colors.content[position].hex)[RED],
          getRGB(colors.content[position].hex)[GREEN],
          getRGB(colors.content[position].hex)[BLUE]
        ).toJson()
      )
    } catch (e: Exception) {
      val showTrace = e !is IllegalStateException
      showErrorDialog(e, showTrace)
    }
  }

  private fun setMode(position: Int) {
    try {
      if (Store.socket == null) {
        throw IllegalStateException(getString(R.string.socket_no_connection))
      }
      Store.socket?.sendText(
        EspColorModel(
          Store.currentModeId,
          getRGB(colors.content[position].hex)[RED],
          getRGB(colors.content[position].hex)[GREEN],
          getRGB(colors.content[position].hex)[BLUE]
        ).toJson()
      )
    } catch (e: Exception) {
      val showTrace = e !is IllegalStateException
      showErrorDialog(e, showTrace)
    }
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
    loadingSpinner.isVisible = true
    CoroutineScope(Dispatchers.Main).launch {
      val colorApi: ColorApi = retrofit.create(ColorApi::class.java)
      val call: Call<ListWrapper<Color>> = colorApi.loadColors()
      call.enqueue(object : Callback<ListWrapper<Color>> {
        override fun onResponse(
          call: Call<ListWrapper<Color>>,
          response: Response<ListWrapper<Color>>
        ) {
          if (response.isSuccessful) {
            colors = response.body()!!
            colorGridViewAdapter.refresh(colors)
            loadingSpinner.isVisible = false
          }
        }

        override fun onFailure(call: Call<ListWrapper<Color>>, t: Throwable) {
          val e = java.lang.Exception(t)
          showErrorDialog(e, true)
          loadingSpinner.isVisible = false
        }
      })
    }
  }

  private fun showErrorDialog(e: Exception, showTrace: Boolean) {
    val errorDialog = ErrorDialog(e, showTrace)
    errorDialog.show(parentFragmentManager, "errorDialog")
  }
}