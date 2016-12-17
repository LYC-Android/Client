package util;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.piasy.rxandroidaudio.StreamAudioRecorder;
import com.tbruyelle.rxpermissions.RxPermissions;

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
import rx.functions.Action1;

/**
 *
 */

public class UpFileActivity extends BaseActivity {

    @InjectView(R.id.status)
    TextView mStatus;
    @InjectView(R.id.recycleView)
    RecyclerView mRecycleView;
    @InjectView(R.id.record)
    Button mRecord;
    private static final String TAG = "UpFileActivity";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private UpFileAdapter mAdapter;
    private AudioRecord recorder;
    private List<OutLineFile> datas = new ArrayList<>();
    private BufferedWriter writer;
    private String path;
    private Date date;
    private MyThread JNI = new MyThread();

    private Thread RecordThread;

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
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                128000);
        mAdapter = new UpFileAdapter(UpFileActivity.this, datas, mHandler);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mAdapter);
    }

    @OnClick({R.id.record})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record:
                mRecord.setEnabled(false);
                start();
                break;
        }
    }

    public void start() {
        boolean isPermissionsGranted = RxPermissions.getInstance(getApplicationContext())
                .isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && RxPermissions.getInstance(getApplicationContext())
                .isGranted(Manifest.permission.RECORD_AUDIO);
        if (!isPermissionsGranted) {
            RxPermissions.getInstance(getApplicationContext())
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean granted) {
                            // not record first time to request permission
                            if (granted) {
                                Toast.makeText(getApplicationContext(), "Permission granted",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Permission not granted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } else {
            RecordThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    startRecord();
                }
            });
            RecordThread.start();
        }

    }

    private void stopRecord() {

        try {
            if (writer != null) {
                writer.close();
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
                    mRecord.setEnabled(true);
                    Toast.makeText(UpFileActivity.this, "录制成功", Toast.LENGTH_SHORT).show();
                    mStatus.setText("当前状态:录制成功，可以等待上传");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRecord() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mStatus.setText("当前状态:录制中.....");
                    Toast.makeText(UpFileActivity.this, "录制中，请耐心等待", Toast.LENGTH_SHORT).show();
                }
            });
            initFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            short[] myRecoed = new short[64000];
            double[] doubles = new double[65536];
            recorder.startRecording();//开始录音
            for (int i = 0; i < 4; i++) {
                Log.d(TAG, "第" + i + "次");
                recorder.read(myRecoed, 0, myRecoed.length);
                if (i == 3) {
                    recorder.stop();
                    recorder.release();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                JNI.getStringFromNative(myRecoed, doubles);
                Log.d(TAG, "过了JNI");

                for (int j = 0; j < 4000; j++) {
                    writer.write(doubles[j] + "\r\n");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopRecord();
    }





    private void initFile() throws IOException {
        MyUser myUser = BmobUser.getCurrentUser(UpFileActivity.this, MyUser.class);
        String mPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "bmob" + File.separator;
        File file1 = new File(mPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(mPath);
        builder.append(myUser.getUsername());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        date = new Date(System.currentTimeMillis());
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
    }

    @Override
    protected void onDestroy() {
        if (RecordThread != null) {
            RecordThread.interrupt();
        }

        super.onDestroy();
    }
}
