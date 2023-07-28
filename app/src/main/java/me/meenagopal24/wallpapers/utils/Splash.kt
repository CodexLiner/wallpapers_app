package me.meenagopal24.wallpapers.utils

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import me.meenagopal24.wallpapers.MainActivity
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.databases.AllWallpaperListHelper
import me.meenagopal24.wallpapers.databases.CategoryListHelper
import me.meenagopal24.wallpapers.models.ApiResponseDezky
import me.meenagopal24.wallpapers.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getWallpapers()
        getCategories()
        val handler = Handler()
        handler.postDelayed({ // Start your desired activity here
            val intent = Intent(this@Splash, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }, 1500)
    }

    private fun getWallpapers() {
        val call: Call<ApiResponseDezky> = RetrofitClient.getInstance().api.wallpaper
        call.enqueue(object : Callback<ApiResponseDezky> {
            override fun onResponse(call: Call<ApiResponseDezky>, response: Response<ApiResponseDezky>) {
                try {
                    response.body()
                        ?.let { AllWallpaperListHelper(this@Splash).saveList(it.list) }
                } catch (_: java.lang.Exception) {
                }
            }

            override fun onFailure(call: Call<ApiResponseDezky>, t: Throwable) {}
        })

    }

    private fun getCategories() {
        val call: Call<ApiResponseDezky> = RetrofitClient.getInstance().api.categories
        call.enqueue(object : Callback<ApiResponseDezky> {
            override fun onFailure(call: Call<ApiResponseDezky>, t: Throwable) {}

            override fun onResponse(call: Call<ApiResponseDezky>, response: Response<ApiResponseDezky>) {
                runOnUiThread {
                    CategoryListHelper(this@Splash).saveList(response.body()?.list as ArrayList<ApiResponseDezky.item>)
                }
            }
        })
    }

}