package me.meenagopal24.wallpapers.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class wallpapers {
    public static class item implements Parcelable {
        String name;
        String url;
        @NotNull
        String uuid;

        @NotNull
        public String getUuid() {
            return uuid;
        }

        public void setUuid(@NotNull String uuid) {
            this.uuid = uuid;
        }

        public item(String name, String url, @NotNull String uuid) {
            this.name = name;
            this.url = url;
            this.uuid = uuid;
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
