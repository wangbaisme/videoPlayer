package com.zc.xxj.ui.Presenter;

import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.ui.Contract.SearchResultContract;
import com.zc.xxj.ui.Model.SearchResultModel;

import java.util.ArrayList;

public class SearchResultPresenter implements SearchResultContract.SearchResultPresenter {

    private SearchResultContract.SearchResultModel mSearchResultModel;

    public SearchResultPresenter(SearchResultContract.SearchResultView mView){
        mSearchResultModel = new SearchResultModel() {
            @Override
            public void callbackWithList(ArrayList<ModuleInfo> list) {
                mView.showVideoListView(list);
            }
        };
    }

    @Override
    public void requestSearchContent(String keyWord) {
        mSearchResultModel.searchVideoWithKeyword(keyWord);
    }
}
