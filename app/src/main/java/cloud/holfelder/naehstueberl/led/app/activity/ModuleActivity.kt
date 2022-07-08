package cloud.holfelder.naehstueberl.led.app.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import cloud.holfelder.naehstueberl.led.app.R
import cloud.holfelder.naehstueberl.led.app.adapter.ModuleAdapter
import cloud.holfelder.naehstueberl.led.app.dialog.ModuleDialog
import cloud.holfelder.naehstueberl.led.app.model.Module
import cloud.holfelder.naehstueberl.led.app.wrapper.ListWrapper

class ModuleActivity : AppCompatActivity(), ModuleDialog.ModuleItemListener {
    private lateinit var listModule: ListView
    private lateinit var modules: ListWrapper<Module>
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

    private fun loadModules() {
        // TODO: reqeust for loading modules
        modules = ListWrapper(listOf(Module(1, "test", "192.168.2.112", "FF:FF:FF:FF:FF:FF")))
    }

    private fun setModuleAdapter() {
        val moduleAdapter = ModuleAdapter(modules, this, supportFragmentManager)
        listModule.adapter = moduleAdapter
    }

    override
    fun createModule(name: String, address: String, mac: String) {
        // TODO: request for creating module
        val module = Module(null, name, address, mac)
    }

    override
    fun updateModule(name: String, address: String, mac: String, pos: Int) {
        val module = modules.content[pos]
        module.name = name
        module.address = address
        module.mac = mac
        // TODO: request for updating module
    }
}