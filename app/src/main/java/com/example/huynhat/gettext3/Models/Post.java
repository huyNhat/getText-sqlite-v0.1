package com.example.huynhat.gettext3.Models;

/**
 * Created by huynhat on 2017-11-16.
 */

public class Post {
    private int post_id;
    private String user_email;
    private String image;
    private String bookTitle;
    private String isbn;
    private String courseUsed;
    private String condition;
    private double price;
    //private String dateTime;


    public Post() {

    }

    public Post(int post_id, String user_email, String image, String bookTitle, String isbn, String courseUsed, String condition, double price) {
        this.post_id = post_id;
        this.user_email = user_email;
        this.image = image;
        this.bookTitle = bookTitle;
        this.isbn = isbn;
        this.courseUsed = courseUsed;
        this.condition = condition;
        this.price = price;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCourseUsed() {
        return courseUsed;
    }

    public void setCourseUsed(String courseUsed) {
        this.courseUsed = courseUsed;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id=" + post_id +
                ", user_email='" + user_email + '\'' +
                ", image='" + image + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", isbn='" + isbn + '\'' +
                ", courseUsed='" + courseUsed + '\'' +
                ", condition='" + condition + '\'' +
                ", price=" + price +
                '}';
    }
}
