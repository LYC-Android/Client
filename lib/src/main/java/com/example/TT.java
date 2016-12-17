package com.example;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mr.cheng on 2016/11/28.
 */
public class TT {
    private int[] xinlv;
    private int[] resultX;
    private float[] resultY;

    public static void main(String[] args) {
        ArrayList<Float> data = new ArrayList<>();
        try {
            File file = new File("E:" + File.separator + "NewECG4K.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);
            String length;
            while ((length=bf.readLine()) !=null) {
                data.add(Float.valueOf(length));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TT tt = new TT();
        tt.CaculateXinLv(data);
    }

    /**
     * 计算心率的方法，返回一个长度为8的数组
     */
    private void CaculateXinLv(ArrayList<Float> drawList) {
        long start = System.currentTimeMillis();
        int size = 4000;
        float[] mFloats = new float[size];
        float[] mDaoshu = new float[size];
        for (int i = 0; i < size; i++) {
            mFloats[i] = drawList.get(i);
        }

        mDaoshu[0] = mDaoshu[1] = mDaoshu[size - 1] = mDaoshu[size - 2] = 0;
        for (int i = 2; i < mFloats.length - 2; i++) {
            mDaoshu[i] = mFloats[i + 1] - mFloats[i - 1] + 2 * (mFloats[i + 2] - mFloats[i - 2]);
        }
        //最大导数
        float max_daoshu = 0;
        for (int i = 0; i < mDaoshu.length; i++) {
            if (max_daoshu < mDaoshu[i]) {
                max_daoshu = mDaoshu[i];
            }
        }
        //存储点数的R波数组
        ArrayList<Integer> dianshuX = new ArrayList<>();
        float threshold = (float) (max_daoshu * 0.375);
        for (int i = 0; i < mFloats.length - 1; i++) {
            if (mFloats[i] > threshold && (mDaoshu[i] * mDaoshu[i + 1]) < 0) {
                dianshuX.add(i);
            }
        }

        //计算斜线部分，局部变换法
        float[] dianshuY = new float[dianshuX.size()];
        for (int i = 0; i < dianshuY.length; i++) {
            dianshuY[i] = drawList.get(dianshuX.get(i));
        }

        if (dianshuX.get(0) < 40) {
            //第一个点小于40的时候，忽略第一个点
            float[] foward40Y = new float[dianshuX.size() - 1];
            resultX = new int[dianshuX.size() - 1];
            resultY = new float[dianshuX.size() - 1];
            for (int i = 1; i < dianshuX.size(); i++) {
                foward40Y[i - 1] = drawList.get(dianshuX.get(i) - 39);
            }
            for (int i = 0; i < foward40Y.length; i++) {
                float tan = (dianshuY[i] - foward40Y[i]) / 40;
                float[] tempResult = new float[38];
                for (int j = 0; j < 38; j++) {
                    tempResult[j] = Math.abs((tan * (j + 1)) + foward40Y[i] - drawList.get(dianshuX.get(i) - 39 + j));
                }

                int MaxIndex = 0;
                resultY[i] = tempResult[0];
                for (int h = 0; h < tempResult.length - 1; h++) {
                    if (resultY[i] < tempResult[h + 1]) {
                        resultY[i] = tempResult[h + 1];
                        MaxIndex = h + 1;
                    }
                }
                resultX[i] = dianshuX.get(i) - 39 + MaxIndex;
                resultY[i] = drawList.get(resultX[i]);
            }
        } else {
            float[] foward40Y = new float[dianshuX.size()];
            resultX = new int[dianshuX.size()];
            resultY = new float[dianshuX.size()];
            for (int i = 0; i < dianshuX.size(); i++) {
                foward40Y[i] = drawList.get(dianshuX.get(i) - 39);
            }
            for (int i = 0; i < foward40Y.length; i++) {
                float tan = (dianshuY[i] - foward40Y[i]) / 40;
                float[] tempResult = new float[38];
                for (int j = 0; j < 38; j++) {
                    tempResult[j] = Math.abs((tan * (j + 1)) + foward40Y[i] - drawList.get(dianshuX.get(i) - 38 + j));
                }

                int MaxIndex = 0;
                resultY[i] = tempResult[0];
                for (int h = 0; h < tempResult.length - 1; h++) {
                    if (resultY[i] < tempResult[h + 1]) {
                        resultY[i] = tempResult[h + 1];
                        MaxIndex = h + 1;
                    }
                }
                resultX[i] = dianshuX.get(i) - 39 + MaxIndex;
                resultY[i] = drawList.get(resultX[i]);
            }
        }

//        //测试代码
//        System.out.println("X坐标");
//        for (int i = 0; i < resultX.length; i++) {
//            System.out.println(resultX[i]);
//        }
//
//        System.out.println("y坐标");
//        for (int i = 0; i < resultY.length; i++) {
//            System.out.println(resultY[i]);
//        }
        //
        for (int i = 0; i < dianshuX.size(); i++) {
            System.out.println(dianshuX.get(i));
        }
        int[] result1 = new int[dianshuX.size() - 1];
        for (int i = 0; i < dianshuX.size() - 1; i++) {
            result1[i] = dianshuX.get(i + 1) - dianshuX.get(i);
        }

        int fs = 500;
        int[] result2 = new int[result1.length];
        for (int i = 0; i < result1.length; i++) {
            result2[i] = (60 * fs) / result1[i];
        }
        //输出结果result2【】
        xinlv = result2;
        System.out.printf(System.currentTimeMillis()-start+"");
    }

}
