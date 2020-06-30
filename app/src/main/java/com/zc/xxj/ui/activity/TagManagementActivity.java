package com.zc.xxj.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.zc.xxj.R;
import com.zc.xxj.adapter.DragGridAdapter;
import com.zc.xxj.ui.Contract.TagManagementContract;
import com.zc.xxj.ui.Presenter.TagManagementPresenter;
import com.zc.xxj.utils.JsonUtil;
import com.zc.xxj.utils.OthersUtil;
import com.zc.xxj.view.DragGridView;

public class TagManagementActivity extends BaseActivity implements View.OnClickListener, TagManagementContract.TagManagementView {
    private static final String TAG = TagManagementActivity.class.getSimpleName();

    @BindView(R.id.added_drag_gridview)
    DragGridView mAddedDragGridView;
    @BindView(R.id.not_added_drag_gridview)
    DragGridView mNotAddedDragGridView;
    @BindView(R.id.btn_control)
    TextView mBtnControl;

    private TagManagementContract.TagManagementPresenter mTagManagementPresenter;

    private List<String> mAddedList = new ArrayList<>();
    private List<String> mNotAddedList = new ArrayList<>();
    private TagManagerAdapter mAddedAdapter;
    private TagManagerAdapter mNotAddedAdapter;

    @Override
    public void refreshTagVeiw(List<String> addedList, List<String> notAddedList) {
        mAddedList = addedList;
        mNotAddedList = notAddedList;

        mAddedAdapter = new TagManagerAdapter(mAddedList, true, mAddedDragGridView) {
            @Override
            public void itemContorlEvent(int position) {
                String itemName = mAddedList.get(position);
                mAddedList.remove(position);
                mNotAddedList.add(itemName);
                mAddedAdapter.updateContent(mAddedList);
                mNotAddedAdapter.updateContent(mNotAddedList);
            }
        };
        mAddedAdapter.setNotMovePosition(1);
        mNotAddedAdapter = new TagManagerAdapter(mNotAddedList, false, mNotAddedDragGridView) {
            @Override
            public void itemContorlEvent(int position) {
                String itemName = mNotAddedList.get(position);
                mNotAddedList.remove(position);
                mAddedList.add(itemName);
                mAddedAdapter.updateContent(mAddedList);
                mNotAddedAdapter.updateContent(mNotAddedList);
            }
        };
        mAddedDragGridView.setAdapter(mAddedAdapter);
        mNotAddedDragGridView.setAdapter(mNotAddedAdapter);
        mAddedDragGridView.setItemLongClickListener(new DragGridView.ItemLongClickListener() {
            @Override
            public void itemLongClick(int position) {
                mAddedAdapter.setControlState(true, position);
                mNotAddedAdapter.setControlState(true, -1);
            }
        });
        mNotAddedDragGridView.setItemLongClickListener(new DragGridView.ItemLongClickListener() {
            @Override
            public void itemLongClick(int position) {
                mAddedAdapter.setControlState(true, -1);
                mNotAddedAdapter.setControlState(true, -1);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_control:
                mTagManagementPresenter.saveTagInfo(mAddedList, mNotAddedList);
                Intent intent = new Intent();
                intent.putStringArrayListExtra(OthersUtil.TAGMANAGEMENT_ADDED_TAGLIST, (ArrayList<String>) mAddedList);
                this.setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_tag_manager);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mBtnControl.setOnClickListener(this);
        mTagManagementPresenter = new TagManagementPresenter(mContext,this);
        mTagManagementPresenter.requsetTag();
    }

    abstract class TagManagerAdapter extends DragGridAdapter<String> {

        boolean canDrag;

        public TagManagerAdapter(List<String> list, boolean canDrag, DragGridView dragGridView) {
            super(list, canDrag, dragGridView);
            this.canDrag = canDrag;
        }

        @Override
        public View getItemView(int position, View convertView, ViewGroup parent) {
            String text = getList().get(position);
            convertView = LayoutInflater.from(TagManagementActivity.this).inflate(R.layout.drag_grid_view_item, null);
            TextView tv_text = convertView.findViewById(R.id.tv_text);
            ImageView btn_control = convertView.findViewById(R.id.tag_control);
            if (canDrag) btn_control.setImageResource(R.mipmap.close);
            else btn_control.setImageResource(R.mipmap.tag_settings);
            btn_control.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemContorlEvent(position);
                }
            });
            tv_text.setText(text);

            return convertView;
        }

        public abstract void itemContorlEvent(int position);
    }
}
