package me.meenagopal24.wallpapers.MVVM.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.meenagopal24.wallpapers.MVVM.common.SafeApiRequest
import me.meenagopal24.wallpapers.MVVM.home.source.BrowseDefaultRepository
import me.meenagopal24.wallpapers.models.ApiResponseDezky
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: BrowseDefaultRepository) : ViewModel() {
    val recentlyAddedWallpapers: MutableLiveData<ArrayList<ApiResponseDezky.item>> =
        MutableLiveData()
    private val list: ArrayList<ApiResponseDezky.item> = arrayListOf()

    init {
        getWallpapers();
    }

    private fun getWallpapers() {
        SafeApiRequest.safe {
            repo.getRecentWallpapers().list?.let {
                list.addAll(it)
                recentlyAddedWallpapers.postValue(it)
            }
        }
    }

    fun onRefreshWallpapers() {
        getWallpapers()
    }
}