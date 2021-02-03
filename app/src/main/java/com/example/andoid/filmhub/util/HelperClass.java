package com.example.andoid.filmhub.util;

public class HelperClass {

    // Variables
    private int mImage;
    private String mTitle;
    private String mDate;

    // Constructor
    public HelperClass(int mImage, String mTitle, String mDate) {
        this.mImage = mImage;
        this.mTitle = mTitle;
        this.mDate = mDate;
    }

    public HelperClass(int mImage, String mTitle) {
        this.mImage = mImage;
        this.mTitle = mTitle;
    }

    // Methods
    public int getmImage() {
        return mImage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDate() {
        return mDate;
    }
}
