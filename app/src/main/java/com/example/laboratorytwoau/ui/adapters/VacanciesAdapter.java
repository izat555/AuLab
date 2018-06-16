package com.example.laboratorytwoau.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.laboratorytwoau.R;
import com.example.laboratorytwoau.data.ResourceHelper;
import com.example.laboratorytwoau.data.entities.VacancyModel;
import com.example.laboratorytwoau.ui.IOnClickCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VacanciesAdapter extends RecyclerView.Adapter {
    private List<VacancyModel> mVacancyModels;
    private ResourceHelper mResourceHelper;
    private IOnClickCallback mCallback;

    private class VacancyHolder extends RecyclerView.ViewHolder {
        private TextView mTvProfession;
        private TextView mTvDate;
        private TextView mTvVacancyDescription;
        private TextView mTvVacancySalary;
        private CheckBox mCbFavorites;

        public VacancyHolder(View itemView) {
            super(itemView);

            mTvProfession = itemView.findViewById(R.id.tv_profession);
            mTvDate = itemView.findViewById(R.id.tv_date);
            mTvVacancyDescription = itemView.findViewById(R.id.tv_description);
            mTvVacancySalary = itemView.findViewById(R.id.tv_salary);
            mCbFavorites = itemView.findViewById(R.id.cb_favorites);

            mCbFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VacancyModel vacancyModel = mVacancyModels.get(getAdapterPosition());
                    if (vacancyModel.isChecked()) {
                        vacancyModel.setChecked(false);
                    } else {
                        vacancyModel.setChecked(true);
                    }
                    notifyDataSetChanged();
                    mCallback.onCheckBoxClicked(vacancyModel.isChecked(), vacancyModel);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onItemClicked((ArrayList<VacancyModel>) mVacancyModels, getAdapterPosition());
                }
            });
        }
    }

    public VacanciesAdapter(ResourceHelper resourceHelper, IOnClickCallback callback, List<VacancyModel> vacancyModels) {
        mResourceHelper = resourceHelper;
        mCallback = callback;
        mVacancyModels = vacancyModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        VacancyHolder vacancyHolder = new VacancyHolder(view);
        return vacancyHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VacancyHolder vacancyHolder = (VacancyHolder) holder;

        vacancyHolder.mTvProfession.setText(mVacancyModels.get(position).getProfession());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(mVacancyModels.get(position).getUpdateDate());
            vacancyHolder.mTvDate.setText(new SimpleDateFormat("HH:mm dd MMM yyyy")
                    .format(new Date(date.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        vacancyHolder.mTvVacancyDescription.setText(mVacancyModels.get(position).getBody());

        String salary = mVacancyModels.get(position).getSalary();
        if (mResourceHelper != null) {
            vacancyHolder.mTvVacancySalary
                    .setText(salary.isEmpty() ? mResourceHelper.getContext().getString(R.string.negotiation) : salary);
        }

        if (mVacancyModels.get(position).isChecked()) {
            vacancyHolder.mCbFavorites.setButtonDrawable(R.drawable.iv_fav_45_45);
            vacancyHolder.mCbFavorites.setChecked(true);
        } else {
            vacancyHolder.mCbFavorites.setButtonDrawable(R.drawable.iv_fav_default_45_45);
            vacancyHolder.mCbFavorites.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mVacancyModels.size();
    }

    public void addVacancyModels(List<VacancyModel> vacancyModels) {
        mVacancyModels.addAll(vacancyModels);
        notifyDataSetChanged();
    }

    public void setVacancyModels(List<VacancyModel> vacancyModels) {
        if (vacancyModels.isEmpty()) {
            return;
        }

        for (int i = 0; i < mVacancyModels.size(); i++) {
            for (VacancyModel model : vacancyModels) {
                if (model.getPid().equals(mVacancyModels.get(i).getPid())) {
                    mVacancyModels.get(i).setChecked(vacancyModels.get(i).isChecked());
                }
            }
        }
        notifyDataSetChanged();
    }
}
