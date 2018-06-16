package com.example.laboratorytwoau.ui.suitable;

import android.util.Log;

import com.example.laboratorytwoau.StartApplication;
import com.example.laboratorytwoau.config.AppConstants;
import com.example.laboratorytwoau.data.PreferencesHelper;
import com.example.laboratorytwoau.data.ResourceHelper;
import com.example.laboratorytwoau.data.SQLiteHelper;
import com.example.laboratorytwoau.data.entities.VacancyModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuitableVacanciesPresenter implements SuitableVacanciesContract.Presenter {
    private ResourceHelper mResourceHelper;
    private SuitableVacanciesContract.View mView;
    private SQLiteHelper mSQLiteHelper;
    private PreferencesHelper mPreferencesHelper;

    public SuitableVacanciesPresenter(ResourceHelper resourceHelper) {
        mResourceHelper = resourceHelper;
        mSQLiteHelper = new SQLiteHelper(mResourceHelper.getContext());
        mPreferencesHelper = new PreferencesHelper(mResourceHelper.getContext());
    }

    @Override
    public void bind(SuitableVacanciesContract.View view) {
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
        if (mPreferencesHelper.getProfessionName().isEmpty()) {
            mView.onBackGetVacanciesNoProfession();
            return;
        }

        if (isViewAttached() && page == 1) {
            mView.showIndicator();
        }

        if (mResourceHelper != null) {
            Call<List<VacancyModel>> call = StartApplication
                    .get(mResourceHelper.getContext()).getRetrofitService()
                    .searchVacanciesByProfession(AppConstants.LOGIN, AppConstants.F_SEARCH,
                            AppConstants.COUNT, page, mPreferencesHelper.getProfessionName());

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
                        Log.d("MY_LOG", t.getMessage());
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
