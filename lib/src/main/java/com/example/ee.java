package com.example;

import java.util.ArrayList;

/**
 * 输入一个8192长度的FLOAT数组，输出8个长度的int数组
 */

public class ee {
    public static void main(String[] args) {

    }

    private int[] CaculateXinLv(ArrayList<Float> datas) {
        float[] mFloats = new float[datas.size()];
        float[] mDaoshu = new float[datas.size()];
        for (int i = 0; i < datas.size(); i++) {
            mFloats[i] = datas.get(i);
        }

        mDaoshu[0] = mDaoshu[1] = mDaoshu[datas.size()-1] = mDaoshu[datas.size()-2] = 0;
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
        ArrayList<Integer> dianshu = new ArrayList<>();
        float threshold = (float) (max_daoshu * 0.375);
        for (int i = 0; i < mFloats.length - 1; i++) {
            if (mFloats[i] > threshold && (mDaoshu[i] * mDaoshu[i + 1]) < 0) {
                dianshu.add(i);
            }
        }

        int[] result1 = new int[dianshu.size() - 1];
        for (int i = 0; i < dianshu.size() - 1; i++) {
            result1[i] = dianshu.get(i + 1) - dianshu.get(i);
        }

        int fs = 1024;
        int[] result2 = new int[result1.length];
        for (int i = 0; i < result1.length; i++) {
            result2[i] = (60 * fs) / result1[i];
        }

        //输出结果result2【】
       return result2;
    }

//    private ArrayList<Float> readFile(String fileName) {
//        ArrayList<Float> datas = new ArrayList<Float>();
//        try {
//            File file = new File(fileName);
//            FileInputStream inputStream = new FileInputStream(file);
//            InputStreamReader isr = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(isr);
//            String string = null;
//            while ((string = bufferedReader.readLine()) != null) {
//                datas.add(new Float(string));
//            }
//            bufferedReader.close();
//            isr.close();
//            inputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return datas;
//    }
}
