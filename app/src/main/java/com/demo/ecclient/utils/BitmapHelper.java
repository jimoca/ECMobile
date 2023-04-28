package com.demo.ecclient.utils;


import android.graphics.Bitmap;

import java.math.BigInteger;
import java.util.Optional;

public class BitmapHelper {

    public static int[] getBitmapPixels(Bitmap bitmap, int x, int y, int width, int height) {
        return Optional.of(new int[bitmap.getWidth() * bitmap.getHeight()])
                .map(it -> {
                    bitmap.getPixels(it, 0, bitmap.getWidth(), x, y,
                            width, height);
                    final int[] subsetPixels = new int[width * height];
                    for (int i = 0; i < height; i++) {
                        System.arraycopy(it, (i * bitmap.getWidth()),
                                subsetPixels, i * width, width);
                    }
                    return subsetPixels;
                }).orElse(null);
    }

    public static Bitmap setBitmapPixels(BigInteger[] pixels, int width, int height) {
        return Optional.of(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888))
                .map(it -> {
                    it.setPixels(TypeConverter.bigIntegersToInts(pixels), 0, width, 0, 0, width, height);
                    return it;
                }).orElse(null);
    }
}

