package util;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Bmob_adapter.UpFileAdapter;
import bean.MyUser;
import bean.OutLineFile;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import mrcheng.myapplication.BaseActivity;
import mrcheng.myapplication.MyThread;
import mrcheng.myapplication.R;

/**
 *
 */

public class UpFileActivity extends BaseActivity implements Runnable {

    @InjectView(R.id.status)
    TextView mStatus;
    @InjectView(R.id.recycleView)
    RecyclerView mRecycleView;
    @InjectView(R.id.record)
    Button mRecord;
    private AudioRecord recorder;
    private String path;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private UpFileAdapter mAdapter;
    private List<OutLineFile> datas = new ArrayList<>();

    static {
        System.loadLibrary("myNativeLib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upfile_layout);
        ButterKnife.inject(this);
        setTitle("离线上传");
        try {
            datas = DataSupport.findAll(OutLineFile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter = new UpFileAdapter(UpFileActivity.this, datas, mHandler);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mAdapter);
    }

    @OnClick({R.id.record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record:
                 mRecord.setEnabled(false);
                new Thread(this).start();
                break;
        }
    }

    @Override
    public void run() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UpFileActivity.this, "正在录制，请耐心等待32秒", Toast.LENGTH_SHORT).show();
                mStatus.setText("当前状态:录制中.....");
            }
        });
        short[][] mydatas = new short[1][];
        short[] myRecoed = new short[64000];
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                128000);
        recorder.startRecording();//开始录音
        for (int i = 0; i < mydatas.length; i++) {
            recorder.read(myRecoed, 0, myRecoed.length);
            mydatas[i] = myRecoed;
        }
        recorder.stop();
        recorder.release();
        MyWriteFileMethod(mydatas);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecord.setEnabled(true);
            }
        });

    }

    private void MyWriteFileMethod(final short[][] mydatas) {
        BufferedWriter writer = null;
        try {
            MyUser myUser = BmobUser.getCurrentUser(UpFileActivity.this, MyUser.class);
            StringBuilder builder = new StringBuilder();
            builder.append("/storage/sdcard0/bmob/");
            builder.append(myUser.getUsername());
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            String name = format.format(date);
            builder.append(name);
            builder.append(".txt");
            path = builder.toString();
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            writer = new BufferedWriter(new FileWriter(file));
            MyThread myThread=new MyThread();
            double[] doubles = new double[65536];
            for (int j = 0; j < mydatas.length; j++) {
                myThread.getStringFromNative(mydatas[j], doubles);
                for (int i = 0; i < 8192; i++) {
                    writer.write(doubles[i] + "\r\n");
                    writer.flush();
                }
            }
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OutLineFile outLineFile = new OutLineFile();
            outLineFile.setData(format1.format(date));
            outLineFile.setIsSuccess(false);
            outLineFile.setPath(path);
            outLineFile.save();
            datas.add(outLineFile);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(UpFileActivity.this, "录制成功", Toast.LENGTH_SHORT).show();
                    mStatus.setText("当前状态:录制成功，可以等待上传");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
