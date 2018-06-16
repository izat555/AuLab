package com.example.laboratorytwoau.ui.vacancies;

import com.example.laboratorytwoau.data.entities.VacancyModel;
import com.example.laboratorytwoau.ui.ILifeCycle;
import com.example.laboratorytwoau.ui.IRotateLoading;

import java.util.ArrayList;
import java.util.List;

public interface VacanciesContract {

    interface View extends IRotateLoading {
        void onBackGetVacancies(ArrayList<VacancyModel> vacancyModels);
        void showError(String msg);
        boolean isIndicatorShown();
    }

    interface Presenter extends ILifeCycle<View> {
        void getVacancies(int page);
        void saveFavoriteVacancy(VacancyModel vacancyModel);
        void deleteFavoriteVacancy(String pid);
    }
}
