package com.zc.xxj.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.zc.player.util.LoggerUtil;
import com.zc.xxj.R;
import com.zc.xxj.ui.Contract.SearchContract;
import com.zc.xxj.ui.Presenter.SearchPresenter;
import com.zc.xxj.utils.ToastUtil;
import com.zc.xxj.view.FlowLayout;
import com.zc.xxj.view.RecordWaveView;
import com.zc.xxj.view.RenderView;

public class SearchActivity extends BaseActivity implements View.OnClickListener,
        TextView.OnEditorActionListener, SearchContract.SearchView {
    private static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.ll_status_bar)
    LinearLayout mLlStatusBar;
    @BindView(R.id.search_editText)
    EditText mSearchEditText;
    @BindView(R.id.search_delete)
    ImageView mSearchEditTextClean;
    @BindView(R.id.search_go)
    ImageView mSearchGo;
    @BindView(R.id.search_back)
    ImageView mSearchBack;
    @BindView(R.id.flow_layout_history)
    FlowLayout mHistoryFlowLayout;
    @BindView(R.id.flow_layout_recommend)
    FlowLayout mRecommendFlowLayout;
    @BindView(R.id.btn_clean)
    ImageView mBtnClean;
    @BindView(R.id.record_wave_view)
    RecordWaveView mRecordWaveView;
    @BindView(R.id.view)
    FrameLayout mView;
    @BindView(R.id.btn_voice_search)
    ImageView mBtnVoiceSearch;

    private List<String> historyList = new ArrayList<>();
    private long lastClickTime = 0;
    private SearchContract.SearchPresenter mSearchPresenter;

    private final int updateRecommendItem = 2000;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){

            }
        }
    };

    @Override
    public void refreshHistoryFlowView(List<String> list) {
        mHistoryFlowLayout.showTag(list);
    }

    @Override
    public void refreshHotRecommentFlowView(List<String> list) {
        mRecommendFlowLayout.showTag(list);
    }

    @Override
    public List<String> getHistoryList() {
        return mHistoryFlowLayout.getHistoryList();
    }

    @Override
    public void recodeContentText(String keyword) {
        mSearchEditText.setText(keyword);
        mSearchEditText.setFocusable(true);
    }

    @Override
    public void showRecordWaveView() {
        if (mView.getVisibility() != View.VISIBLE){
            mView.setVisibility(View.VISIBLE);
            mRecordWaveView.startAnim();
        }
    }

    @Override
    public void hideRecordWaveView() {
        if (mView.getVisibility() == View.VISIBLE){
            mView.setVisibility(View.INVISIBLE);
            mRecordWaveView.stopAnim();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_delete:
                mSearchEditText.setText("");
                break;
            case R.id.search_go:
                searchForOrderTag();
                break;
            case R.id.search_back:
                finish();
                break;
            case R.id.btn_clean:
                historyList.clear();
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        mSearchPresenter.updateSearchHistoryData(historyList);
                    }
                });
                mHistoryFlowLayout.showTag(historyList);
                break;
            case R.id.btn_voice_search:
                mSearchPresenter.startRecord();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch (i){
            case EditorInfo.IME_ACTION_SEARCH:
                searchForOrderTag();
                break;
        }
        return true;
    }

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    public void onResume(){
        super.onResume();
        mRecordWaveView.onResume();
    }

    public void onPause(){
        super.onPause();
        mRecordWaveView.onPause();
    }

    public void onDestroy(){
        super.onDestroy();
        mSearchPresenter.destroyRecord();
    }

    private void init(){
        mSearchPresenter = new SearchPresenter(mContext, this);

        mRecordWaveView.setZOrderOnTop(true);
        mRecordWaveView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mRecordWaveView.setOnInitFinishListener(new RenderView.OnInitFinishListener() {
            @Override
            public void complete() {
                if (getIntent().getBooleanExtra("voiceSearch",false)){
                    mSearchPresenter.startRecord();
                }
            }
        });

        mBtnVoiceSearch.setOnClickListener(this);
        mSearchEditTextClean.setOnClickListener(this);
        mSearchGo.setOnClickListener(this);
        mSearchBack.setOnClickListener(this);
        mBtnClean.setOnClickListener(this);
        mSearchEditText.setOnEditorActionListener(this);
        mSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && getIntent().getBooleanExtra("voiceSearch",false)){
                    mSearchPresenter.destroyRecord();
                    mSearchPresenter.initRecord();
                    mRecordWaveView.stopAnim();
                    mView.setVisibility(View.INVISIBLE);
                }
            }
        });
        mHistoryFlowLayout.setDeleteMode(true);
        mHistoryFlowLayout.setItemClickListener(new FlowLayout.ItemClickListener() {
            @Override
            public void onItemClick(String currentSelectedkeywords) {
                updateHistory(currentSelectedkeywords);
            }
        });

        mSearchPresenter.refreshHistoryView();

        mRecommendFlowLayout.setDeleteMode(false);
        mRecommendFlowLayout.setItemClickListener(new FlowLayout.ItemClickListener() {
            @Override
            public void onItemClick(String currentSelectedkeywords) {
                updateHistory(currentSelectedkeywords);
            }
        });
        mSearchPresenter.refreshRecommentView();
    }

    private void searchForOrderTag(){
        if (System.currentTimeMillis() - lastClickTime <= 300) return;
        lastClickTime = System.currentTimeMillis();
        if (mSearchEditText.getText().toString().isEmpty()){
            ToastUtil.getInstance(this).showShortToast(getString(R.string.search_text_is_null));
            return;
        }
        updateHistory(mSearchEditText.getText().toString());
    }

    private void updateHistory(String currentSelectedWord){
        mSearchPresenter.updateSearchHistoryData(currentSelectedWord);
        Intent intent = new Intent(mContext, SearchResultActivity.class);
        intent.putExtra("keyword", currentSelectedWord);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
