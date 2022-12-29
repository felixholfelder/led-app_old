package cloud.holfelder.led.app.store

import android.app.Application
import cloud.holfelder.led.app.model.Module
import com.neovisionaries.ws.client.WebSocket

class Store : Application() {
  companion object {
    var currentModuleAddress = ""
    var currentColor = ""
    var currentModeId = -1
    var currentModule: Module? = null
    var isModeActive = false
    var socket: WebSocket? = null
  }
}