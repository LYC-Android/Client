package com.example;

/**
 * 作者:马骥，杨松
 * 将字符串转为ASCLL码的16进制数形式的字符串
 */

public class Two {
    public static void main(String[] args) {
        new Two().method();
    }

    /**
     * 输入的参数
     */
    private String mString = "02a0";

    private void method() {
        System.out.println("输入参数为:" + mString + "输出ASCLL码16进制为:" + StringToAsciiString(mString));
    }

    private  String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            String b = Integer.toHexString(c);
            result = result + b;
        }
        return result;
    }
}
