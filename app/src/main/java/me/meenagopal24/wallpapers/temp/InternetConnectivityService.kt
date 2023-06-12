package me.meenagopal24.wallpapers.temp

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.IBinder
import androidx.fragment.app.FragmentManager

class nternetConnectivityService : Service() {
    private lateinit var connectivityReceiver: InternetConnectivityReceiver
    private lateinit var fm : FragmentManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
//        connectivityReceiver = InternetConnectivityReceiver(supportFragmentManager)
//        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }
}
