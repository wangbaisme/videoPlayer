package com.zc.xxj.ui.Contract;


import java.util.List;

public interface HomePageContract extends BaseContract {
    interface HomePageModel extends BaseModel{
        void instantiateTag();
    }

    interface HomePagePresenter extends BasePresenter{
        void getHomePageTag();
    }

    interface HomePageView extends BaseView{
        void refreshView(List<String> list);
    }
}
