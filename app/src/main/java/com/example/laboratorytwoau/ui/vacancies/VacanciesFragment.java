package com.example.laboratorytwoau.ui.vacancies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.laboratorytwoau.R;
import com.example.laboratorytwoau.data.ResourceHelper;
import com.example.laboratorytwoau.data.entities.VacancyModel;
import com.example.laboratorytwoau.ui.IOnClickCallback;
import com.example.laboratorytwoau.ui.adapters.VacanciesAdapter;
import com.example.laboratorytwoau.ui.browse.BrowseActivity;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

public class VacanciesFragment extends Fragment implements VacanciesContract.View, IOnClickCallback {
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private RotateLoading mRotateLoading;

    private RecyclerView mRvDayVacancies;
    private VacanciesAdapter mVacanciesAdapter;

    private Context mContext;
    private int mPage;

    private ResourceHelper mResourceHelper;
    private VacanciesPresenter mPresenter;

    @Override
    public void showIndicator() {
        mRotateLoading.start();
    }

    @Override
    public void hideIndicator() {
        mRotateLoading.stop();
    }

    @Override
    public boolean isIndicatorShown() {
        return mRotateLoading.isShown();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new VacanciesPresenter(mResourceHelper = new ResourceHelper(mContext));
        mPresenter.bind(this);
        mPage = 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_vacancies, container, false);

        mSwipyRefreshLayout = view.findViewById(R.id.swipy_refresh_layout);
        mSwipyRefreshLayout.setColorSchemeColors(mContext.getResources().getColor(R.color.colorToolbar));
        mSwipyRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipyRefreshLayout.setRefreshing(false);

        mRvDayVacancies = view.findViewById(R.id.rv_day_vacancies);
        mRvDayVacancies.setLayoutManager(new LinearLayoutManager(mContext));
        mRvDayVacancies.setHasFixedSize(true);
        mVacanciesAdapter = new VacanciesAdapter(mResourceHelper, this, new ArrayList<VacancyModel>());
        mRvDayVacancies.setAdapter(mVacanciesAdapter);

        mRotateLoading = view.findViewById(R.id.progress_bar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getVacancies(mPage++);
    }

    @Override
    public void onBackGetVacancies(ArrayList<VacancyModel> vacancyModels) {
        mVacanciesAdapter.addVacancyModels(vacancyModels);
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    private SwipyRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipyRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh(SwipyRefreshLayoutDirection direction) {
            mSwipyRefreshLayout.setRefreshing(true);

            mSwipyRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipyRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.getVacancies(mPage++);
                            mSwipyRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            });
        }
    };

    @Override
    public void onCheckBoxClicked(boolean isChecked, VacancyModel vacancyModel) {
        if (isChecked) {
            Snackbar.make(mRvDayVacancies, R.string.add_to_fav, Snackbar.LENGTH_LONG).show();
            mPresenter.saveFavoriteVacancy(vacancyModel);
        } else {
            Snackbar.make(mRvDayVacancies, R.string.del_form_fav, Snackbar.LENGTH_LONG).show();
            mPresenter.deleteFavoriteVacancy(vacancyModel.getPid());
        }
    }

    @Override
    public void onItemClicked(ArrayList<VacancyModel> vacancyModels, int position) {
        Intent browseIntent = new Intent(mContext, BrowseActivity.class);
        browseIntent.putParcelableArrayListExtra(getString(R.string.vacancies), vacancyModels);
        browseIntent.putExtra(getString(R.string.position), position);
        startActivityForResult(browseIntent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            mVacanciesAdapter.setVacancyModels(data.<VacancyModel>getParcelableArrayListExtra(getString(R.string.vacancies)));
        }
    }
}
