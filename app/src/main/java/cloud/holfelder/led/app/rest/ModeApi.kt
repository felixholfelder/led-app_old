package cloud.holfelder.led.app.rest

import cloud.holfelder.led.app.model.Mode
import cloud.holfelder.led.app.wrapper.ListWrapper
import retrofit2.Call
import retrofit2.http.GET

interface ModeApi {
  @GET("modes")
  fun loadModes(): Call<ListWrapper<Mode>>
}
