package com.zc.xxj.ui.Presenter;

import android.content.Context;

import com.zc.xxj.ui.Contract.TagManagementContract;
import com.zc.xxj.ui.Model.TagManagementModel;

import java.util.List;

public class TagManagementPresenter implements TagManagementContract.TagManagementPresenter {

    private TagManagementContract.TagManagementModel mTagManagementModel;

    public TagManagementPresenter(Context context, TagManagementContract.TagManagementView mView){

        mTagManagementModel = new TagManagementModel(context) {
            @Override
            public void callbackTotalTag(List<String> mAddedTagList, List<String> mNotAddedTagList) {
                mView.refreshTagVeiw(mAddedTagList, mNotAddedTagList);
            }
        };
    }

    @Override
    public void requsetTag() {
        mTagManagementModel.getTag();
    }

    @Override
    public void saveTagInfo(List<String> list, List<String> list1) {
        mTagManagementModel.updateTagInfo(list, list1);
    }
}
