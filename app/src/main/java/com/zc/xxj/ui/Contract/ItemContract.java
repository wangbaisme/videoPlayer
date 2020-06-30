package com.zc.xxj.ui.Contract;

import com.zc.xxj.bean.ModuleInfo;
import java.util.ArrayList;

public interface ItemContract extends BaseContract {
    interface ItemModel extends BaseContract.BaseModel{
        void updateData(String moduleName);
    }

    interface ItemPresenter extends BaseContract.BasePresenter{
        void getVideoList(String moduleName);
    }

    interface ItemView extends BaseContract.BaseView{
        void refreshVideoList(ArrayList<ModuleInfo> list);
    }
}
