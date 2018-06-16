package com.example.laboratorytwoau.ui.main;

import com.example.laboratorytwoau.data.entities.TabItemModel;
import com.example.laboratorytwoau.ui.ILifeCycle;
import com.example.laboratorytwoau.ui.IRotateLoading;

import java.util.ArrayList;

public interface MainContract {

    interface View extends IRotateLoading {
        void initViewPager(ArrayList<TabItemModel> tabItemModels);
    }

    interface Presenter extends ILifeCycle<View> {
        void setViewPager();
    }
}
