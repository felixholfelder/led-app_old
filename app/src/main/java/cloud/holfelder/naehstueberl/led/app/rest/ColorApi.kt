package cloud.holfelder.naehstueberl.led.app.rest

import cloud.holfelder.naehstueberl.led.app.model.Color
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper
import retrofit2.Call
import retrofit2.http.GET

interface ColorApi {
    @GET("colors")
    fun loadColors(): Call<ListWrapper<Color>>
}
