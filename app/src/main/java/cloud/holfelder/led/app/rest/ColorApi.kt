package cloud.holfelder.led.app.rest

import cloud.holfelder.led.app.model.Color
import cloud.holfelder.led.app.wrapper.ListWrapper
import retrofit2.Call
import retrofit2.http.GET

interface ColorApi {
  @GET("colors")
  fun loadColors(): Call<ListWrapper<Color>>
}
