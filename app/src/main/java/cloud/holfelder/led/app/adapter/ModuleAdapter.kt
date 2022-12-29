package cloud.holfelder.led.app.adapter

import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.activity.HomeActivity
import cloud.holfelder.led.app.dialog.ErrorDialog
import cloud.holfelder.led.app.dialog.ModuleDialog
import cloud.holfelder.led.app.model.Module
import cloud.holfelder.led.app.store.Store
import cloud.holfelder.led.app.wrapper.ListWrapper
import com.neovisionaries.ws.client.*
import okhttp3.OkHttpClient
import java.net.UnknownHostException

class ModuleAdapter(var modules: ListWrapper<Module>, val context: Context,
                    val fragmentManager: FragmentManager) : BaseAdapter() {
  private lateinit var layoutInflater: LayoutInflater
  private lateinit var textModule: TextView
  private lateinit var btnSettings: ImageButton
  private lateinit var btnIsConnected: ImageButton
  private lateinit var client: OkHttpClient
  private var socket: WebSocket? = null

  override fun getCount() = modules.content.size
  override fun getItem(position: Int) = modules.content[position]
  override fun getItemId(position: Int): Long = 0

  override
  fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val policy = ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)

    client = OkHttpClient()
    var view = convertView
    layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    if (view == null) {
      view = layoutInflater.inflate(R.layout.module_item, null)
    }
    textModule = view!!.findViewById(R.id.textModule)
    btnSettings = view.findViewById(R.id.btnSettings)
    btnIsConnected = view.findViewById(R.id.btnIsConnected)

    if (Store.currentModule?.id == getItem(position).id) {
      btnIsConnected.isVisible = true
    }

    textModule.text = getItem(position).name

    setModule(position)
    openSettings(position)
    handleWebSocketEvents()
    return view
  }

  private fun setModule(position: Int) {
    textModule.setOnClickListener {
      val isConnected = connect(getItem(position))
      if (isConnected) {
        Store.currentModuleAddress = getItem(position).address
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context.startActivity(intent)
      }
    }
  }

  private fun connect(module: Module): Boolean {
    try {
      if (socket != null) {
        socket!!.sendClose(444, context.getString(R.string.socket_connection_closed))
      }
      val socket = WebSocketFactory().createSocket("ws://${module.address}:81").connect()
      Store.socket = socket
      Store.currentModule = module
    } catch (e: Exception) {
      val showTrace = e !is UnknownHostException
      showErrorDialog(e, showTrace)
      return false
    }
    return true
  }

  private fun openSettings(position: Int) {
    btnSettings.setOnClickListener {
      val moduleDialog = ModuleDialog(getItem(position), position)
      moduleDialog.show(fragmentManager, "moduleDialog")
    }
  }

  fun refresh(modules: ListWrapper<Module>) {
    this.modules = modules
    notifyDataSetChanged()
  }

  private fun showErrorDialog(e: Exception, showTrace: Boolean) {
    val errorDialog = ErrorDialog(e, showTrace)
    errorDialog.show(fragmentManager, "errorDialog")
  }

  private fun handleWebSocketEvents() {
    Store.socket?.addListener(object : WebSocketAdapter() {
      override fun onConnected(
        websocket: WebSocket?,
        headers: MutableMap<String, MutableList<String>>?
      ) {
        Toast.makeText(
          context,
          "Verbunden mit ${Store.currentModule?.name}!",
          Toast.LENGTH_LONG
        ).show()
      }

      override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
        showErrorDialog(Exception(exception?.message), true)
      }

      override fun onDisconnected(
        websocket: WebSocket?,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean
      ) {
        showErrorDialog(
          Exception("Verbindung zu ${Store.currentModule?.name} unterbrochen"),
          false
        )
      }

      override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
        showErrorDialog(
          Exception("Fehler bei ${Store.currentModule?.name}!"),
          false
        )
      }
    })
  }
}