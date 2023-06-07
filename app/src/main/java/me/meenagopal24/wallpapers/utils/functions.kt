package me.meenagopal24.wallpapers.utils

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.UI.SettingsFragment

class functions(private val m: FragmentManager) {
    public fun switchFragment(fragment: String?) {
        val fragmentClass = fragment?.let { Class.forName(it) }
        val constructor = fragmentClass?.getDeclaredConstructor()
        constructor?.isAccessible = true
        val ff = constructor?.newInstance() as Fragment

        if (m.findFragmentByTag(fragment) != null) {
            m.beginTransaction().replace(
                R.id.main_layout,
                m.findFragmentByTag(fragment)!!
            ).commit()
        } else m.beginTransaction().addToBackStack(fragment)
            .replace(R.id.main_layout, ff, fragment).commit()
    }
}