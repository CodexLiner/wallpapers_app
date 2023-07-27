package me.meenagopal24.wallpapers.MVVM.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import me.meenagopal24.wallpapers.MVVM.home.HomeFragment
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.ApiResponseDezky

@Module
@InstallIn(ViewModelComponent::class)
object AdapterModule {
    @Provides
    fun provideStaggeredList(): MutableList<ApiResponseDezky.item> {
        // Provide the list of items to be displayed in the RecyclerView
        // You can fetch the data from your data source (e.g., API, database, etc.)
        return mutableListOf()
    }

    @Provides
    fun provideChangeListener(activity: HomeFragment): ChangeInterface {
        // Provide the ChangeInterface implementation for handling item clicks in the RecyclerView
        // You can implement the ChangeInterface as needed
        return activity
    }

    @Provides
    fun provideStaggeredAdapter(
        list: MutableList<ApiResponseDezky.item>,
        change: ChangeInterface
    ): StaggeredAdapter {
        return StaggeredAdapter(list, change)
    }
}