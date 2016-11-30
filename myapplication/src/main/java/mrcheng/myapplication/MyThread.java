package mrcheng.myapplication;

/**
 * Created by mr.cheng on 2016/11/30.
 */
public class MyThread {

    static {
        System.loadLibrary("myNativeLib");
    }
    public native void getStringFromNative(short[] shorts, double[] doubles);
}
