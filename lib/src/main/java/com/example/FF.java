package com.example;

/**
 * Created by mr.cheng on 2016/12/3.
 */
public class FF {
    public static void main(String[] args) {
        FF ff = new FF();
        ff.method();
    }

    private void method() {
        short[] myRecoed = new short[6000];

        for (int i = 0; i < 6000; i++) {
            myRecoed[i]= (short) -32768;
        }
        int counter = 0;
        boolean startCount = true;
        for (int i = 0; i < myRecoed.length; i++) {
            if (Math.abs(myRecoed[i]) == 32768) {
                if (startCount) {
                    counter++;
                    if (counter==1000){
                        System.out.printf("1111");
                        System.out.printf("位置:"+i);
                        System.out.println();
                        counter=0;
                    }
                } else {
                    startCount = true;
                    counter = 0;
                }

            } else {
                startCount = false;
            }
        }
    }
}
