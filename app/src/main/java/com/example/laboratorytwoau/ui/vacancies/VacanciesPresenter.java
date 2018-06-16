package com.example.laboratorytwoau.ui.vacancies;

import com.example.laboratorytwoau.StartApplication;
import com.example.laboratorytwoau.config.AppConstants;
import com.example.laboratorytwoau.data.ResourceHelper;
import com.example.laboratorytwoau.data.SQLiteHelper;
import com.example.laboratorytwoau.data.entities.VacancyModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VacanciesPresenter implements VacanciesContract.Presenter {
    private ResourceHelper mResourceHelper;
    private VacanciesContract.View mView;
    private SQLiteHelper mSQLiteHelper;

    public VacanciesPresenter(ResourceHelper resourceHelper) {
        mResourceHelper = resourceHelper;
        mSQLiteHelper = new SQLiteHelper(mResourceHelper.getContext());
    }

    @Override
    public void bind(VacanciesContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }

    private boolean isViewAttached() {
        return (mView != null);
    }

    @Override
    public void getVacancies(int page) {
        if (isViewAttached() && page == 1) {
            mView.showIndicator();
        }

        if (mResourceHelper != null) {
            Call<List<VacancyModel>> call = StartApplication
                    .get(mResourceHelper.getContext()).getRetrofitService()
                    .getAllVacancies(AppConstants.LOGIN, AppConstants.F, AppConstants.COUNT, page);

            if (isViewAttached()) {
                call.enqueue(new Callback<List<VacancyModel>>() {
                    @Override
                    public void onResponse(Call<List<VacancyModel>> call, Response<List<VacancyModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<VacancyModel> favoriteVacancyModels = mSQLiteHelper.getAllFavVacancies();
                            ArrayList<VacancyModel> fetchedVacancyModels = (ArrayList<VacancyModel>) response.body();
                            for (int i = 0; i < fetchedVacancyModels.size(); i++) {
                                for (VacancyModel model : favoriteVacancyModels) {
                                    if (model.getPid().equals(fetchedVacancyModels.get(i).getPid())) {
                                        fetchedVacancyModels.get(i).setChecked(true);
                                    }
                                }
                            }
                            mView.onBackGetVacancies(fetchedVacancyModels);
                            if (mView.isIndicatorShown()) {
                                mView.hideIndicator();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<VacancyModel>> call, Throwable t) {
                        mView.showError(t.getMessage());
                        if (mView.isIndicatorShown()) {
                            mView.hideIndicator();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void saveFavoriteVacancy(VacancyModel vacancyModel) {
       mSQLiteHelper.saveSingleFavVacancy(vacancyModel);
    }

    @Override
    public void deleteFavoriteVacancy(String pid) {
        mSQLiteHelper.deleteSingleFavVacancy(pid);
    }
}
