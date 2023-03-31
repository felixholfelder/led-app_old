package cloud.holfelder.led.app.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.adapter.ModuleAdapter
import cloud.holfelder.led.app.model.Module
import cloud.holfelder.led.app.wrapper.ListWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModuleActivity : AppCompatActivity() {
  private lateinit var listModule: ListView
  private lateinit var loadingSpinner: ProgressBar
  private lateinit var tvResourcesNotFound: TextView
  private var modules: ListWrapper<Module> = ListWrapper(arrayListOf())
  private lateinit var moduleAdapter: ModuleAdapter
  private lateinit var wifiManager: WifiManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_module)
    listModule = findViewById(R.id.listModule)
    loadingSpinner = findViewById(R.id.loadingSpinner)
    tvResourcesNotFound = findViewById(R.id.tvResourcesNotFound)
    wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    scanForModules()
    setModuleAdapter()
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
    listModule.isVisible = false
    loadingSpinner.isVisible = true
    tvResourcesNotFound.isVisible = false
    CoroutineScope(Dispatchers.Main).launch {
      registerReceiver(wifiReciever, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
      if (!wifiManager.isWifiEnabled) {
        Toast.makeText(this@ModuleActivity, "Keine Internetverbindung!", Toast.LENGTH_LONG).show()
        wifiManager.isWifiEnabled = true
      }
      wifiManager.startScan()
    }
  }

  var wifiReciever = object: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      unregisterReceiver(this)
      for (device in wifiManager.scanResults) {
        println("done 1")
        val module = Module(null, device.SSID, "123", "123")
        modules.content = listOf(module)
        moduleAdapter.refresh(modules)
      }
      println("done 2")
    }
  }
}