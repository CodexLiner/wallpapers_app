package me.meenagopal24.wallpapers

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import me.meenagopal24.wallpapers.UI.HomeFragment
import me.meenagopal24.wallpapers.services.InternetConnectivityReceiver
import me.meenagopal24.wallpapers.utils.Constants.*
import me.meenagopal24.wallpapers.utils.Functions
import java.util.*
import android.net.Uri


class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1
    lateinit var bottomNav: BottomNavigationView
    private var isHome = false
    lateinit var appUpdateManager: AppUpdateManager
    private val MY_REQUEST_CODE = 17326
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this) {}
        checkNotificationPermission();
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext);
        checkForUpdates()
        val connectivityReceiver = InternetConnectivityReceiver(supportFragmentManager, this)
        registerReceiver(
            connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .disallowAddToBackStack()
            .add(R.id.main_layout, HomeFragment(), HOME_FRAGMENT).commit()



        bottomNav = findViewById(R.id.bottom_nav_bar)

        for (i in 0 until bottomNav.menu.size()) {
            bottomNav.menu.getItem(i).title = ""
        }
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

    private fun checkForUpdates() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
               try {
                   appUpdateManager.startUpdateFlowForResult(
                       appUpdateInfo,
                       AppUpdateType.FLEXIBLE,
                       this@MainActivity,
                       MY_REQUEST_CODE
                   )
               }catch (e:Exception){
                   Log.d("TAG", "onActivityResult E: $e ")
               }
            }
        }
        appUpdateManager.registerListener(listener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) { }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        requestNotificationPermission()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(android.Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_POST_NOTIFICATIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate()
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
    }

    fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.main_layout),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.colorMain))
            show()
        }
    }

    override fun onStop() {
        super.onStop()
        appUpdateManager.unregisterListener(listener)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (isHome) finish()
        if (checkOnTop(CONNECTIVITY_FRAGMENT)) {
            finish()
        }
        if (checkOnTop(CATEGORY_FRAGMENT)
            || checkOnTop(FAVOURITE_FRAGMENT)
            || checkOnTop(
                SETTING_FRAGMENT
            )
        ) {
            Log.d(
                "TAG", "onBackPressed: ${
                    supportFragmentManager.getBackStackEntryAt(
                        supportFragmentManager.backStackEntryCount - 1
                    ).name
                }"
            )
            Functions(supportFragmentManager).switchFragment(HOME_FRAGMENT)
            isHome = true
            bottomNav.selectedItemId = R.id.home_bottom
        } else {
            if (checkOnTop(PREVIEW_FRAGMENT) || checkOnTop("old_preview_fav")
            ) {
                bottomModify(true)
                Functions.windowTrans(this, false)
            }
            super.onBackPressed()
        }
    }

    fun bottomModify(boolean: Boolean) {
        val frameLayout = findViewById<FrameLayout>(R.id.frame_demo)
        if (boolean) {
            val layoutParams = frameLayout.layoutParams
            layoutParams.height = Functions.getDp(60, this)
            frameLayout.visibility = View.VISIBLE
            bottomNav.setPadding(0, 0, 0, 0)
        } else {
            frameLayout.visibility = View.GONE
        }
    }

    private fun checkOnTop(fragment: String): Boolean {
        return if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.getBackStackEntryAt(
                supportFragmentManager.backStackEntryCount - 1
            ).name == fragment
        else false
    }
}