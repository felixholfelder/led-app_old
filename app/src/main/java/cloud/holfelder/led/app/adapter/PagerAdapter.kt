package cloud.holfelder.led.app.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import cloud.holfelder.led.app.tabs.ColorFragment
import cloud.holfelder.led.app.tabs.ModeFragment

class PagerAdapter(var context: Context, fm: FragmentManager, var tabs: Int) :
  FragmentStatePagerAdapter(fm) {
  private val COLOR_TAB = 0
  private val MODE_TAB = 1

  override fun getCount() = tabs

  override
  fun getItem(position: Int): Fragment {
    return when (position) {
      COLOR_TAB -> ColorFragment()
      MODE_TAB -> ModeFragment()
      else -> getItem(position)
    }
  }
}