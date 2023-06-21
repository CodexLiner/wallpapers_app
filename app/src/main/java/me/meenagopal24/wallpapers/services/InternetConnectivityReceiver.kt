package me.meenagopal24.wallpapers.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import androidx.fragment.app.FragmentManager
import me.meenagopal24.wallpapers.MainActivity
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.UI.ConnectivityFragment
import me.meenagopal24.wallpapers.utils.Constants.CONNECTIVITY_FRAGMENT

class InternetConnectivityReceiver(private val supportFragmentManager: FragmentManager, private val activity: MainActivity) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if ((networkInfo != null) && networkInfo.isConnected) {
            // Internet is available
            doFragmentRemove()
        } else {
            // Internet is not available
            doFragment()
        }
    }

    private fun doFragmentRemove() {
        val fragment = supportFragmentManager.findFragmentByTag(CONNECTIVITY_FRAGMENT)
        try {
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
                activity.bottomNav.visibility = View.VISIBLE

            }
        }catch (_ : Exception){}
    }

    private fun doFragment() {
       try {
           supportFragmentManager.beginTransaction()
               .add(R.id.main_layout, ConnectivityFragment(), CONNECTIVITY_FRAGMENT).commit()
           activity.bottomNav.visibility = View.GONE
       }catch (_:java.lang.Exception){}
    }
}