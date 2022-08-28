package cloud.holfelder.led.app.rest

import cloud.holfelder.led.app.model.EspColorModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EspApi {
    @POST("mode")
    fun setMode(@Body espColorModel: EspColorModel): Call<Any>

    @POST("color")
    fun setColor(@Body espColorModel: EspColorModel): Call<Any>
}
