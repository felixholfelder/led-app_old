package cloud.holfelder.led.app.utils

import cloud.holfelder.led.app.rest.RequestController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RequestUtils {
    companion object {
        private val BASE = "https://led-rest.holfelder.cloud/api/"
        private val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(RequestController.getClient())
            .build()
    }
}