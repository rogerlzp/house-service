package com.house.backend.houseservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final Logger log = LoggerFactory.getLogger(MD5Utils.class);

    public MD5Utils() {
    }

    public static String encrypt(String data) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(data.getBytes());
            byte[] var3 = md5.digest();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                byte b = var3[var5];
                sb.append(String.format("%02X", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException var7) {
            log.error("MD5加密出错详情：", var7);
            return null;
        }
    }

}
