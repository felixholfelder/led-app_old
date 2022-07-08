package cloud.holfelder.naehstueberl.led.app.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cloud.holfelder.naehstueberl.led.app.R
import cloud.holfelder.naehstueberl.led.app.adapter.ModuleAdapter
import cloud.holfelder.naehstueberl.led.app.dialog.ModuleDialog
import cloud.holfelder.naehstueberl.led.app.model.Color
import cloud.holfelder.naehstueberl.led.app.model.Module
import cloud.holfelder.naehstueberl.led.app.rest.ColorApi
import cloud.holfelder.naehstueberl.led.app.rest.ModuleApi
import cloud.holfelder.naehstueberl.led.app.rest.RequerstController
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModuleActivity : AppCompatActivity(), ModuleDialog.ModuleItemListener,
    Callback<ListWrapper<Module>> {
    private lateinit var listModule: ListView
    private var modules: ListWrapper<Module> = ListWrapper(listOf())
    private lateinit var moduleAdapter: ModuleAdapter

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module)
        listModule = findViewById(R.id.listModule)

        loadModules()
        setModuleAdapter()
        setModule()
    }

    private fun setModule() {
        listModule.onItemClickListener = OnItemClickListener { _, _, pos, _ ->
            // TODO: set current module globally
        }
    }

    override
    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.module_items, menu)
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.itemAddModule -> openModuleDialog()
            R.id.itemModuleRefresh -> loadModules()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openModuleDialog() {
        val moduleDialog = ModuleDialog(null, null)
        moduleDialog.show(supportFragmentManager, "moduleDialog")
    }

    private fun setModuleAdapter() {
        moduleAdapter = ModuleAdapter(modules, this, supportFragmentManager)
        listModule.adapter = moduleAdapter
    }

    private fun loadModules() {
        val BASE = "https://led-rest.holfelder.cloud/api/"
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(RequerstController.getClient())
            .build()
        val moduleApi: ModuleApi = retrofit.create(ModuleApi::class.java)
        val call: Call<ListWrapper<Module>> = moduleApi.loadModules()
        call.enqueue(this)
    }

    override
    fun createModule(name: String, address: String, mac: String) {
        val module = Module(null, name, address, mac)
        val BASE = "https://led-rest.holfelder.cloud/api/"
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(RequerstController.getClient())
            .build()
        val moduleApi: ModuleApi = retrofit.create(ModuleApi::class.java)
        val call: Call<ListWrapper<Module>> = moduleApi.createModule(module)
        call.enqueue(this)
    }

    override
    fun updateModule(name: String, address: String, mac: String, pos: Int) {
        val module = modules.content[pos]
        module.name = name
        module.address = address
        module.mac = mac
        val BASE = "https://led-rest.holfelder.cloud/api/"
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(RequerstController.getClient())
            .build()
        val moduleApi: ModuleApi = retrofit.create(ModuleApi::class.java)
        val call: Call<ListWrapper<Module>> = moduleApi.updateModule(module.id!!, module)
        call.enqueue(this)
    }

    override
    fun onResponse(call: Call<ListWrapper<Module>>, response: Response<ListWrapper<Module>>) {
        if (response.isSuccessful) {
            modules = response.body()!!
            moduleAdapter.refresh(modules)
        }
    }

    override
    fun onFailure(call: Call<ListWrapper<Module>>, t: Throwable) {
        Toast.makeText(this, "Ladung der Module fehlgeschlagen!", Toast.LENGTH_LONG).show()
    }
}