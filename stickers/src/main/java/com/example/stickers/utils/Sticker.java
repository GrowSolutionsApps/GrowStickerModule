package com.example.stickers.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Sticker implements Parcelable {
    public static final Creator<Sticker> CREATOR = new Creator<Sticker>() {
        public Sticker createFromParcel(Parcel parcel) {
            return new Sticker(parcel);
        }

        public Sticker[] newArray(int i) {
            return new Sticker[i];
        }
    };


    public List<String> f1368a;
    public long b;
    public String imageFileName;

    public Sticker(String str, List<String> list) {
        this.imageFileName = str;
        this.f1368a = list;
    }

    public int describeContents() {
        return 0;
    }

    public void setSize(long j) {
        this.b = j;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.imageFileName);
        parcel.writeStringList(this.f1368a);
        parcel.writeLong(this.b);
    }

    public Sticker(Parcel parcel) {
        this.imageFileName = parcel.readString();
        this.f1368a = parcel.createStringArrayList();
        this.b = parcel.readLong();
    }
}
