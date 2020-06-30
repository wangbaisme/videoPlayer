package com.zc.xxj.ui.activity;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.zc.xxj.R;
import com.zc.xxj.adapter.ItemAdapter;
import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.ui.Contract.SearchResultContract;
import com.zc.xxj.ui.Presenter.SearchResultPresenter;

public class SearchResultActivity extends BaseActivity implements SearchResultContract.SearchResultView {

    @BindView(R.id.ll_status_bar)
    LinearLayout mLlStatusBar;
    @BindView(R.id.search_result_back)
    ImageView mBtnBack;
    @BindView(R.id.search_result_recycle)
    RecyclerView mSearchResultRecycle;

    private String keyword;
    private ItemAdapter mItemAdapter;
    private SearchResultContract.SearchResultPresenter mSearchResultPresenter;
    private ArrayList<ModuleInfo> mModuleInfoList = new ArrayList<>();

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        keyword = getIntent().getStringExtra("keyword");

        mSearchResultPresenter = new SearchResultPresenter(this);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mItemAdapter = new ItemAdapter(mContext, mModuleInfoList) {
            @Override
            public void clickItme(String videoUrl, String videoName) {
                Intent intent = new Intent(mContext, VideoPlayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("videoUrl", videoUrl);
                intent.putExtra("videoTitle", videoName);
                startActivity(intent);
//                try {
//                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//                    pendingIntent.send();
//                } catch (Exception e) {
//                    startActivity(intent);
//                    e.printStackTrace();
//                }
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mSearchResultRecycle.setLayoutManager(layoutManager);
        mSearchResultRecycle.setAdapter(mItemAdapter);

        mSearchResultPresenter.requestSearchContent(keyword);
    }

    @Override
    public void showVideoListView(ArrayList<ModuleInfo> list) {
        mItemAdapter.updateItem(list);
    }
}
