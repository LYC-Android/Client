package util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private ObjectInputStream objectInputStream;
    private ServerSocket serverSocket;

    private Context mContext;
    private List<Float> mDatas = new ArrayList<>();
    private boolean flag = true;
    private String Ip;
    private Socket mSocket;
    private BatteryReceiver batteryReceiver;
    private int battery;
    private ObjectOutputStream mObjectOutputStream;


    public ReadFileThread(Context context, String ip) {
        mContext = context;
        this.Ip = ip;

    }

    @Override
    public void run() {
        super.run();
        try {
            serverSocket=new ServerSocket(Constant.TCP_PORT);
            mSocket=serverSocket.accept();

            EventBus.getDefault().post("已连接");
            ReadFile();
            mObjectOutputStream = new ObjectOutputStream(new BufferedOutputStream(mSocket.getOutputStream()));
            //注册广播接受者java代码
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            //创建广播接受者对象
            batteryReceiver = new BatteryReceiver();

            //注册receiver
            mContext.registerReceiver(batteryReceiver, intentFilter);
            while (flag) {
                Thread.sleep(8000);
                if (!flag) return;
                Trasmit trasmit = new Trasmit();
                trasmit.setDatas(mDatas);
                trasmit.setIsOnline(true);
                trasmit.setBatteryInfo(battery + "");
                mObjectOutputStream.writeObject(trasmit);
                mObjectOutputStream.flush();
                EventBus.getDefault().post("已上传");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                if (serverSocket!=null) {
                    serverSocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void ReadFile() {
        try {
            MyThread my=new MyThread();
            int length;
            InputStream is = mContext.getResources().openRawResource(R.raw.fmsignal);
            BufferedInputStream inputStream = new BufferedInputStream(is);
            byte[] buf = new byte[64000 * 2];
            double[] doubles = new double[65536];
            while ((length = inputStream.read(buf, 0, buf.length)) != -1) {
                short[] shorts = byteArray2ShortArray(buf, buf.length / 2);
                my.getStringFromNative(shorts, doubles);
            }
            inputStream.close();
            is.close();
            mDatas.clear();
            for (int i = 0; i < 4000; i++) {
                mDatas.add((float) doubles[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void SendDisimiss() {
        if (mSocket!=null&&mSocket.isConnected()){
            try {
                Trasmit trasmit = new Trasmit();
                trasmit.setIsOnline(false);
                mObjectOutputStream.writeObject(trasmit);
                mObjectOutputStream.flush();
                EventBus.getDefault().post("已关闭传输");
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (mObjectOutputStream!=null) {
                        mObjectOutputStream.close();
                    }
                    if (mSocket!=null) {
                        mSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public short[] byteArray2ShortArray(byte[] data, int items) {
        short[] retVal = new short[items];
        for (int i = 0; i < retVal.length; i++)
            retVal[i] = (short) ((data[i * 2] & 0xff) | (data[i * 2 + 1] & 0xff) << 8);

        return retVal;
    }
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
