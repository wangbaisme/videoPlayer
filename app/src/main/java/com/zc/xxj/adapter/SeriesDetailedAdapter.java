package com.zc.xxj.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zc.xxj.R;
import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.ui.activity.VideoPlayActivity;
import com.zc.xxj.utils.SystemUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SeriesDetailedAdapter extends RecyclerView.Adapter<SeriesDetailedAdapter.ViewHolder> {

    private Context mContext;
    private List<ModuleInfo> detailedVideoList;
    LinearLayout.LayoutParams params;
    private int screenWidth;

    public SeriesDetailedAdapter(Context context, List<ModuleInfo> detailedVideoList){
        mContext = context;
        this.detailedVideoList = detailedVideoList;
        screenWidth = (SystemUtil.getScreenWidth(mContext) - SystemUtil.dip2px(mContext,50))/2
                - SystemUtil.dip2px(mContext,10);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,screenWidth*9/16
                + SystemUtil.dip2px(mContext,24));
    }

    public void updateSeriesDetailed(List<ModuleInfo> detailedVideoList){
        this.detailedVideoList = detailedVideoList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mVideoImg;
        TextView mVideoName;
        CardView mSeriesDetailedCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mVideoImg = itemView.findViewById(R.id.detailed_video_img);
            mVideoName = itemView.findViewById(R.id.detailed_video_name);
            mSeriesDetailedCardView = itemView.findViewById(R.id.series_detailed_card_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_series_detailed, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mSeriesDetailedCardView.setLayoutParams(params);
        holder.mVideoName.setText(detailedVideoList.get(position).getmVideoName());
        holder.mVideoImg.setImageResource(detailedVideoList.get(position).getmVideoImg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectVideo(detailedVideoList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailedVideoList.size();
    }
    public abstract void currentSelectVideo(ModuleInfo info);
}
