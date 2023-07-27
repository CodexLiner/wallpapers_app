package me.meenagopal24.wallpapers.MVVM.Category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import me.meenagopal24.wallpapers.MVVM.home.source.BrowseDefaultRepository

class CategoryModelFactory(val repo: BrowseDefaultRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CategoryViewModel(repo) as T
    }

}