package me.meenagopal24.wallpapers.MVVM.home.source

import me.meenagopal24.wallpapers.databases.DatabaseGetter
import me.meenagopal24.wallpapers.models.ApiResponseDezky
import timber.log.Timber
import javax.inject.Inject

class BrowseDefaultRepository @Inject constructor(
    private val remoteRepo: BrowseRemoteRepository,
    private val databaseGetter: DatabaseGetter,
) : BrowseDataSource {
    override suspend fun getRecentWallpapers(): ApiResponseDezky {
        databaseGetter.getWallpapers().saveList(remoteRepo.getWallpapers(   ).list)
        return ApiResponseDezky(databaseGetter.getWallpapers().getList())
    }

    override suspend fun getWallpapersCategoryWise(category: String): ApiResponseDezky {
        return remoteRepo.getCategories()
    }

    override suspend fun getAllCategories(): ApiResponseDezky {
        databaseGetter.getCategories().saveList(remoteRepo.getCategories().list)
        return ApiResponseDezky(databaseGetter.getCategories().getList())
    }
}