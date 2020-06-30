package com.zc.xxj.ui.Contract;

import java.util.List;

public interface SearchContract extends BaseContract {
    interface SearchModel extends BaseContract.BaseModel{
        void getSearchHistory();
        void getHotRecomment();
        void updateSearchHistory(List<String> list);
    }

    interface SearchPresenter extends BaseContract.BasePresenter{
        void refreshHistoryView();
        void refreshRecommentView();
        void updateSearchHistoryData(List<String> list);
        void updateSearchHistoryData(String currentSelectWord);
        void initRecord();
        void startRecord();
        void stopRecord();
        void cancelRecord();
        void destroyRecord();
    }

    interface SearchView extends BaseView{
        void refreshHistoryFlowView(List<String> list);
        void refreshHotRecommentFlowView(List<String> list);
        List<String> getHistoryList();
        void recodeContentText(String keyword);
        void showRecordWaveView();
        void hideRecordWaveView();
    }
}
