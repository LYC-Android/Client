package com.example;




public class Four {
    public static void main(String[] args) {
        new Four().method();
    }

    /**
     * 输入参数
     */
    public static final String FIND_CARD_ERROR="aa bb 06 00 00 00 01 02 14 14";
    public static final String FIND_CARD_SUCCESS="aabb08 00 00 00 01 02 00 04 00 07";

    private void method() {
        String readStr=FIND_CARD_ERROR;
        boolean statu = !(readStr.equals(FIND_CARD_ERROR.replace(" ", "").toLowerCase()));
//        System.out.println(FIND_CARD_ERROR.replace(" ", "").toLowerCase());
        System.out.println(statu);
    }



}
