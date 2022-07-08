package cloud.holfelder.naehstueberl.led.app.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import cloud.holfelder.naehstueberl.led.app.fragment.ColorFragment
import cloud.holfelder.naehstueberl.led.app.fragment.ModeFragment

class PagerAdapter(var context: Context, fm: FragmentManager, var tabs: Int) : FragmentStatePagerAdapter(fm) {
    override fun getCount() = tabs

    override
    fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ColorFragment()
            1 -> ModeFragment()
            else -> getItem(position)
        }
    }
}