package model;


import java.math.BigInteger;

public interface PictureRaw {
    BigInteger[] getPixels();
    int getWidth();
    int getHeight();
}
