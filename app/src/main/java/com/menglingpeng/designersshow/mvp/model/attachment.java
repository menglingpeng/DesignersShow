package com.menglingpeng.designersshow.mvp.model;

/**
 * Created by mengdroid on 2017/12/17.
 */

public class attachment {

    private int id;

    private String url;

    private String thumbnail_url;

    private int size;

    private String content_type;

    private int views_count;

    private String created_at;

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public int getSize() {
        return size;
    }

    public String getContent_type() {
        return content_type;
    }

    public int getViews_count() {
        return views_count;
    }

    public String getCreated_at() {
        return created_at;
    }
}
