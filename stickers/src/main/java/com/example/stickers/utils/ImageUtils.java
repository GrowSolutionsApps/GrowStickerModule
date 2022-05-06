package com.example.stickers.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static Bitmap bytesToBitmap(byte[] bArr) {
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
    }

    public static byte[] compressImageToBytes(Uri uri, int i, int i2, int i3, Context context, CompressFormat compressFormat) throws IOException {
        Bitmap bitmap = Media.getBitmap(context.getContentResolver(), uri);
        int attributeInt = new ExifInterface(WAFileUtils.getImageRealPathFromURI(context, uri)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        Matrix matrix = new Matrix();
        if (attributeInt == 6) {
            matrix.postRotate(90.0f);
        } else if (attributeInt == 3) {
            matrix.postRotate(180.0f);
        } else if (attributeInt == 8) {
            matrix.postRotate(270.0f);
        }
        Bitmap overlayBitmapToCenter = overlayBitmapToCenter(Bitmap.createBitmap(i3, i2, Config.ARGB_8888), scaleBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), i3, i2));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        overlayBitmapToCenter.compress(compressFormat, i, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



    public static Bitmap overlayBitmapToCenter(Bitmap bitmap, Bitmap bitmap2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int width2 = bitmap2.getWidth();
        int height2 = bitmap2.getHeight();
        double d = (double) width;
        Double.isNaN(d);
        double d2 = d * 0.5d;
        double d3 = (double) width2;
        Double.isNaN(d3);
        float f = (float) (d2 - (d3 * 0.5d));
        double d4 = (double) height;
        Double.isNaN(d4);
        double d5 = d4 * 0.5d;
        double d6 = (double) height2;
        Double.isNaN(d6);
        float f2 = (float) (d5 - (d6 * 0.5d));
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, new Matrix(), null);
        canvas.drawBitmap(bitmap2, f, f2, null);
        return createBitmap;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("Width and height are ");
        sb.append(width);
        String str = "--";
        sb.append(str);
        sb.append(height);
        String str2 = "Pictures";
        Log.v(str2, sb.toString());
        if (width > height) {
            i2 = (int) (((float) height) / (((float) width) / ((float) i)));
        } else if (height > width) {
            i = (int) (((float) width) / (((float) height) / ((float) i2)));
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("after scaling Width and height are ");
        sb2.append(i);
        sb2.append(str);
        sb2.append(i2);
        Log.v(str2, sb2.toString());
        return Bitmap.createScaledBitmap(bitmap, i, i2, true);
    }
}
