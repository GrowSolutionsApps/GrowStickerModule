package com.example.stickers.utils;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class  LeakGuardHandlerWrapper<T> extends Handler {
    private final WeakReference<T> mOwnerInstanceRef;

    public LeakGuardHandlerWrapper(@Nonnull T t) {
        this(t, Looper.myLooper());
    }

    public LeakGuardHandlerWrapper(@Nonnull T t, Looper looper) {
        super(looper);
        this.mOwnerInstanceRef = new WeakReference<>(t);
    }

    @Nullable
    public T getOwnerInstance() {
        return this.mOwnerInstanceRef.get();
    }
}