package com.example.laboratorytwoau.data.network;

import com.example.laboratorytwoau.data.entities.VacancyModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("mobile-api.php")
    Call<List<VacancyModel>> getAllVacancies(@Field("login") String login,
                                             @Field("f") String f,
                                             @Field("limit") int limit,
                                             @Field("page") int page);

    @FormUrlEncoded
    @POST("mobile-api.php")
    Call<List<VacancyModel>> searchVacanciesByProfession(@Field("login") String login,
                                                         @Field("f") String f,
                                                         @Field("limit") int limit,
                                                         @Field("page") int page,
                                                         @Field("search_str") String prof);
}
