package com.demo.ecclient.utils;

import static com.demo.ecclient.utils.Constants.DECRYPT;
import static com.demo.ecclient.utils.Constants.ENCRYPT;

import android.util.Log;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import security.misc.HomomorphicException;
import security.paillier.PaillierCipher;
import security.paillier.PaillierPrivateKey;
import security.paillier.PaillierPublicKey;

public class PaillierPixels {

    public static BigInteger[] encryptPixels(BigInteger[] pixels, PaillierPublicKey publicKey) {
        Instant start = Instant.now();
        Log.d(ENCRYPT, "Start...");
        // 用來紀錄已經加密過的pixel
        Map<BigInteger, BigInteger> repeatPixel = new HashMap<>();


        pixels = Arrays.stream(pixels).map(pixel -> {

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

        Instant finish = Instant.now();
        long time = Duration.between(start, finish).toMillis();
        Log.d(ENCRYPT, "Finished: " + time + "ms");
        return pixels;
    }

    public static BigInteger[] decryptPixels(BigInteger[] pixels, PaillierPrivateKey privateKey) {
        Instant start = Instant.now();
        Log.d(DECRYPT, "Start...");
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

        Instant finish = Instant.now();
        long time = Duration.between(start, finish).toMillis();
        Log.d(DECRYPT, "Finished: " + time + "ms");

        return pixels;
    }

}
