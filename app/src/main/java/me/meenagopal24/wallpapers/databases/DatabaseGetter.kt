package me.meenagopal24.wallpapers.databases

import android.content.Context
import javax.inject.Inject

class DatabaseGetter @Inject constructor(private val context: Context) {
    public fun getWallpapers(): AllWallpaperListHelper {
        return AllWallpaperListHelper(context)
    }

    public fun getCategories(): CategoryListHelper {
        return CategoryListHelper(context)
    }

    public fun getFavourite(): FavouriteWallpaperHelper {
        return FavouriteWallpaperHelper(context)
    }

    public fun getWallpaper(): WallpaperListHelper {
        return WallpaperListHelper(context)
    }



}