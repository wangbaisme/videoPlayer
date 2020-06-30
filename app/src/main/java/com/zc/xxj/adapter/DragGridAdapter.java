package com.zc.xxj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import com.zc.xxj.R;
import com.zc.xxj.view.DragGridView;

public abstract class DragGridAdapter<T> extends BaseAdapter {

    private static final String TAG = "DragGridAdapter";

    private boolean isMove = false;

    private int movePosition = -1;
    private int position = -1;
    private boolean theFristLongClick = true;

    private boolean canDrag;

    private int notMovePosition = 1;
    private DragGridView dragGridView;
    private boolean isVisibility = false;

    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public DragGridAdapter(List list, boolean canDrag, DragGridView dragGridView) {
        this.list = list;
        this.canDrag = canDrag;
        this.dragGridView = dragGridView;
        dragGridView.setCanDrag(canDrag);
    }

    public void setNotMovePosition(int position){
        notMovePosition = position;
        dragGridView.setNotMovePosition(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = getItemView(position, convertView, parent);

        if (canDrag && position <= (notMovePosition - 1)){
            view.setBackgroundResource(R.drawable.btn_bg_pre);
        }

        if (isVisibility && (position >= notMovePosition || !canDrag)){
            view.findViewById(R.id.tag_control).setVisibility(View.VISIBLE);
            if (theFristLongClick && (position == this.position)){
                theFristLongClick = false;
                this.position = -1;
                view.setVisibility(View.INVISIBLE);
            }
        }

        if (position == movePosition && isMove) {
            view.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    protected abstract View getItemView(int position, View convertView, ViewGroup parent);

    public void exchangePosition(int originalPosition, int nowPosition, boolean isMove) {
        T t = list.get(originalPosition);
        list.remove(originalPosition);
        list.add(nowPosition, t);
        movePosition = nowPosition;
        setItemState(isMove);
        notifyDataSetChanged();
    }

    public void setItemState(boolean isMove){
        this.isMove = isMove;
    }

    public void setControlState(boolean isVisibility, int position){
        this.isVisibility = isVisibility;
        this.position = position;
        notifyDataSetChanged();
    }

    public void updateContent(List list){
        this.list = list;
        notifyDataSetChanged();
    }
}
