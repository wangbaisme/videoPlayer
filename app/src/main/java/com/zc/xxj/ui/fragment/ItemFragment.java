package com.zc.xxj.ui.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zc.xxj.R;
import com.zc.xxj.adapter.ItemAdapter;
import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.ui.Contract.ItemContract;
import com.zc.xxj.ui.Presenter.ItemPresenter;
import com.zc.xxj.ui.activity.VideoPlayActivity;
import com.zc.xxj.utils.OthersUtil;

public class ItemFragment extends BaseFragment implements ItemContract.ItemView {
    private static final String TAG = ItemFragment.class.getSimpleName();

    private ItemContract.ItemPresenter mItemPresenter;

    private String mModuleName;
    private TextView mTextView;
    private SwipeRefreshLayout mRefreshView;
    private RecyclerView mItemRecycleView;
    private ItemAdapter mItemAdapter;
    private ArrayList<ModuleInfo> videoList = new ArrayList<>();

    public ItemFragment(String moduleName){
        Bundle bundle = new Bundle();
        bundle.putString(OthersUtil.ItemFragment_MODULENAME,moduleName);
        setArguments(bundle);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_item, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        mItemPresenter = new ItemPresenter(this);

        mTextView = view.findViewById(R.id.module_name);
        mItemRecycleView = view.findViewById(R.id.item_video);
        mRefreshView = view.findViewById(R.id.item_swipe_refresh);
        mRefreshView.setColorSchemeResources(R.color.color_4EA6FD);
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshView.setRefreshing(false);
                    }
                },2000);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mItemRecycleView.setLayoutManager(layoutManager);
        mItemAdapter = new ItemAdapter(getActivity(), videoList) {
            @Override
            public void clickItme(String videoUrl, String videoName) {
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videoTitle", videoName);
                startActivity(intent);
//                try {
//                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, 0);
//                    pendingIntent.send();
//                } catch (Exception e) {
//                    startActivity(intent);
//                    e.printStackTrace();
//                }
            }
        };
        mItemRecycleView.setAdapter(mItemAdapter);
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {
        //可视操作
        if (isVisible){
            mTextView.setText(mModuleName);
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        mModuleName = getArguments().getString(OthersUtil.ItemFragment_MODULENAME);
        mItemPresenter.getVideoList(mModuleName);
    }

    @Override
    public void refreshVideoList(ArrayList<ModuleInfo> list) {
        mItemAdapter.updateItem(list);
    }
}
