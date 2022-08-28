package cloud.holfelder.led.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import cloud.holfelder.led.app.R
import cloud.holfelder.led.app.adapter.PagerAdapter
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        tabLayout = findViewById(R.id.tabview)
        viewPager = findViewById(R.id.pager)

        createTabs()
        setAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.color_activity_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemModule -> openModules()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.home_colors)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.home_modus)))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
    }

    private fun openModules() {
        startActivity(Intent(this, ModuleActivity::class.java))
    }

    private fun setAdapter() {
        val adapter = PagerAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}