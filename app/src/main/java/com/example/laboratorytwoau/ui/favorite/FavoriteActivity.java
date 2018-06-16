package com.example.laboratorytwoau.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.laboratorytwoau.R;
import com.example.laboratorytwoau.data.ResourceHelper;
import com.example.laboratorytwoau.data.SQLiteHelper;
import com.example.laboratorytwoau.data.entities.VacancyModel;
import com.example.laboratorytwoau.ui.IOnClickCallback;
import com.example.laboratorytwoau.ui.adapters.VacanciesAdapter;
import com.example.laboratorytwoau.ui.browse.BrowseActivity;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements IOnClickCallback {
    private Toolbar mToolbar;
    private SQLiteHelper mSQLiteHelper;
    private ArrayList<VacancyModel> mVacancyModels;
    private RecyclerView mRvFavorites;
    private VacanciesAdapter mVacanciesAdapter;
    private TextView mTvMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mSQLiteHelper = new SQLiteHelper(this);
        mVacancyModels = mSQLiteHelper.getAllFavVacancies();
        for (VacancyModel model : mVacancyModels) {
            model.setChecked(true);
        }

        mTvMsg = findViewById(R.id.tv_message);
        if (mVacancyModels.isEmpty()) {
            mTvMsg.setVisibility(View.VISIBLE);
            return;
        }

        mRvFavorites = findViewById(R.id.rv_favorites);
        mRvFavorites.setLayoutManager(new LinearLayoutManager(this));
        mRvFavorites.setHasFixedSize(true);
        mVacanciesAdapter = new VacanciesAdapter(new ResourceHelper(this), this, mVacancyModels);
        mRvFavorites.setAdapter(mVacanciesAdapter);
    }

    @Override
    public void onCheckBoxClicked(boolean isChecked, VacancyModel vacancyModel) {
        if (isChecked) {
            mSQLiteHelper.saveSingleFavVacancy(vacancyModel);
            Snackbar.make(mRvFavorites, R.string.add_to_fav, Snackbar.LENGTH_LONG).show();
        } else {
            mSQLiteHelper.deleteSingleFavVacancy(vacancyModel.getPid());
            Snackbar.make(mRvFavorites, R.string.del_form_fav, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClicked(ArrayList<VacancyModel> vacancyModels, int position) {
        Intent browseIntent = new Intent(this, BrowseActivity.class);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(getString(R.string.vacancies), mVacancyModels);
        setResult(100, intent);
        super.onBackPressed();
    }
}
