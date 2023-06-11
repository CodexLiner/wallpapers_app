package me.meenagopal24.wallpapers.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class categories {
    public static class item {
        String name;

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

        String url;
    }
    @SerializedName("categories")
    List<wallpapers.item> list;

    public List<wallpapers.item> getList() {
        return list;
    }

    public void setList(List<wallpapers.item> list) {
        this.list = list;
    }
}
