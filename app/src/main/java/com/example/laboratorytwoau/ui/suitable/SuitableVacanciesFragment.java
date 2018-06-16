package com.example.laboratorytwoau.ui.suitable;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laboratorytwoau.ui.IDialog;
import com.example.laboratorytwoau.R;
import com.example.laboratorytwoau.data.ResourceHelper;
import com.example.laboratorytwoau.data.entities.VacancyModel;
import com.example.laboratorytwoau.ui.IOnClickCallback;
import com.example.laboratorytwoau.ui.adapters.VacanciesAdapter;
import com.example.laboratorytwoau.ui.browse.BrowseActivity;
import com.example.laboratorytwoau.ui.prof_dialog.ProfDialogFragment;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class SuitableVacanciesFragment extends Fragment implements SuitableVacanciesContract.View, IOnClickCallback {
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private RotateLoading mRotateLoading;
    private RecyclerView mRvSuitable;
    private VacanciesAdapter mVacanciesAdapter;
    private Context mContext;
    private int mPage;
    private ResourceHelper mResourceHelper;
    private SuitableVacanciesPresenter mPresenter;
    private TextView mTvHint;
    private Button mBtnAddProf;
    private IDialog mDialog;

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
        mDialog = (IDialog) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SuitableVacanciesPresenter(mResourceHelper = new ResourceHelper(mContext));
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
        View view = inflater.inflate(R.layout.fragment_suitable_vacancies, container, false);

        mSwipyRefreshLayout = view.findViewById(R.id.swipy_refresh_layout);
        mSwipyRefreshLayout.setColorSchemeColors(mContext.getResources().getColor(R.color.colorToolbar));
        mSwipyRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipyRefreshLayout.setRefreshing(false);

        mRvSuitable = view.findViewById(R.id.rv_suitable_vacancies);
        mRvSuitable.setLayoutManager(new LinearLayoutManager(mContext));
        mRvSuitable.setHasFixedSize(true);
        mVacanciesAdapter = new VacanciesAdapter(mResourceHelper, this, new ArrayList<VacancyModel>());
        mRvSuitable.setAdapter(mVacanciesAdapter);

        mRotateLoading = view.findViewById(R.id.progress_bar);

        mTvHint = view.findViewById(R.id.tv_hint);
        mBtnAddProf = view.findViewById(R.id.btn_add_prof);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getVacancies(mPage++);

        mBtnAddProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.callDialog(mContext.getString(R.string.profession));
            }
        });
    }

    @Override
    public void onBackGetVacancies(ArrayList<VacancyModel> vacancyModels) {
        mVacanciesAdapter.addVacancyModels(vacancyModels);
        if (mTvHint.getVisibility() == View.VISIBLE || mBtnAddProf.getVisibility() == View.VISIBLE) {
            mTvHint.setVisibility(View.GONE);
            mBtnAddProf.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackGetVacanciesNoProfession() {
        mPage = 1;
        mTvHint.setVisibility(View.VISIBLE);
        mBtnAddProf.setVisibility(View.VISIBLE);
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
            Snackbar.make(mRvSuitable, R.string.add_to_fav, Snackbar.LENGTH_LONG).show();
            mPresenter.saveFavoriteVacancy(vacancyModel);
        } else {
            Snackbar.make(mRvSuitable, R.string.del_form_fav, Snackbar.LENGTH_LONG).show();
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

    public void refreshData() {
        mPage = 1;
        mRvSuitable.swapAdapter(mVacanciesAdapter = new VacanciesAdapter(mResourceHelper, this, new ArrayList<VacancyModel>()), true);
        mPresenter.getVacancies(mPage++);
        //mVacanciesAdapter.notifyDataSetChanged();
    }
}
