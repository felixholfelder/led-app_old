package cloud.holfelder.led.app.rest

import cloud.holfelder.led.app.model.Module
import cloud.holfelder.led.app.wrapper.ListWrapper
import retrofit2.Call
import retrofit2.http.*

interface ModuleApi {
  @GET("modules")
  fun loadModules(): Call<ListWrapper<Module>>

  @POST("modules")
  fun createModule(@Body module: Module): Call<ListWrapper<Module>>

  @PUT("modules/{id}")
  fun updateModule(@Path("id") id: String?, @Body module: Module): Call<ListWrapper<Module>>

  @DELETE("modules/{id}")
  fun deleteModule(@Path("id") id: String?): Call<Any>
}
