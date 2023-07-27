package me.meenagopal24.wallpapers.MVVM.home.source

import kotlinx.coroutines.flow.Flow
import me.meenagopal24.wallpapers.models.ApiResponseDezky


interface BrowseDataSource {
    suspend fun getRecentWallpapers(): ApiResponseDezky
    suspend fun getWallpapersCategoryWise(category: String): ApiResponseDezky
    suspend fun getAllCategories() : ApiResponseDezky
}