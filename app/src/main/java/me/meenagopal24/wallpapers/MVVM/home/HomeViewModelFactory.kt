package me.meenagopal24.wallpapers.MVVM.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import me.meenagopal24.wallpapers.MVVM.home.source.BrowseDefaultRepository

@Suppress("UNREACHABLE_CODE")
class HomeViewModelFactory(private val repo: BrowseDefaultRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return return HomeViewModel(repo) as T
    }
}
