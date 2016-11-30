package Bmob_adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.util.ArrayList;
import java.util.List;

import bean.OutLineFile;
import butterknife.ButterKnife;
import butterknife.InjectView;
import mrcheng.myapplication.R;
import util.UpFileMethod;

/**
 * Created by mr.cheng on 2016/11/17.
 */
public class UpFileAdapter extends RecyclerView.Adapter<UpFileAdapter.ViewHolder> {
    private Context mContext;
    private List<OutLineFile> mDatas;
    private Handler mHandler;

    public UpFileAdapter(Context context, List<OutLineFile> datas,Handler handler) {
        mContext = context;
        mDatas = datas;
        this.mHandler=handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filemsg, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mData.setText("录制时间:" + mDatas.get(position).getData());
        if (mDatas.get(position).isSuccess()){
            holder.mUp.setProgress(100);
        }
        holder.mUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mUp.getProgress()==100){
                    Toast.makeText(mContext, "已经上传", Toast.LENGTH_SHORT).show();
                }else {
                    new UpFileMethod(mContext,mDatas.get(position), holder.mUp,mHandler).done();
                }
            }
        });
        /**
         * 模版里面的onclickListener
         */
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.data)
        TextView mData;
        @InjectView(R.id.up)
        CircularProgressButton mUp;
        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public void addData(int position, OutLineFile item) {
        mDatas.add(position, item);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
