package mrcheng.myapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.DatabaseInfo;
import bean.DialogWrapper;
import bean.Request;
import bean.Resopnse;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.notification.BmobNotificationManager;

import cn.bmob.v3.exception.BmobException;
import util.MToast;
import util.ReadFileThread;

/**
 * Created by mr.cheng on 2016/9/14.
 */
public class BaseActivity extends AppCompatActivity {
    private Context mContext;
    private ReadFileThread TransmitThread;
    private AlertDialog TrasmitDialog;
    private AlertDialog YesOrNodialog;
    /**
     * 电量百分比
     */
    private int battery;
    private BatteryReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = BaseActivity.this;
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (batteryReceiver != null) unregisterReceiver(batteryReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String Msg) {
        MToast.showToast(BaseActivity.this, Msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final DialogWrapper event) {
//        processCustomMessage(event.getIp(), event.getCardService(), event.getObjectId());
        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //创建广播接受者对象
        batteryReceiver = new BatteryReceiver();

        //注册receiver
        registerReceiver(batteryReceiver, intentFilter);
        processCustomMessage(event.getMsg(), event.getInfo());
    }

    private void processCustomMessage(BmobIMMessage msg, BmobIMUserInfo info) {
        String type = msg.getMsgType();
        if (type.equals("request")) {

            //发给谁，就填谁的用户信息
            //启动一个暂态会话，也就是isTransient为true,表明该会话仅执行发送消息的操作，不会保存会话和消息到本地数据库中，
            final BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true, null);
            //这个obtain方法才是真正创建一个管理消息发送的会话
            final BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);

            Request request = Request.convert(msg);
            final String realName = request.getRealName();
            YesOrNodialog = new AlertDialog.Builder(mContext).create();
            YesOrNodialog.setTitle(realName + "请求与您你连接");
            YesOrNodialog.setCancelable(false);
            YesOrNodialog.setButton(DialogInterface.BUTTON_POSITIVE, "接受", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Resopnse resopnse = new Resopnse();
                    resopnse.setContent("1");
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("receive", true);
                    resopnse.setExtraMap(map);
                    conversation.sendMessage(resopnse, new MessageSendListener() {
                        @Override
                        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                            if (e == null) {
                                ShowTransmitDialog(realName);
                                Toast.makeText(mContext, "正在与" + realName + "连接", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "发送请求失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            YesOrNodialog.setButton(DialogInterface.BUTTON_NEGATIVE, "拒绝", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Resopnse resopnse = new Resopnse();
                    resopnse.setContent("1");
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("receive", false);
                    resopnse.setExtraMap(map);
                    conversation.sendMessage(resopnse, new MessageSendListener() {
                        @Override
                        public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "拒绝了连接", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "发送请求失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            YesOrNodialog.show();
        }
    }

    /**
     * 弹出正在与对方进行心电传输的对话框
     */
    private void ShowTransmitDialog(String realName) {
        BmobIMApplication.setCONNECTING(true);
        TransmitThread = new ReadFileThread(mContext);
        TransmitThread.start();
        TrasmitDialog = new AlertDialog.Builder(mContext).create();
        TrasmitDialog.setTitle("正与" + realName + "进行心电传输");
        TrasmitDialog.setCancelable(false);
        TrasmitDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TransmitThread.SendDisimiss();
                    }
                }).start();

                BmobIMApplication.setCONNECTING(false);
            }
        });
        TrasmitDialog.show();
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

    public int getBattery() {
        return battery;
    }
}
