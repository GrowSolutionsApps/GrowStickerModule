package com.example.stickers.identities;




import com.example.stickers.utils.StickerPack;

import java.util.ArrayList;
import java.util.List;

public class StickerPacksContainer {
    public String androidPlayStoreLink;
    public String iosAppStoreLink;
    public List<StickerPack> stickerPacks;

    public StickerPacksContainer() {
        this.stickerPacks = new ArrayList();
    }

    public void addStickerPack(StickerPack stickerPack) {
        this.stickerPacks.add(stickerPack);
    }

    public String getAndroidPlayStoreLink() {
        return this.androidPlayStoreLink;
    }

    public String getIosAppStoreLink() {
        return this.iosAppStoreLink;
    }

    public StickerPack getStickerPack(int i) {
        return (StickerPack) this.stickerPacks.get(i);
    }

    public List<StickerPack> getStickerPacks() {
        return this.stickerPacks;
    }

    public StickerPack removeStickerPack(int i) {
        return (StickerPack) this.stickerPacks.remove(i);
    }

    public void setAndroidPlayStoreLink(String str) {
        this.androidPlayStoreLink = str;
    }

    public void setIosAppStoreLink(String str) {
        this.iosAppStoreLink = str;
    }

    public void setStickerPacks(List<StickerPack> list) {
        this.stickerPacks = list;
    }

    public StickerPacksContainer(String str, String str2, List<StickerPack> list) {
        this.androidPlayStoreLink = str;
        this.iosAppStoreLink = str2;
        this.stickerPacks = list;
    }
}
