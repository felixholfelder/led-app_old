package cloud.holfelder.led.app.rest

import cloud.holfelder.led.app.model.EspModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EspApi {
    @POST("mode")
    fun setMode(@Body espModel: EspModel): Call<Any>

    @POST("color")
    fun setColor(@Body espModel: EspModel): Call<Any>
}
