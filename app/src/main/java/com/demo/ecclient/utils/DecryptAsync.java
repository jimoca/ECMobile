package com.demo.ecclient.utils;


import java.math.BigInteger;

public interface DecryptAsync {
    void processStart();
    void processFinish(BigInteger[] output);
}
