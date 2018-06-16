package com.example.laboratorytwoau.data;

import android.content.Context;

public class ResourceHelper {
    private Context mContext;

    public ResourceHelper(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public String getStringResource(int resId) {
        return mContext.getString(resId);
    }
}
