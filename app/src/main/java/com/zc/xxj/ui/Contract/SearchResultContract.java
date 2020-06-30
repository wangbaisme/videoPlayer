package com.zc.xxj.ui.Contract;

import com.zc.xxj.bean.ModuleInfo;

import java.util.ArrayList;

public interface SearchResultContract extends BaseContract {
    interface SearchResultModel extends BaseModel{
        void searchVideoWithKeyword(String keyword);
    }

    interface SearchResultPresenter extends BasePresenter{
        void requestSearchContent(String keyWord);
    }

    interface SearchResultView extends BaseView{
        void showVideoListView(ArrayList<ModuleInfo> list);
    }
}
