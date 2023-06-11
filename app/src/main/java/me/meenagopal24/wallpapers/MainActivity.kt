package me.meenagopal24.wallpapers

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.meenagopal24.wallpapers.UI.FavouriteFragment
import me.meenagopal24.wallpapers.UI.HomeFragment
import me.meenagopal24.wallpapers.utils.Constants.*
import me.meenagopal24.wallpapers.utils.Functions


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
                    Functions(supportFragmentManager).switchFragment(HOME_FRAGMENT)
                    true
                }
                R.id.cat_bottom -> {
                    Functions(supportFragmentManager).switchFragment(CATEGORY_FRAGMENT)
                    true
                }
                R.id.fav_bottom -> {
                    Functions(supportFragmentManager).switchFragment(FAVOURITE_FRAGMENT)
                    true
                }
                R.id.settings_bottom -> {
                    Functions(supportFragmentManager).switchFragment(SETTING_FRAGMENT)
                    true
                }
                else -> {
                    false
                }
            }
        }

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
        } else {
            if (supportFragmentManager.backStackEntryCount > 0 && supportFragmentManager.getBackStackEntryAt(
                    supportFragmentManager.backStackEntryCount - 1
                ).name == PREVIEW_FRAGMENT
            ) {
                bottomModify(true)
                Functions.windowTrans(this, false)
            }
            super.onBackPressed()
        }


    }

    fun bottomModify(boolean: Boolean) {
        val frameLayout = findViewById<FrameLayout>(R.id.frame_demo)
        if (boolean){
            val layoutParams = frameLayout.layoutParams
            layoutParams.height = Functions.getDp(60 , this)
            frameLayout.visibility = View.VISIBLE
            bottomNav.setPadding(0 , 0 ,0 , 0)


        }else{
            frameLayout.visibility = View.GONE
        }
    }

    private fun checkOnTop(fragment: String): Boolean {
        return if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name == fragment
        else false
    }
}