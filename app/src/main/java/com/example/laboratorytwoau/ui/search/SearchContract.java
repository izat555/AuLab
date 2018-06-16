package com.example.laboratorytwoau.ui.search;

import com.example.laboratorytwoau.ui.ILifeCycle;
import com.example.laboratorytwoau.ui.IRotateLoading;

public interface SearchContract {

    interface View extends IRotateLoading {

    }

    interface Presenter extends ILifeCycle<View> {

    }
}
