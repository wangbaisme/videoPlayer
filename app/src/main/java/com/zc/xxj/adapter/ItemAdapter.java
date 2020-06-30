package com.zc.xxj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.zc.xxj.R;
import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.utils.SystemUtil;


public abstract class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ModuleInfo> mItemNameList;
    private int screenWidth;
    LinearLayout.LayoutParams params;

    public ItemAdapter(Context context, ArrayList<ModuleInfo> itemList){
        this.mContext = context;
        mItemNameList = itemList;
        screenWidth = SystemUtil.getScreenWidth(mContext) - SystemUtil.dip2px(mContext,40);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth*9/16);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView mItemName;
        ImageView mItemImg;
        ImageView mItemExchangeState;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.item_card_view);
            mItemName = itemView.findViewById(R.id.item_name);
            mItemImg = itemView.findViewById(R.id.item_img);
            mItemExchangeState = itemView.findViewById(R.id.exchange_state);
        }
    }

    public void updateItem(ArrayList<ModuleInfo> itemList){
        mItemNameList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mCardView.setLayoutParams(params);
        holder.mItemName.setText(mItemNameList.get(position).getmVideoName());
        holder.mItemImg.setImageResource(mItemNameList.get(position).getmVideoImg());
        if (mItemNameList.get(position).isExchangeState())
            holder.mItemExchangeState.setVisibility(View.VISIBLE);
        else
            holder.mItemExchangeState.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此处应该有兑换状态
                if (mItemNameList.get(position).isExchangeState()){

                    return;
                }
                clickItme(mItemNameList.get(position).getmVideoUrl(), mItemNameList.get(position).getmVideoName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemNameList.size();
    }

    public abstract void clickItme(String videoUrl, String videoName);
}
