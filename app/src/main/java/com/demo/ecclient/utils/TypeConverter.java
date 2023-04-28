package com.demo.ecclient.utils;

import java.math.BigInteger;
import java.util.Arrays;

public class TypeConverter {
    public static BigInteger[] intsToBigIntegers(int[] ints) {
        return Arrays.stream(ints)
                .mapToObj(BigInteger::valueOf)
                .toArray(BigInteger[]::new);
    }

    public static int[] bigIntegersToInts(BigInteger[] bigIntegers) {
        return Arrays.stream(bigIntegers)
                .mapToInt(BigInteger::intValue)
                .toArray();
    }

}
