package com.example.stickers.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class StickerPack implements Parcelable {
    public static final Creator<StickerPack> CREATOR = new Creator<StickerPack>() {
        public StickerPack createFromParcel(Parcel parcel) {
            return new StickerPack(parcel);
        }

        public StickerPack[] newArray(int i) {
            return new StickerPack[i];
        }
    };


    public String f1369a;
    public String b;
    public String identifier;
    public boolean isWhitelisted;
    public final String licenseAgreementWebsite;
    public String name;
    public final String privacyPolicyWebsite;
    public String publisher;
    public final String publisherEmail;
    public final String publisherWebsite;
    public List<Sticker> stickers;
    public long totalSize;
    public String trayImageFile;

    public StickerPack(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        this.identifier = str;
        this.name = str2;
        this.publisher = str3;
        this.trayImageFile = str4;
        this.publisherEmail = str5;
        this.publisherWebsite = str6;
        this.privacyPolicyWebsite = str7;
        this.licenseAgreementWebsite = str8;
    }

    public int describeContents() {
        return 0;
    }

    public List<Sticker> getStickers() {
        return this.stickers;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public void setAndroidPlayStoreLink(String str) {
        this.b = str;
    }

    public void setIosAppStoreLink(String str) {
        this.f1369a = str;
    }

    public void setStickers(List<Sticker> list) {
        this.stickers = list;
        this.totalSize = 0;
        for (Sticker sticker : list) {
            this.totalSize += sticker.b;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.identifier);
        parcel.writeString(this.name);
        parcel.writeString(this.publisher);
        parcel.writeString(this.trayImageFile);
        parcel.writeString(this.publisherEmail);
        parcel.writeString(this.publisherWebsite);
        parcel.writeString(this.privacyPolicyWebsite);
        parcel.writeString(this.licenseAgreementWebsite);
        parcel.writeString(this.f1369a);
        parcel.writeTypedList(this.stickers);
        parcel.writeLong(this.totalSize);
        parcel.writeString(this.b);
        parcel.writeByte(this.isWhitelisted ? (byte) 1 : 0);
    }

    public StickerPack(Parcel parcel) {
        this.identifier = parcel.readString();
        this.name = parcel.readString();
        this.publisher = parcel.readString();
        this.trayImageFile = parcel.readString();
        this.publisherEmail = parcel.readString();
        this.publisherWebsite = parcel.readString();
        this.privacyPolicyWebsite = parcel.readString();
        this.licenseAgreementWebsite = parcel.readString();
        this.f1369a = parcel.readString();
        this.stickers = parcel.createTypedArrayList(Sticker.CREATOR);
        this.totalSize = parcel.readLong();
        this.b = parcel.readString();
        this.isWhitelisted = parcel.readByte() != 0;
    }
}
