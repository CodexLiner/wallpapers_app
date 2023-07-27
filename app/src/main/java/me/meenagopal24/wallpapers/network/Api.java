package me.meenagopal24.wallpapers.network;

import me.meenagopal24.wallpapers.models.ApiResponseDezky;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("/wallpaper")
    Call<ApiResponseDezky> getWallpaper();

    @GET("/wallpaper/get")
    Call<ApiResponseDezky> getWallpaperByCategory(@Query("category") String category);

    @GET("/category")
    Call<ApiResponseDezky> getCategories();
}
