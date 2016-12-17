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
        String str = "a b";
        String[] arr=str.split("\\s+");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
    public static String algorismToHEXString(int algorism) {
        String result = "";
        result = Integer.toHexString(algorism);
        return result;
    }

}
