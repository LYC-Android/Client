package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.cheng on 2016/11/28.
 */
public class TT {
    public static void main(String[] args) {
        TT tt=new TT();
        tt.ttttt();
    }
private void ttttt(){
   long start= System.currentTimeMillis();
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    long end=System.currentTimeMillis();
    System.out.println(end-start);
}
    private void test(){

    int mycount=4000;
        int counter=0;
        List<Integer> TempDoubles = new ArrayList<>();
        List<Integer> mDoubls = new ArrayList<>();
        List<Integer> mDraws = new ArrayList<>();
        for (int i = 0; i < mycount; i++) {
            TempDoubles.add(i);
            if (TempDoubles.size()>=mycount/8){
                for (int h = 0; h < TempDoubles.size(); h++) {
                    mDoubls.add(TempDoubles.get(h));
                    if (h > 0 && h % 5 == 0) {//Modified 每5点插值一次使得8秒数据量达到4800点
                        mDoubls.add((TempDoubles.get(h - 1) + TempDoubles.get(h + 1)) / 2);
                    }
                }
                mDoubls.add(TempDoubles.get((mycount / 8) - 1));
                for (int l = 0; l < mDoubls.size(); l++) {
                    if (l % 4 == 0) {//Modified 然后4倍抽取使得8秒画图点数为1200点
                        mDraws.add(mDoubls.get(l));
                    }
                }
                System.out.println(mDraws.size());
                TempDoubles.clear();
                mDoubls.clear();
            }
        }

    }
}
