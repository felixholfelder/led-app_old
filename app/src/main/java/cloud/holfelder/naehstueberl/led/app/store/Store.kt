package cloud.holfelder.naehstueberl.led.app.store

import android.app.Application

class Store : Application() {
    companion object {
        var currentModuleAddress = ""
        var currentColorId = -1
        var currentModeId = -1
        var isModeActive = false
    }
}