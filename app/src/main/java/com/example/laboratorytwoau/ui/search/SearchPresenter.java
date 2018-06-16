package com.example.laboratorytwoau.ui.search;

public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View mView;

    @Override
    public void bind(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }
}
