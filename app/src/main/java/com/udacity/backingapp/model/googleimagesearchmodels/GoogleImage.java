package com.udacity.backingapp.model.googleimagesearchmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by feder on 31/03/2018.
 */

public class GoogleImage {
    @SerializedName("kind")
    private String kind;

    @SerializedName("title")
    private String title;

    @SerializedName("link")
    private String link;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
