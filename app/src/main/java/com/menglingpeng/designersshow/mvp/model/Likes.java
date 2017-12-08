package com.menglingpeng.designersshow.mvp.model;

/**
 * Created by mengdroid on 2017/11/24.
 */

public class Likes {
    private int id;

    private String created_at;

    private Shot shot;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Shot getShot() {
        return shot;
    }

    public void setShot(Shot shot) {
        this.shot = shot;
    }
}
