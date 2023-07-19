package me.meenagopal24.wallpapers.utils;

import android.os.Parcelable;

import me.meenagopal24.wallpapers.UI.CategoryFragment;
import me.meenagopal24.wallpapers.UI.FavouriteFragment;
import me.meenagopal24.wallpapers.UI.HomeFragment;
import me.meenagopal24.wallpapers.UI.PreviewFragment;
import me.meenagopal24.wallpapers.UI.SettingsFragment;
import me.meenagopal24.wallpapers.UI.ConnectivityFragment;

public class Constants {
    //    public final static String BASE_URL = "https://wallpaper-server.vercel.app";
    public final static String BASE_URL = "https://me.meenagopal24.me";
    public final static String BASE_URL_CONTENT = "https://meenagopal24.me/wallpaperapi/uploads";
    public final static String BASE_URL_IMAGE = BASE_URL_CONTENT + "/";

    public final static String BASE_URL_THUMBNAIL = BASE_URL_CONTENT + "/thumbnails/";
    public final static String BASE_URL_CATEGORY = BASE_URL_CONTENT + "/category_thumbnails/";
    public static final String HOME_FRAGMENT = HomeFragment.class.getName();
    public static final String CATEGORY_FRAGMENT = CategoryFragment.class.getName();
    public static final String SETTING_FRAGMENT = SettingsFragment.class.getName();
    public static final String FAVOURITE_FRAGMENT = FavouriteFragment.class.getName();
    public static final String PREVIEW_FRAGMENT = PreviewFragment.class.getName();
    public static final String CONNECTIVITY_FRAGMENT = ConnectivityFragment.class.getName();
    public static Parcelable recyclerState = null;
    public static final int VIEW_TYPE_CONTENT = 0;
    public static final int VIEW_TYPE_AD = 1;
//    public static final String NATIVE_AD_ID = "ca-app-pub-3940256099942544/2247696110";
    public static final String NATIVE_AD_ID = "ca-app-pub-6831834965817563/9582093237"; // production

}
