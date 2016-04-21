package com.example.witquiz.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{

    private int id;
    private String name;

    public Category() {
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public static final Parcelable.Creator<Category> CREATOR =
            new Parcelable.Creator<Category>() {

                public Category createFromParcel(Parcel in) {
                    return new Category(in);
                }

                public Category[] newArray(int size) {
                    return new Category[size];
                }
            };
}
