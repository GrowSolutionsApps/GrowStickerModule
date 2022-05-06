package com.example.stickers.view;

import android.animation.FloatEvaluator;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import androidx.fragment.app.Fragment;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class TouchA {
    public static float a(float f, float f2, float f3, float f4) {
        return ((f - f2) * f3) + f4;
    }

    public static float a(float f, FloatEvaluator floatEvaluator, float f2, Double d) {
        return floatEvaluator.evaluate(f2, d, Float.valueOf(f)).floatValue();
    }

    public static int a(int i, int i2, int i3, int i4) {
        return i;
    }

    public static int a(Attributes attributes, int i) {
        return i;
    }

    public static long a() {
        return new Date().getTime();
    }

    public static RemoteException a(String str, Throwable th) {
//        zzayu.zzc(str, th);
        return new RemoteException();
    }

    /*public static IObjectWrapper TouchA(Parcel parcel) {
        IObjectWrapper asInterface = Stub.asInterface(parcel.readStrongBinder());
        parcel.recycle();
        return asInterface;
    }*/

    public static String a(int i, String str, int i2) {
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        sb.append(i2);
        return sb.toString();
    }

    public static String a(int i, String str, int i2, String str2, int i3) {
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        sb.append(i2);
        sb.append(str2);
        sb.append(i3);
        return sb.toString();
    }

    public static String a(int i, String str, String str2) {
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    public static String a(int i, String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        return sb.toString();
    }

    public static String a(int i, String str, String str2, String str3, String str4) {
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append(str4);
        return sb.toString();
    }


    public static String a(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(i);
        return sb.toString();
    }

    public static String a(String str, int i, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(i);
        sb.append(str2);
        return sb.toString();
    }

    public static String a(String str, long j) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(j);
        return sb.toString();
    }

    public static String a(String str, Uri uri) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(uri);
        return sb.toString();
    }

    public static String a(String str, Fragment fragment, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(fragment);
        sb.append(str2);
        return sb.toString();
    }

    public static String a(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    public static String a(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        return sb.toString();
    }

    public static String a(String str, String str2, String str3, String str4) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append(str4);
        return sb.toString();
    }

    public static String a(StringBuilder sb, int i, String str) {
        sb.append(i);
        sb.append(str);
        return sb.toString();
    }

    public static String a(StringBuilder sb, String str, String str2) {
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    public static StringBuilder a(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        return sb;
    }

    public static StringBuilder a(String str, int i, String str2, int i2, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(i);
        sb.append(str2);
        sb.append(i2);
        sb.append(str3);
        return sb;
    }

    public static ArrayList a(LinkedHashMap linkedHashMap, Object obj) {
        ArrayList arrayList = new ArrayList();
        linkedHashMap.put(obj, arrayList);
        return arrayList;
    }

    public static ArrayList a(Map map, Object obj) {
        ArrayList arrayList = new ArrayList();
        map.put(obj, arrayList);
        return arrayList;
    }

    public static void a(int i, Canvas canvas, int i2, int i3) {
//        InlineMarker.finallyStart(i);
        canvas.restoreToCount(i2);
//        InlineMarker.finallyEnd(i3);
    }

    public static int b(int i, int i2, int i3, int i4) {
        return ((i * i2) / i3) + i4;
    }

    public static int b(String str, int i) {
        return String.valueOf(str).length() + i;
    }

    public static StringBuilder b(int i, String str, String str2, String str3, String str4) {
        StringBuilder sb = new StringBuilder(i);
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append(str4);
        return sb;
    }

    public static StringBuilder b(String str, int i, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(i);
        sb.append(str2);
        return sb;
    }

    public static StringBuilder b(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        return sb;
    }

    public static void b(String str, Fragment fragment, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(fragment);
        Log.v(str2, sb.toString());
    }

    public static void b(String str, String str2, String str3, String str4) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
//        com.example.mvvmstickermodule.base.utils.Logger.i(str4, sb.toString());
    }

    public static void b(StringBuilder sb, String str, String str2) {
        sb.append(str);
        Log.e(str2, sb.toString());
    }

    public static void c(String str, int i, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(i);
        Log.i(str2, sb.toString());
    }


}
