package com.jwt.demo.util;




import com.jwt.demo.exception.KinshipRuntimeException;

import java.security.MessageDigest;

/**
 * @author   Mr丶s
 * @ClassName   
 * @Version   V1.0
 * @Date   2018/8/29 21:59
 * @Description   
 */
public class Md5Util {

    /**
     * 用于加密的字符
     */
    private final static char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};


    /**
     * 32位 md5加密
     *
     * @param input 需要md5的串
     * @return md5后的字符串
     */
    public static String md5_32(String input) {
        String code = code(input, 32);
        return code.toLowerCase();
    }

    /**
     * 16位 md5加密
     *
     * @param input 需要md5的串
     * @return md5后的字符串
     */
    public static String md5_16(String input) {
        String code = code(input, 16);
        return code.toLowerCase();
    }

    public static String code(String input, int bit) {
        try {
            MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
            if (bit == 16) {
                return bytesToHex(md.digest(input.getBytes("utf-8"))).substring(8, 24);
            } else {
                return bytesToHex(md.digest(input.getBytes("utf-8")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new KinshipRuntimeException("Could not found MD5 algorithm.", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        int t;
        for (int i = 0; i < 16; i++) {
            t = bytes[i];
            if (t < 0) {
                t += 256;
            }
            sb.append(md5String[(t >>> 4)]);
            sb.append(md5String[(t % 16)]);
        }
        return sb.toString();
    }
}
