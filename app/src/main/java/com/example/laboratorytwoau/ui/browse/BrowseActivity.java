package com.example.laboratorytwoau.ui.browse;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laboratorytwoau.R;
import com.example.laboratorytwoau.data.SQLiteHelper;
import com.example.laboratorytwoau.data.entities.VacancyModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BrowseActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private LinearLayout mLinearLayoutPrev;
    private LinearLayout mLinearLayoutNext;

    private ArrayList<VacancyModel> mVacancyModels;
    private int mPosition;

    private TextView mTvHeader;
    private TextView mTvProfile;
    private TextView mTvDate;
    private TextView mTvSalary;
    private TextView mTvSite;
    private TextView mTvTelephone;
    private CheckBox mCbFavorites;
    private TextView mTvBody;
    private Button mBtnCall;

    private SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_vacancies));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mVacancyModels = getIntent().getParcelableArrayListExtra(getString(R.string.vacancies));
        mPosition = getIntent().getIntExtra(getString(R.string.position), 0);
        mSQLiteHelper = new SQLiteHelper(this);

        mLinearLayoutPrev = findViewById(R.id.ll_prev);
        mLinearLayoutNext = findViewById(R.id.ll_next);
        mTvHeader = findViewById(R.id.tv_header);
        mTvProfile = findViewById(R.id.tv_profile);
        mTvDate = findViewById(R.id.tv_date);
        mTvSalary = findViewById(R.id.tv_salary);
        mTvSite = findViewById(R.id.tv_site);
        mTvTelephone = findViewById(R.id.tv_telephone);
        mCbFavorites = findViewById(R.id.cb_favorites);
        mTvBody = findViewById(R.id.tv_body);
        mTvBody.setMovementMethod(new ScrollingMovementMethod());
        mBtnCall = findViewById(R.id.btn_call);

        mLinearLayoutPrev.setOnClickListener(this);
        mLinearLayoutNext.setOnClickListener(this);
        mCbFavorites.setOnClickListener(this);
        mBtnCall.setOnClickListener(this);

        setValues();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_prev:
                mPosition--;
                setValues();
                break;
            case R.id.ll_next:
                mPosition++;
                setValues();
                break;
            case R.id.cb_favorites:
                VacancyModel vacancyModel = mVacancyModels.get(mPosition);
                if (vacancyModel.isChecked()) {
                    vacancyModel.setChecked(false);
                    ((CheckBox) v).setButtonDrawable(R.drawable.iv_fav_default_45_45);
                    mSQLiteHelper.deleteSingleFavVacancy(vacancyModel.getPid());
                    Snackbar.make(mBtnCall, R.string.del_form_fav, Snackbar.LENGTH_LONG).show();
                } else {
                    vacancyModel.setChecked(true);
                    ((CheckBox) v).setButtonDrawable(R.drawable.iv_fav_45_45);
                    mSQLiteHelper.saveSingleFavVacancy(vacancyModel);
                    Snackbar.make(mBtnCall, R.string.add_to_fav, Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_call:
                String phone = mVacancyModels.get(mPosition).getTelephone();
                Uri number = Uri.parse("tel:" + phone);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                /*List activities = getPackageManager().queryIntentActivities(callIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                Log.d("MY_LOG", isIntentSafe ? "true" : "false");*/
                startActivity(callIntent);
                break;
        }
    }

    private void setValues() {
        if (!mVacancyModels.isEmpty()) {
            if (mPosition == 0 && mVacancyModels.size() > 1) {
                mLinearLayoutPrev.setVisibility(View.INVISIBLE);
                mLinearLayoutNext.setVisibility(View.VISIBLE);
            } else if (mPosition == mVacancyModels.size() - 1 && mVacancyModels.size() > 1) {
                mLinearLayoutNext.setVisibility(View.INVISIBLE);
                mLinearLayoutPrev.setVisibility(View.VISIBLE);
            } else if (mVacancyModels.size() > 1) {
                mLinearLayoutPrev.setVisibility(View.VISIBLE);
                mLinearLayoutNext.setVisibility(View.VISIBLE);
            }
        }

        VacancyModel vacancyModel = mVacancyModels.get(mPosition);

        mTvHeader.setText(vacancyModel.getHeader());

        mTvProfile.setText(vacancyModel.getProfile());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(vacancyModel.getUpdateDate());
            mTvDate.setText(new SimpleDateFormat("HH:mm dd MMM yyyy").format(new Date(date.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mTvSalary.setText(vacancyModel.getSalary());

        mTvSite.setText(vacancyModel.getSiteAddress());

        mTvTelephone.setText(vacancyModel.getTelephone());

        if (vacancyModel.isChecked()) {
            mCbFavorites.setChecked(true);
            mCbFavorites.setButtonDrawable(R.drawable.iv_fav_45_45);
        } else {
            mCbFavorites.setChecked(false);
            mCbFavorites.setButtonDrawable(R.drawable.iv_fav_default_45_45);
        }

        mTvBody.setText(vacancyModel.getBody());
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
