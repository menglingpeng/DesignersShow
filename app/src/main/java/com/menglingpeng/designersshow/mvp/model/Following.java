package com.menglingpeng.designersshow.mvp.model;

/**
 * Created by mengdroid on 2017/12/4.
 */

public class Following {
    private int id;

    private String created_at;

    private User followee;

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

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }
}
