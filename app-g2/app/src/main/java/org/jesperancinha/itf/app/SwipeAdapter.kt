package org.jesperancinha.itf.app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 *
 */
class SwipeAdapter(fm: FragmentManager?, private val fragments: Array<Fragment>) :
    FragmentStatePagerAdapter(
        fm!!
    ) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}