package cloud.holfelder.naehstueberl.led.app.rest

import cloud.holfelder.naehstueberl.led.app.model.EspModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.POST

interface EspApi {
    @POST("mode")
    fun setMode(@Body espModel: EspModel): Call<Any>

    @POST("color")
    fun setColor(@Body espModel: EspModel): Call<Any>
}
