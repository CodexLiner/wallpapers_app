package me.meenagopal24.wallpapers.utils

import android.app.Activity
import androidx.browser.customtabs.CustomTabsIntent.Builder;
import android.content.Context
import android.net.Uri
import android.view.Window
import android.view.WindowManager
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import me.meenagopal24.wallpapers.R

class Functions(private val m: FragmentManager) {
    fun switchFragment(fragment: String?) {
        val fragmentClass = fragment?.let { Class.forName(it) }
        val constructor = fragmentClass?.getDeclaredConstructor()
        constructor?.isAccessible = true
        val ff = constructor?.newInstance() as Fragment

        if (m.findFragmentByTag(fragment) != null) {
            m.beginTransaction().setCustomAnimations(
                R.anim.slide, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out
            ).replace(
                R.id.main_layout, m.findFragmentByTag(fragment)!!
            ).commit()
        } else m.beginTransaction()
            .setCustomAnimations(R.anim.slide, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .addToBackStack(fragment).replace(R.id.main_layout, ff, fragment).commit()
    }

    companion object {
        fun o(context: Context?, url: String?) {
            val builder: CustomTabsIntent.Builder = Builder()
            builder.setShowTitle(true)
            builder.setUrlBarHidingEnabled(true)
            val intent: CustomTabsIntent = builder.build()
            if (context != null) {
                intent.launchUrl(context, Uri.parse(url))
            }
        }

        public fun windowTrans(activity: Activity, b: Boolean) {
            val window: Window = activity.window
            if (b) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
        }

        fun getDp(dpValue: Int, context: Context): Int {
            val density = context.resources.displayMetrics.density
            return (dpValue * density + 0.5f).toInt()
        }


    }
}