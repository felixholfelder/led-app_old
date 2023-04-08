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
import cloud.holfelder.led.app.model.Module
import cloud.holfelder.led.app.wrapper.ListWrapper
import tej.wifitoolslib.DevicesFinder
import tej.wifitoolslib.interfaces.OnDeviceFindListener
import tej.wifitoolslib.models.DeviceItem

class ModuleActivity : AppCompatActivity() {
  private lateinit var listModule: ListView
  private lateinit var loadingSpinner: ProgressBar
  private lateinit var tvResourcesNotFound: TextView
  private var modules: ListWrapper<Module> = ListWrapper(arrayListOf())
  private lateinit var moduleAdapter: ModuleAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_module)
    listModule = findViewById(R.id.listModule)
    loadingSpinner = findViewById(R.id.loadingSpinner)
    tvResourcesNotFound = findViewById(R.id.tvResourcesNotFound)

    setModuleAdapter()
    scanForModules()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.module_items, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.itemModuleRefresh -> scanForModules()
    }
    return super.onOptionsItemSelected(item)
  }

  private fun setModuleAdapter() {
    moduleAdapter = ModuleAdapter(modules, this, supportFragmentManager)
    listModule.adapter = moduleAdapter
  }

  private fun scanForModules() {
    DevicesFinder(this, object : OnDeviceFindListener {
      override fun onStart() {
        showLoadingSpinner()
        clearList()
      }
      override fun onDeviceFound(deviceItem: DeviceItem?) {}
      override fun onComplete(deviceItems: List<DeviceItem?>?) {
        deviceItems!!.forEachIndexed { index, item ->
          if (item!!.deviceName.contains("ESP")) {
            modules.content.add(Module(index, item.deviceName, item.ipAddress))
            moduleAdapter.refresh(modules)
          }
        }
        if (modules.content.isEmpty()) {
          showNullEntries()
        } else {
          showList()
        }
      }
      override fun onFailed(errorCode: Int) {
        showErrorDialog(Exception(errorCode.toString()), false)
      }
    }).start()
  }

  private fun clearList() {
    modules.content = mutableListOf()
    moduleAdapter.refresh(modules)
  }

  private fun showNullEntries() {
    listModule.isVisible = false
    loadingSpinner.isVisible = false
    tvResourcesNotFound.isVisible = true
  }

  private fun showLoadingSpinner() {
    listModule.isVisible = false
    loadingSpinner.isVisible = true
    tvResourcesNotFound.isVisible = false
  }

  private fun showList() {
    listModule.isVisible = true
    loadingSpinner.isVisible = false
    tvResourcesNotFound.isVisible = false
  }

  private fun showErrorDialog(e: Exception, showTrace: Boolean) {
    val errorDialog = ErrorDialog(e, showTrace)
    errorDialog.show(supportFragmentManager, "errorDialog")
  }
}
