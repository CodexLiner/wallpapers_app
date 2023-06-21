package me.meenagopal24.wallpapers.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class categories {
    public static class item {
        String name;
        String id;
        String thumbnail;
        String uuid;
        String image;
        @SerializedName("name")
        String category;
        String url;
        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

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


    }

    @SerializedName("result")
    List<wallpapers.item> list;

    public List<wallpapers.item> getList() {
        return list;
    }

    public void setList(List<wallpapers.item> list) {
        this.list = list;
    }
}
