package cloud.holfelder.naehstueberl.led.app.rest

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request

class RequestController {
    companion object {
        private val USERNAME = ""
        private val PASSWORD = ""
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
