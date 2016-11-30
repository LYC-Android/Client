package com.example;

/**
 * 作者:马骥，杨松
 * 将字符串类型的16进制转换为16进制显示
 */

public class Six {
    public static void main(String[] args) {
        new Six().method();
    }

    /**
     * 输入参数
     */
    private String mString = "02a0";

    private void method() {
        int[][] ints = new int[3][];
        int[] ints3 = new int[]{1, 2, 6};
        ints[0]=ints3;
        int[] ints1 = new int[]{1, 2, 3, 4, 5, 6};
        ints[1] = ints1;
        int[] ints2 = new int[]{3, 2, 3, 5, 5, 6};
        ints[2] = ints2;
        for (int i = 0; i < ints.length; i++) {
            int[] ii=ints[i];
            for (int j = 0; j < ints[i].length; j++) {
                System.out.printf(ints[i][j]+" ");
            }
            System.out.println();
        }
    }

    /**
     * 十六进制字符串转为Byte数组,每两个十六进制字符转为一个Byte
     *
     * @param hex 十六进制字符串
     * @return byte 转换结果
     */
    public static byte[] hexStringToByte(String hex) {
        int max = hex.length() / 2;
        byte[] bytes = new byte[max];
        String binarys = Six.hexStringToBinary(hex);
        for (int i = 0; i < max; i++) {
            bytes[i] = (byte) Six.binaryToAlgorism(binarys.substring(i * 8 + 1, (i + 1) * 8));
            if (binarys.charAt(8 * i) == '1') {
                bytes[i] = (byte) (0 - bytes[i]);
            }
        }
        return bytes;
    }

    /**
     * 十六转二进制
     *
     * @param hex 十六进制字符串
     * @return 二进制字符串
     */
    public static String hexStringToBinary(String hex) {
        hex = hex.toUpperCase();
        String result = "";
        int max = hex.length();
        for (int i = 0; i < max; i++) {
            char c = hex.charAt(i);
            switch (c) {
                case '0':
                    result += "0000";
                    break;
                case '1':
                    result += "0001";
                    break;
                case '2':
                    result += "0010";
                    break;
                case '3':
                    result += "0011";
                    break;
                case '4':
                    result += "0100";
                    break;
                case '5':
                    result += "0101";
                    break;
                case '6':
                    result += "0110";
                    break;
                case '7':
                    result += "0111";
                    break;
                case '8':
                    result += "1000";
                    break;
                case '9':
                    result += "1001";
                    break;
                case 'A':
                    result += "1010";
                    break;
                case 'B':
                    result += "1011";
                    break;
                case 'C':
                    result += "1100";
                    break;
                case 'D':
                    result += "1101";
                    break;
                case 'E':
                    result += "1110";
                    break;
                case 'F':
                    result += "1111";
                    break;
            }
        }
        return result;
    }

    /**
     * 二进制字符串转十进制
     *
     * @param binary 二进制字符串
     * @return 十进制数值
     */
    public static int binaryToAlgorism(String binary) {
        int max = binary.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = binary.charAt(i - 1);
            int algorism = c - '0';
            result += Math.pow(2, max - i) * algorism;
        }
        return result;
    }

}
