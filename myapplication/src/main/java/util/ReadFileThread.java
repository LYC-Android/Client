package util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bean.Trasmit;
import cn.bmob.v3.listener.SaveListener;
import mrcheng.myapplication.BaseActivity;
import mrcheng.myapplication.Bmob_ChatActivity;
import mrcheng.myapplication.MyThread;
import mrcheng.myapplication.R;

/**
 * Created by mr.cheng on 2016/9/28.
 */
public class ReadFileThread extends Thread {

    private Context mContext;
    private List<Float> mDatas = new ArrayList<>();
    private boolean flag = true;
    private BatteryReceiver batteryReceiver;
    private int battery;
    private AudioRecord recorder;
    MyThread JNI = new MyThread();
    private static final String TAG = "ReadFileThread";

    public ReadFileThread(Context context) {
        mContext = context;
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                128000);
    }

    @Override
    public void run() {
        super.run();

        EventBus.getDefault().post("已连接");

        short[] myRecoed = new short[64000];
        double[] doubles = new double[65536];

//            ReadFile();
        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        batteryReceiver = new BatteryReceiver();

        //注册receiver
        mContext.registerReceiver(batteryReceiver, intentFilter);
        recorder.startRecording();//开始录音
        while (flag) {
            recorder.read(myRecoed, 0, myRecoed.length);
            if (!flag) return;
            JNI.getStringFromNative(myRecoed, doubles);
            mDatas.clear();
            Trasmit trasmit = new Trasmit();
            for (int i = 0; i < 4000; i++) {
                mDatas.add((float) doubles[i]);
            }
            trasmit.setDatas(mDatas);
            trasmit.setIsOnline(true);
            trasmit.setBatteryInfo(battery + "");
            trasmit.save(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    EventBus.getDefault().post("已上传");
                }

                @Override
                public void onFailure(int i, String s) {
                    EventBus.getDefault().post("上传失败");
                }
            });
            Log.d(TAG, "11111");
        }
        recorder.stop();
    }




    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void SendDisimiss() {
        Trasmit trasmit = new Trasmit();
        trasmit.setIsOnline(false);
        trasmit.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post("已取消");
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
        flag = false;
    }

//    public short[] byteArray2ShortArray(byte[] data, int items) {
//        short[] retVal = new short[items];
//        for (int i = 0; i < retVal.length; i++)
//            retVal[i] = (short) ((data[i * 2] & 0xff) | (data[i * 2 + 1] & 0xff) << 8);
//
//        return retVal;
//    }

    /**
     * 广播接受者
     */
    class BatteryReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //判断它是否是为电量变化的Broadcast Action
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                battery = ((level * 100) / scale);
            }
        }

    }
}
