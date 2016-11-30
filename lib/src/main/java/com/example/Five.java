package com.example;

/**
 *  作者:马骥，杨松
 * 将整型转为对应的字符类型的16进制形式
 */

public class Five {
    public static void main(String[] args) {
        new Five().method();
    }

    /**
     * 输入参数
     */
    private int mInt=20;

    private void method() {
        System.out.printf("输入参数为:"+mInt+"，输出16进制形式为:"+algorismToHEXString(mInt));
    }
    public static String algorismToHEXString(int algorism) {
        String result = "";
        result = Integer.toHexString(algorism);
        return result;
    }

}
