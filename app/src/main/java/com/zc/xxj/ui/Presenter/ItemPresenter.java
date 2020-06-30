package com.zc.xxj.ui.Presenter;

import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.ui.Contract.ItemContract;
import com.zc.xxj.ui.Model.ItemModel;

import java.util.ArrayList;

public class ItemPresenter implements ItemContract.ItemPresenter {

    private ItemContract.ItemModel mItemModel;

    public ItemPresenter(ItemContract.ItemView mView){
        mItemModel = new ItemModel() {
            @Override
            public void callBack(ArrayList<ModuleInfo> list) {
                mView.refreshVideoList(list);
            }
        };
    }

    @Override
    public void getVideoList(String moduleName) {
        mItemModel.updateData(moduleName);
    }
}
