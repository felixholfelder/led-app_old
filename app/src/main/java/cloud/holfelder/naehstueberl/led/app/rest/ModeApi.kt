package cloud.holfelder.naehstueberl.led.app.rest

import cloud.holfelder.naehstueberl.led.app.model.Mode
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper
import retrofit2.Call
import retrofit2.http.GET

interface ModeApi {
    @GET("modes")
    fun loadModes(): Call<ListWrapper<Mode>>
}
