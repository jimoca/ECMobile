package model;

import android.graphics.Bitmap;

import java.math.BigInteger;

public class PictureMask implements PictureRaw {
    private BigInteger[] pixels;
    private Bitmap bitmap;
    private int width;
    private int height;

    public PictureMask(BigInteger[] pixels, Bitmap bitmap, int width, int height) {
        this.pixels = pixels;
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
    }

    public static PictureMask pictureMask(Bitmap bitmap, int[] pixels) {
        BigInteger[] bigIntegers = new BigInteger[pixels.length];
        for (int i = 0; i < pixels.length; i++)
            bigIntegers[i] = BigInteger.valueOf(pixels[i]);
        return new PictureMask(bigIntegers, bitmap, bitmap.getWidth(), bitmap.getHeight());
    }

    public Bitmap getBitmap() {
        return bitmap;
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