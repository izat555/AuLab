package com.example.laboratorytwoau.ui.main;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.laboratorytwoau.data.PreferencesHelper;
import com.example.laboratorytwoau.ui.IDialog;
import com.example.laboratorytwoau.R;
import com.example.laboratorytwoau.data.ResourceHelper;
import com.example.laboratorytwoau.data.entities.TabItemModel;
import com.example.laboratorytwoau.ui.drawer.DrawerActivity;
import com.example.laboratorytwoau.ui.adapters.PagerAdapter;
import com.example.laboratorytwoau.ui.prof_dialog.ProfDialogFragment;
import com.example.laboratorytwoau.ui.suitable.SuitableVacanciesContract;
import com.example.laboratorytwoau.ui.suitable.SuitableVacanciesFragment;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class MainActivity extends DrawerActivity implements MainContract.View, IDialog {
    private Toolbar mToolbar;
    //private ImageButton mImageButtonAction;
    private RotateLoading mRotateLoading;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MainPresenter mPresenter;
    private PreferencesHelper mPreferencesHelper;
    private MenuItem mMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(new ResourceHelper(this));
        mPresenter.bind(this);

        setSupportActionBar(mToolbar = findViewById(R.id.main_toolbar));
        initDrawer(this, mToolbar);

        /*mImageButtonAction = findViewById(R.id.image_btn_action);
        mImageButtonAction.setOnClickListener(mOnClickListener);*/

        mRotateLoading = findViewById(R.id.progress_bar);
        mViewPager = findViewById(R.id.view_pager_main);
        mTabLayout = findViewById(R.id.tab_layout_main);

        mPresenter.setViewPager();

        mPreferencesHelper = new PreferencesHelper(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbind();
    }

    @Override
    public void showIndicator() {
        mRotateLoading.start();
    }

    @Override
    public void hideIndicator() {
        mRotateLoading.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenuItem = menu.findItem(R.id.item_action);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MY_LOG", "onOptionsItemSelected");
        if (item.getTitle().toString().equals(getString(R.string.profession))) {
            callDialog(getString(R.string.profession));
        } else if (item.getTitle().toString().equals(getResources().getDrawable(R.drawable.ic_suitable_white_30_30))) {
            callDialog(getString(R.string.suitable));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initViewPager(ArrayList<TabItemModel> tabItemModels) {
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), tabItemModels));
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                mMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_search_white_30_30));
                mMenuItem.setTitle(getString(R.string.search));
            } else if (position == 1) {
                mMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_suitable_white_30_30));
                mMenuItem.setTitle(getString(R.string.profession));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void callDialog(String str) {
        Log.d("MY_LOG", "callDialog");
        if (str.equals(getString(R.string.profession))) {
            ProfDialogFragment profDialogFragment = new ProfDialogFragment();
            profDialogFragment.setCancelable(false);
            profDialogFragment.show(getSupportFragmentManager(), "DIALOG");
        }
    }

    @Override
    public void onDialogCallback(String str) {
        mPreferencesHelper.saveProfessionName(str);
        SuitableVacanciesFragment fragment = (SuitableVacanciesFragment) getSupportFragmentManager().getFragments().get(1);
        fragment.refreshData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
