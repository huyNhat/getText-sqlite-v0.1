package com.example.huynhat.gettext3.Models;

/**
 * Created by 300239275 on 11/26/2017.
 */

public class WishList {
    private int id;
    private int post_id;
    private String user_email;

    public WishList(){

    }

    public WishList(int id, int post_id, String user_email) {
        this.id = id;
        this.post_id = post_id;
        this.user_email = user_email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    @Override
    public String toString() {
        return "WishList{" +
                "id=" + id +
                ", post_id=" + post_id +
                ", user_email='" + user_email + '\'' +
                '}';
    }
}
