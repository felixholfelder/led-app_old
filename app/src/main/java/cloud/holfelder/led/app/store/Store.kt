package cloud.holfelder.led.app.store

import android.app.Application
import cloud.holfelder.led.app.SocketListener
import okhttp3.WebSocket
import java.net.Socket

class Store : Application() {
    companion object {
        var currentModuleAddress = ""
        var currentColorId = -1
        var currentModeId = -1
        var isModeActive = false
        lateinit var socket: WebSocket
    }
}