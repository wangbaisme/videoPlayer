package com.zc.xxj.ui.Contract;

import java.util.List;

public interface TagManagementContract extends BaseContract {
    interface TagManagementModel extends BaseModel{
        void getTag();
        void updateTagInfo(List<String> list, List<String> list1);
    }

    interface TagManagementPresenter extends BasePresenter{
        void requsetTag();
        void saveTagInfo(List<String> list, List<String> list1);
    }

    interface TagManagementView extends BaseView{
        void refreshTagVeiw(List<String> addedList, List<String> notAddedList);
    }
}
