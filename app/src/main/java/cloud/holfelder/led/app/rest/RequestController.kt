package cloud.holfelder.led.app.rest

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request

class RequestController {
    companion object {
        private val USERNAME = "LED-REST"
        private val PASSWORD = "hidh98jlhxl"
        fun getClient(): OkHttpClient {
            return OkHttpClient().newBuilder().addInterceptor { chain ->
                val originalRequest: Request = chain.request()
                val builder: Request.Builder = originalRequest.newBuilder().header(
                    "Authorization", Credentials.basic(USERNAME, PASSWORD)
                )
                val newRequest: Request = builder.build()
                chain.proceed(newRequest)
            }.build()
        }
    }
}
