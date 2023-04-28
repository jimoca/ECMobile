package com.demo.ecclient.utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import security.misc.HomomorphicException;
import security.paillier.PaillierCipher;
import security.paillier.PaillierPrivateKey;
import security.paillier.PaillierPublicKey;

public class PaillierPixels {

    public static BigInteger[] encryptPixels(BigInteger[] pixels, PaillierPublicKey publicKey) {
        System.out.println("encrypting..");

        // 用來記錄重複pixel的次數
        Map<BigInteger, Integer> repeatPixelTimes = new HashMap<>();

        // 用來紀錄已經加密過的pixel
        Map<BigInteger, BigInteger> repeatPixel = new HashMap<>();


        pixels = Arrays.stream(pixels).map(pixel -> {
            // ---計算一下重複的pixels
            int times = 1;
            if (repeatPixelTimes.containsKey(pixel)) {
                times += repeatPixelTimes.get(pixel);
            }
            repeatPixelTimes.put(pixel, times);
            // ---

            BigInteger cipherPixel = null;
            if (pixel.compareTo(BigInteger.valueOf(0)) < 0) {
                pixel = pixel.multiply(BigInteger.valueOf(-1));
            }
            try {
                // 如果map裡面有已經加密過的key就直接從map裡面拿
                cipherPixel = repeatPixel.containsKey(pixel) ? repeatPixel.get(pixel) : PaillierCipher.encrypt(pixel, publicKey);
//                cipherPixel = PaillierCipher.encrypt(pixel, publicKey);
                repeatPixel.put(pixel, cipherPixel);
            } catch (HomomorphicException e) {
                e.printStackTrace();
            }

            return cipherPixel;
        }).toArray(BigInteger[]::new);
        System.out.println(repeatPixelTimes);
        System.out.println(repeatPixelTimes.size());
        System.out.println("encrypt finished");

        return pixels;
    }

    public static BigInteger[] decryptPixels(BigInteger[] pixels, PaillierPrivateKey privateKey) {
        System.out.println("decrypting..");
        Map<BigInteger, BigInteger> repeatPixel = new HashMap<>();

        pixels = Arrays.stream(pixels).map(pixel -> {
            BigInteger plainPixel = null;
            try {
                plainPixel = repeatPixel.containsKey(pixel) ? repeatPixel.get(pixel) : PaillierCipher.decrypt(pixel, privateKey);
//                plainPixel = PaillierCipher.decrypt(pixel, privateKey);
                repeatPixel.put(pixel, plainPixel);
                plainPixel = plainPixel.multiply(BigInteger.valueOf(-1));
            } catch (HomomorphicException e) {
                e.printStackTrace();
            }
            return plainPixel;
        }).toArray(BigInteger[]::new);
        System.out.println("decrypt finished");

        return pixels;
    }

}
