package com.example.stickers.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StickersListDownload implements Parcelable {
    String name;
    String absolutePath;

    public StickersListDownload(String absolutePath, String name) {
        this.name=name;
        this.absolutePath=absolutePath;
    }

    protected StickersListDownload(Parcel in) {
        name = in.readString();
        absolutePath = in.readString();
    }

    public static final Creator<StickersListDownload> CREATOR = new Creator<StickersListDownload>() {
        @Override
        public StickersListDownload createFromParcel(Parcel in) {
            return new StickersListDownload(in);
        }

        @Override
        public StickersListDownload[] newArray(int size) {
            return new StickersListDownload[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.name);
        dest.writeString(this.absolutePath);
    }
}
