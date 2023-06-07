package me.meenagopal24.wallpapers

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.meenagopal24.wallpapers.UI.HomeFragment
import me.meenagopal24.wallpapers.UI.PreviewFragment
import me.meenagopal24.wallpapers.UI.StaggeredFragment
import java.io.IOException


class MainActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        );
        supportFragmentManager.beginTransaction().disallowAddToBackStack()
            .add(R.id.main_layout, HomeFragment()).commit()
        bottomNav = findViewById(R.id.bottom_nav_bar)


    }
}