package com.example.laboratorytwoau.ui.main;

import com.example.laboratorytwoau.R;
import com.example.laboratorytwoau.data.ResourceHelper;
import com.example.laboratorytwoau.data.entities.TabItemModel;
import com.example.laboratorytwoau.ui.suitable.SuitableVacanciesFragment;
import com.example.laboratorytwoau.ui.vacancies.VacanciesFragment;

import java.util.ArrayList;

public class MainPresenter implements MainContract.Presenter {
    private ResourceHelper mResourceHelper;
    private MainContract.View mView;

    public MainPresenter(ResourceHelper resourceHelper) {
        mResourceHelper = resourceHelper;
    }

    @Override
    public void bind(MainContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }

    private boolean isViewAttached() {
        return mView != null;
    }

    @Override
    public void setViewPager() {
        if (isViewAttached() && mResourceHelper != null) {
            ArrayList<TabItemModel> tabItemModels = new ArrayList<>();
            tabItemModels.add(new TabItemModel(new VacanciesFragment(),
                    mResourceHelper.getContext().getString(R.string.day_vacancies)));
            tabItemModels.add(new TabItemModel(new SuitableVacanciesFragment(),
                    mResourceHelper.getContext().getString(R.string.viewed_vacancies)));
            mView.initViewPager(tabItemModels);
        }
    }
}
