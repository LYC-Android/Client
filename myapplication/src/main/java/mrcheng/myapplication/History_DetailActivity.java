package mrcheng.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import bean.DatabaseInfo;
import bean.MyUser;
import bean.Report;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;

public class History_DetailActivity extends BaseActivity {

    @InjectView(R.id.Realname)
    TextView mRealname;
    @InjectView(R.id.age)
    TextView mAge;
    @InjectView(R.id.sex)
    TextView mSex;
    @InjectView(R.id.createtime)
    TextView mCreatetime;
    @InjectView(R.id.message)
    TextView mMessage;
    @InjectView(R.id.xinlv)
    TextView mXinlv;
    @InjectView(R.id.RR)
    TextView mRR;
    @InjectView(R.id.QRS)
    TextView mQRS;
    @InjectView(R.id.suggest)
    TextView mSuggest;
    @InjectView(R.id.commit_time)
    TextView mCommitTime;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history__detail);
        ButterKnife.inject(this);
        setTitle("历史病历");
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        progressDialog = new ProgressDialog(History_DetailActivity.this);
        progressDialog.setTitle("提示:");
        progressDialog.setMessage("正在获取信息,请稍候...");
        progressDialog.show();
        getInfo();
    }

    private void getInfo() {
        BmobQuery<Report> query = new BmobQuery<>();
        String objectId = getIntent().getStringExtra("objectId");
        query.getObject(History_DetailActivity.this, objectId, new GetListener<Report>() {
            @Override
            public void onSuccess(Report report) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                setMesage(report);
            }

            @Override
            public void onFailure(int i, String s) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(History_DetailActivity.this, "查询错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMesage(Report report) {
        mXinlv.setText("心率:窦性心率" + report.getXinlv() + "/min");
        mRR.setText("心动周期(R-R): " + report.getRR() + "秒");
        mQRS.setText("QRS时限:" + report.getQRS() + "秒");
        mSuggest.setText("心电图诊断意见:" + report.getSuggest());
        mMessage.setText("心电图诊断:" + report.getMessage());
        mCommitTime.setText(report.getResultTime());
        mCreatetime.setText(report.getCreateTime());
        MyUser myUser = BmobUser.getCurrentUser(History_DetailActivity.this, MyUser.class);
        mRealname.setText("姓名:" + myUser.getRealName());
        mAge.setText("年龄:" + myUser.getAge());
        if (myUser.getIsBoys()) {
            mSex.setText("性别:男");
        } else {
            mSex.setText("性别:女");
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
