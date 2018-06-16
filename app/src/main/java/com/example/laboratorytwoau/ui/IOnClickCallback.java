package com.example.laboratorytwoau.ui;

import com.example.laboratorytwoau.data.entities.VacancyModel;

import java.util.ArrayList;

public interface IOnClickCallback {
    void onCheckBoxClicked(boolean isChecked, VacancyModel vacancyModel);
    void onItemClicked(ArrayList<VacancyModel> vacancyModels, int position);
}
