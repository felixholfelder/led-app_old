package cloud.holfelder.led.app.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.adapter.ModuleAdapter
import cloud.holfelder.led.app.dialog.ErrorDialog
import cloud.holfelder.led.app.dialog.ModuleDialog
import cloud.holfelder.led.app.model.Color
import cloud.holfelder.led.app.model.Module
import cloud.holfelder.led.app.rest.ColorApi
import cloud.holfelder.led.app.rest.ModuleApi
import cloud.holfelder.led.app.utils.RequestUtils
import cloud.holfelder.led.app.wrapper.ListWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.EOFException

class ModuleActivity : AppCompatActivity(), ModuleDialog.ModuleItemListener,
  Callback<ListWrapper<Module>> {
  private lateinit var listModule: ListView
  private lateinit var loadingSpinner: ProgressBar
  private lateinit var tvResourcesNotFound: TextView
  private var modules: ListWrapper<Module> = ListWrapper(listOf())
  private lateinit var moduleAdapter: ModuleAdapter
  private val retrofit = RequestUtils.retrofit

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_module)
    listModule = findViewById(R.id.listModule)
    loadingSpinner = findViewById(R.id.loadingSpinner)
    tvResourcesNotFound = findViewById(R.id.tvResourcesNotFound)

    loadModules()
    setModuleAdapter()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.module_items, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
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
    loadingSpinner.isVisible = true
    tvResourcesNotFound.isVisible = false
    CoroutineScope(Dispatchers.Main).launch {
      val moduleApi: ModuleApi = retrofit.create(ModuleApi::class.java)
      val call: Call<ListWrapper<Module>> = moduleApi.loadModules()
      call.enqueue(this@ModuleActivity)
    }
  }

  override fun createModule(name: String, address: String, mac: String) {
    val module = Module(null, name, address, mac)
    val moduleApi: ModuleApi = retrofit.create(ModuleApi::class.java)
    val call: Call<ListWrapper<Module>> = moduleApi.createModule(module)
    call.enqueue(this)
  }

  override fun updateModule(name: String, address: String, mac: String, pos: Int) {
    val module = modules.content[pos]
    module.name = name
    module.address = address
    module.mac = mac
    val moduleApi: ModuleApi = retrofit.create(ModuleApi::class.java)
    val call: Call<ListWrapper<Module>> = moduleApi.updateModule(module.id!!, module)
    call.enqueue(this)
  }

  override fun deleteModule(id: String) {
    val moduleApi: ModuleApi = retrofit.create(ModuleApi::class.java)
    val call: Call<Any> = moduleApi.deleteModule(id)
    call.enqueue(object : Callback<Any> {
      override fun onResponse(call: Call<Any>, response: Response<Any>) {
        if (response.isSuccessful) {
          loadModules()
        } else {
          showErrorDialog(Exception("Fehler: ${response.code()}"), false)
        }
      }
      override fun onFailure(call: Call<Any>, t: Throwable) {
        if (t !is EOFException) {
          val e = Exception(t)
          showErrorDialog(e, true)
        }
      }
    })
  }

  override
  fun onResponse(call: Call<ListWrapper<Module>>, response: Response<ListWrapper<Module>>) {
    if (response.isSuccessful) {
      modules = response.body()!!
      moduleAdapter.refresh(modules)
    } else {
      tvResourcesNotFound.isVisible = true
    }

    if (response.body() == null) {
      tvResourcesNotFound.isVisible = true
    }
    loadingSpinner.isVisible = false
  }

  override fun onFailure(call: Call<ListWrapper<Module>>, t: Throwable) {
    val e = Exception(t)
    showErrorDialog(e, true)
    loadingSpinner.isVisible = false
    tvResourcesNotFound.isVisible = true
  }

  private fun showErrorDialog(e: Exception, showTrace: Boolean) {
    val errorDialog = ErrorDialog(e, showTrace)
    errorDialog.show(supportFragmentManager, "errorDialog")
  }
}