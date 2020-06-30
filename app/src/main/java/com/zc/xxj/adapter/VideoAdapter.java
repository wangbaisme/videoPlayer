package com.zc.xxj.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.zc.xxj.R;
import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.utils.SystemUtil;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<ModuleInfo> mVideoList = new ArrayList<>();
    private boolean isWindowFull;
    private Context context;

    public VideoAdapter(List<ModuleInfo> mVideoList, boolean isWindowFull, Context context){
        this.mVideoList = mVideoList;
        this.isWindowFull = isWindowFull;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mVideoImg;
        TextView mVideoName;
        CardView mCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mVideoImg = itemView.findViewById(R.id.img_video);
            mVideoName = itemView.findViewById(R.id.name_video);
            mCardView = itemView.findViewById(R.id.adapter_video_card_view);
        }
    }

    public void update(List<ModuleInfo> mVideoList){
        this.mVideoList = mVideoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isWindowFull) view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video, parent, false);
        else view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_video_not_full, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        final ModuleInfo mModuleInfo = mVideoList.get(position);
        holder.mVideoName.setText(mModuleInfo.getmVideoName());
        holder.mVideoImg.setImageResource(mModuleInfo.getmVideoImg());
        if (!isWindowFull){
            LinearLayout.LayoutParams params_ = new LinearLayout.LayoutParams(SystemUtil.getScreenWidth(context)*2/3,
                    SystemUtil.getScreenWidth(context)*3/8);
            holder.mVideoImg.setLayoutParams(params_);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SystemUtil.getScreenWidth(context)*2/3,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.mCardView.setLayoutParams(params);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCompleteListener.resetVideoContent(mModuleInfo);
            }
        });
        if (position % 18 == 17) mOnCompleteListener.loadMore();
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    private OnCompleteListener mOnCompleteListener;

    public interface OnCompleteListener{
        void loadMore();
        void resetVideoContent(ModuleInfo mModuleInfo);
    }

    public void setOnCompleteListener(OnCompleteListener listener){
        this.mOnCompleteListener = listener;
    }
}
