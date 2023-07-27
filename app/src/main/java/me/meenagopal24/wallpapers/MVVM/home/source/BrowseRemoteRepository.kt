package me.meenagopal24.wallpapers.MVVM.home.source

import me.meenagopal24.wallpapers.models.ApiResponseDezky
import retrofit2.Retrofit
import retrofit2.http.GET
import java.io.IOException
import javax.inject.Inject

class BrowseRemoteRepository @Inject constructor(retrofit: Retrofit) {
    private val service = retrofit.create(Api::class.java)

    suspend fun getWallpapers(): ApiResponseDezky {
        return try {
            service.getWallpapers()
        } catch (_: Exception) {
            ApiResponseDezky()
        }
    }

    suspend fun getCategories(): ApiResponseDezky {
        return try {
            service.getCategories()
        } catch (e: IOException) {
            ApiResponseDezky()
        }
    }

    interface Api {
        @GET("/wallpaper")
        suspend fun getWallpapers(): ApiResponseDezky

        @GET("/category")
        suspend fun getCategories(): ApiResponseDezky
    }
}