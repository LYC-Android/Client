package mrcheng.testrxaudio;

import android.Manifest;
import android.media.AudioFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.piasy.rxandroidaudio.StreamAudioRecorder;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private StreamAudioRecorder mStreamAudioRecorder;
    boolean mIsRecording;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mStreamAudioRecorder = StreamAudioRecorder.getInstance();
    }

    @OnClick({R.id.start, R.id.stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                start();
                break;
            case R.id.stop:
                break;
        }
    }

    private void start() {
        if (mIsRecording) {
            mIsRecording = false;
        } else {
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
                startRecord();
                mIsRecording = true;
            }
        }

    }

    private void startRecord() {
        Log.d(TAG, "start");
        mStreamAudioRecorder.start(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO
                , AudioFormat.ENCODING_PCM_16BIT, 128000, new StreamAudioRecorder.AudioDataCallback() {
            @Override
            public void onAudioData(byte[] data, int size) {
                Log.d(TAG, size + "");
                Log.d(TAG, data.length+"");
            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "错误了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
