package util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bean.DowmloadTable;
import bean.MyUser;
import bean.OutLineFile;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by mr.cheng on 2016/9/10.
 */
public class UpFileMethod {
    private OutLineFile mOutLineFile;
    private static final String TAG = "UpFileMethod";
    private Context mContext;
    private Handler mHandler;
    private CircularProgressButton mCircularProgressButton;


    public UpFileMethod(Context context, OutLineFile outLineFile, CircularProgressButton circularProgressButton, Handler handler) {
        mOutLineFile = outLineFile;
        this.mContext = context;
        mCircularProgressButton = circularProgressButton;
        this.mHandler = handler;
    }

    public void done() {

        final BmobFile bmobFile = new BmobFile(new File(mOutLineFile.getPath()));
        bmobFile.upload(mContext, new UploadFileListener() {
            @Override
            public void onSuccess() {
                final MyUser myUser = BmobUser.getCurrentUser(mContext, MyUser.class);
                BmobQuery<DowmloadTable> query = new BmobQuery<>();
                query.addWhereEqualTo("author", myUser);
                query.findObjects(mContext, new FindListener<DowmloadTable>() {
                    @Override
                    public void onSuccess(final List<DowmloadTable> list) {
                        DowmloadTable dowmloadTable = new DowmloadTable();
                        dowmloadTable.add("urls", bmobFile.getFileUrl(mContext));
                        dowmloadTable.add("times", mOutLineFile.getData());
                        if (list.size() == 0) {
                            dowmloadTable.setAuthor(myUser);
                            dowmloadTable.save(mContext, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    updateUi("上传成功");
                                    mOutLineFile.setIsSuccess(true);
                                    mOutLineFile.save();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    updateUi("上传失败");

                                }
                            });
                        } else {
                            dowmloadTable.update(mContext, list.get(0).getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    updateUi("上传成功");
                                    mOutLineFile.setIsSuccess(true);
                                    mOutLineFile.save();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    updateUi("上传失败");

                                }

                            });
                        }


                    }

                    @Override
                    public void onError(int i, String s) {
                        updateUi("上传失败");

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                updateUi("上传失败");
            }

            @Override
            public void onProgress(final Integer value) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCircularProgressButton.setProgress(value);

                    }
                });
                super.onProgress(value);

            }
        });
    }

    private void updateUi(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
