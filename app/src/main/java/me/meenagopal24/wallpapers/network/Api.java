package me.meenagopal24.wallpapers.network;

import me.meenagopal24.wallpapers.models.categories;
import me.meenagopal24.wallpapers.models.wallpapers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("/wallpaper")
    Call<wallpapers> getWallpaper();

    @GET("/wallpaper")
    Call<wallpapers> getWallpaperByCategory(@Query("category") String category);

    @GET("/category")
    Call<categories> getCategories();
}
