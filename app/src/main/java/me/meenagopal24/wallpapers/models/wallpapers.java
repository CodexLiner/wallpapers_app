package me.meenagopal24.wallpapers.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class wallpapers {
    public static class item implements Parcelable {
        String name;
        String url;

        public item(String name, String url) {
            this.name = name;
            this.url = url;
        }

        protected item(Parcel in) {
            name = in.readString();
            url = in.readString();
        }

        public static final Creator<item> CREATOR = new Creator<item>() {
            @Override
            public item createFromParcel(Parcel in) {
                return new item(in);
            }

            @Override
            public item[] newArray(int size) {
                return new item[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }



        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(name);
            parcel.writeString(url);
        }
    }
    @SerializedName("wallpapers")
    ArrayList<item> list;

    public ArrayList<item> getList() {
        return list;
    }

    public void setList(ArrayList<item> list) {
        this.list = list;
    }
}
