package me.meenagopal24.wallpapers.utils;

import android.os.Parcelable;

import me.meenagopal24.wallpapers.UI.CategoryFragment;
import me.meenagopal24.wallpapers.UI.FavouriteFragment;
import me.meenagopal24.wallpapers.UI.HomeFragment;
import me.meenagopal24.wallpapers.UI.PreviewFragment;
import me.meenagopal24.wallpapers.UI.SettingsFragment;
import me.meenagopal24.wallpapers.UI.ConnectivityFragment;

public class Constants {
    public final static String BASE_URL = "https://wallpaper-server.vercel.app";
    public static final String HOME_FRAGMENT = HomeFragment.class.getName();
    public static final String CATEGORY_FRAGMENT = CategoryFragment.class.getName();
    public static final String SETTING_FRAGMENT = SettingsFragment.class.getName();
    public static final String FAVOURITE_FRAGMENT = FavouriteFragment.class.getName();
    public static final String PREVIEW_FRAGMENT = PreviewFragment.class.getName();
    public static final String CONNECTIVITY_FRAGMENT = ConnectivityFragment.class.getName();
    public static Parcelable recyclerState = null;
}
