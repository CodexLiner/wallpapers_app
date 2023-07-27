package me.meenagopal24.wallpapers.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ApiResponseDezky {
    public static class item implements Parcelable {
        String name;
        String image;
        @NotNull
        String uuid;
        String thumbnail, category;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        @NotNull
        public String getUuid() {
            return uuid;
        }

        public void setUuid(@NotNull String uuid) {
            this.uuid = uuid;
        }

        public item(String name, String image, @NotNull String uuid) {
            this.name = name;
            this.image = image;
            this.uuid = uuid;
        }

        public item(String name, String image, @NotNull String uuid, String thumbnail, String category) {
            this.name = name;
            this.image = image;
            this.uuid = uuid;
            this.thumbnail = thumbnail;
            this.category = category;
        }

        protected item(Parcel in) {
            name = in.readString();
            image = in.readString();
            uuid = in.readString();
            thumbnail = in.readString();
            category = in.readString();
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(name);
            parcel.writeString(image);
            parcel.writeString(uuid);
            parcel.writeString(thumbnail);
            parcel.writeString(category);
        }

        @Override
        public String toString() {
            return "item{" +
                    "name='" + name + '\'' +
                    ", image='" + image + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", category='" + category + '\'' +
                    '}';
        }
    }

    @SerializedName("result")
    ArrayList<item> list;

    public ArrayList<item> getList() {
        return list;
    }

    public void setList(ArrayList<item> list) {
        this.list = list;
    }

    public ApiResponseDezky(ArrayList<item> list) {
        this.list = list;
    }

    public ApiResponseDezky() {}
}
