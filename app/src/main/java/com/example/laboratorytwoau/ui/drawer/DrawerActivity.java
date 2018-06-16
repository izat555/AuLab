package com.example.laboratorytwoau.ui.drawer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.laboratorytwoau.BuildConfig;
import com.example.laboratorytwoau.R;
import com.example.laboratorytwoau.ui.favorite.FavoriteActivity;
import com.example.laboratorytwoau.ui.search.SearchActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public abstract class DrawerActivity extends AppCompatActivity {
    private Activity mActivity;

    public DrawerActivity() {  }

    public void initDrawer(Activity activity, Toolbar toolbar) {
        if (activity == null) {
            return;
        }
        mActivity = activity;
        new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .addDrawerItems(getDrawerItems())
                .withOnDrawerItemClickListener(mOnDrawerItemClickListener)
                .withAccountHeader(getAccountHeader())
                .build();
    }

    private IDrawerItem[] getDrawerItems() {
        String[] itemNames = mActivity.getResources().getStringArray(R.array.drawer_item_titles);
        IDrawerItem[] iDrawerItems = new IDrawerItem[itemNames.length];

        for (int i = 0; i < itemNames.length; i++) {
            iDrawerItems[i] = new PrimaryDrawerItem()
                    .withName(itemNames[i])
                    .withIdentifier(10 + i);
        }

        return iDrawerItems;
    }

    private Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener
            = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            switch ((int) drawerItem.getIdentifier()) {
                case 10:
                    Intent searchIntent = new Intent(mActivity.getApplicationContext(), SearchActivity.class);
                    startActivity(searchIntent);
                    break;
                case 11:
                    Intent favoriteIntent = new Intent(mActivity.getApplicationContext(), FavoriteActivity.class);
                    startActivityForResult(favoriteIntent, 100);
                    break;
                case 12:
                    mActivity.finish();
                    break;
            }

            return false;
        }
    };

    private AccountHeader getAccountHeader() {
        return new AccountHeaderBuilder()
                .withActivity(mActivity)
                .withHeaderBackground(R.color.colorToolbar)
                .addProfiles(new ProfileDrawerItem()
                        .withIcon(R.drawable.logo)
                        .withName(getString(R.string.all_vacancies_in_kg))
                        .withEmail(String.format("%s %s", getString(R.string.version), BuildConfig.VERSION_NAME))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();
    }
}
