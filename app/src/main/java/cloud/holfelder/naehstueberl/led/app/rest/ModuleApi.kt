package cloud.holfelder.naehstueberl.led.app.rest

import cloud.holfelder.naehstueberl.led.app.model.Module
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper
import retrofit2.Call
import retrofit2.http.*

interface ModuleApi {
    @GET("modules")
    fun loadModules(): Call<ListWrapper<Module>>

    @POST("modules")
    fun createModule(@Body module: Module): Call<ListWrapper<Module>>

    @PUT("modules/{id}")
    fun updateModule(@Path("id") id: Int, @Body module: Module): Call<ListWrapper<Module>>
}
