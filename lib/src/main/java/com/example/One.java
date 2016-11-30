package com.example;

/**
 * 作者:马骥，杨松
 * 将8位整型转换成对应的16进制的字符串的形式
 * 入口int 出口char
 */

public class One {
    public static void main(String[] args) {
        new One().method();
    }

    /**
     * 输入参数int类型
     */
    private int i=255;
    private void method(){
        System.out.println("输入的参数为:"+i+",转换成16进制结果为:"+putInt(i));

    }
    private final static byte[] hex = "0123456789ABCDEF".getBytes();
    /**
     * 转换int为byte数组
     *
     * @param x
     */
    public static String putInt(int x) {
        byte[] bb=new byte[4];
        bb[3] = (byte) (x >> 24);
        bb[2] = (byte) (x >> 16);
        bb[1] = (byte) (x >> 8);
        bb[0] = (byte) (x >> 0);
        byte[] buff = new byte[2 * bb.length];
        for (int i = 0; i < bb.length; i++) {
            buff[2 * i] = hex[(bb[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[bb[i] & 0x0f];
        }
        return new String(buff);
    }
}
