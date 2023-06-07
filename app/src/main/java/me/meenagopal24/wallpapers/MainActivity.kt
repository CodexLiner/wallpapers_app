package me.meenagopal24.wallpapers

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.meenagopal24.wallpapers.UI.CategoryFragment
import me.meenagopal24.wallpapers.UI.FavouriteFragment
import me.meenagopal24.wallpapers.UI.HomeFragment
import me.meenagopal24.wallpapers.UI.SettingsFragment
import me.meenagopal24.wallpapers.utils.Constants
import me.meenagopal24.wallpapers.utils.Constants.*
import me.meenagopal24.wallpapers.utils.functions


class MainActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().disallowAddToBackStack()
            .add(R.id.main_layout, HomeFragment(), HOME_FRAGMENT).commit()
        bottomNav = findViewById(R.id.bottom_nav_bar)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_bottom -> {
                    functions(supportFragmentManager).switchFragment(HOME_FRAGMENT)
                    true
                }
                R.id.cat_bottom -> {
                    functions(supportFragmentManager).switchFragment(CATEGORY_FRAGMENT)
                    true
                }
                R.id.fav_bottom -> {
                    functions(supportFragmentManager).switchFragment(FAVOURITE_FRAGMENT)
                    true
                }
                R.id.settings_bottom -> {
                    switchFragment(SETTING_FRAGMENT)
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

    private fun switchFragment(fragment: String?) {
        Log.d("TAG", "switchFragment: $fragment")
        val fragmentClass = fragment?.let { Class.forName(it) }
        val constructor = fragmentClass?.getDeclaredConstructor()
        constructor?.isAccessible = true
        val ff = constructor?.newInstance() as Fragment

        if (supportFragmentManager.findFragmentByTag(fragment) != null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.main_layout,
                supportFragmentManager.findFragmentByTag(fragment)!!
            ).commit()
        } else supportFragmentManager.beginTransaction().addToBackStack(fragment)
            .replace(R.id.main_layout, ff, fragment).commit()
    }

    override fun onBackPressed() {
        if (checkOnTop(CATEGORY_FRAGMENT) || checkOnTop(FAVOURITE_FRAGMENT) || checkOnTop(
                SETTING_FRAGMENT
            )
        ) {
            supportFragmentManager.beginTransaction().replace(
                R.id.main_layout,
                supportFragmentManager.findFragmentByTag(HOME_FRAGMENT)!!
            ).commit()
            bottomNav.selectedItemId = R.id.home_bottom
            Toast.makeText(this, "backPressed", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }


    }

    private fun checkOnTop(fragment: String): Boolean {
        return if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name == fragment
        else false
    }
}