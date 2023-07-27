package me.meenagopal24.wallpapers.MVVM.common

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.meenagopal24.wallpapers.MVVM.Category.CategoryModelFactory
import me.meenagopal24.wallpapers.MVVM.di.AdapterModule
import me.meenagopal24.wallpapers.MVVM.home.source.BrowseDefaultRepository
import me.meenagopal24.wallpapers.MVVM.home.source.BrowseRemoteRepository
import me.meenagopal24.wallpapers.MVVM.home.HomeViewModelFactory
import me.meenagopal24.wallpapers.MVVM.home.HomeFragment
import me.meenagopal24.wallpapers.databases.DatabaseGetter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofitClient(): Retrofit = RetrofitClientV2.invoke()

    @Provides
    @Singleton
    fun provideDatabaseGetter(@ApplicationContext context: Context): DatabaseGetter {
        return DatabaseGetter(context)
    }

    @Provides
    @Singleton
    fun provideBrowseRemoteRepository(retrofit: Retrofit): BrowseRemoteRepository =
        BrowseRemoteRepository(retrofit)

    @Provides
    @Singleton
    fun provideBrowseDefaultRepository(
        repo: BrowseRemoteRepository,
        databaseGetter: DatabaseGetter,
    ): BrowseDefaultRepository =
        BrowseDefaultRepository(repo, databaseGetter)

    @Provides
    @Singleton
    fun provideBrowseViewModelFactory(repo: BrowseDefaultRepository): HomeViewModelFactory =
        HomeViewModelFactory(repo)

    @Provides
    @Singleton
    fun provideCategoryViewModelFactory(repo: BrowseDefaultRepository): CategoryModelFactory =
        CategoryModelFactory(repo)


    @Component(modules = [AdapterModule::class])
    @Singleton
    interface AppComponent {
        fun inject(homeFragment: HomeFragment)
    }
}
