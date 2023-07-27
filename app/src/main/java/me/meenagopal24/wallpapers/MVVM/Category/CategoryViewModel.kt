package me.meenagopal24.wallpapers.MVVM.Category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import me.meenagopal24.wallpapers.MVVM.common.SafeApiRequest
import me.meenagopal24.wallpapers.MVVM.home.source.BrowseDefaultRepository
import me.meenagopal24.wallpapers.models.ApiResponseDezky
import javax.inject.Inject
@HiltViewModel
class CategoryViewModel @Inject constructor(private val repo: BrowseDefaultRepository) : ViewModel() {
    val allCategoryData: MutableLiveData<ArrayList<ApiResponseDezky.item>> =
        MutableLiveData()

    init {
        getCategories()
    }


    private fun getCategories() {
        SafeApiRequest.safe {
            repo.getAllCategories().list?.let {
                allCategoryData.postValue(it)
            }
        }
    }

}