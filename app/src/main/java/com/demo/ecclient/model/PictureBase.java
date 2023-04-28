package com.demo.ecclient.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.math.BigInteger;

public class PictureBase implements PictureRaw, Serializable {
    private static final long serialVersionUID = -3751255153289772365L;
    private BigInteger[] pixels;
    private int width;
    private int height;

    public PictureBase(BigInteger[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public static PictureBase pictureBase(Bitmap bitmap, int[] pixels) {
        BigInteger[] bigIntegers = new BigInteger[pixels.length];
        for (int i = 0; i < pixels.length ; i++)
            bigIntegers[i] = BigInteger.valueOf(pixels[i]);
        return new PictureBase(bigIntegers, bitmap.getWidth(), bitmap.getHeight());
    }

    public void setPixels(BigInteger[] pixels) {
        this.pixels = pixels;
    }

    @Override
    public BigInteger[] getPixels() {
        return this.pixels;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}
