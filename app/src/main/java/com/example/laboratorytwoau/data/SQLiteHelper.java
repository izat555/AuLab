package com.example.laboratorytwoau.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.laboratorytwoau.data.entities.VacancyModel;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "AU_TEST_DB";
    private static final int DB_VERSION = 5;

    private static final String TABLE_FAV_NAME = "TABLE_FAVORITE";
    private static final String PID = "PID";
    private static final String PROFESSION = "PROFESSION";
    private static final String DATE = "DATE";
    private static final String HEADER = "HEADER";
    private static final String SALARY = "SALARY";
    private static final String BODY = "BODY";
    private static final String TEL = "TEL";
    private static final String SITE = "SITE";
    private static final String LINK = "LINK";
    private static final String PROFILE = "PROFILE";
    private static final String RATING = "RATING";

    private static final String CREATE_FAV_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FAV_NAME + "("
            + PID + " TEXT_PRIMARY_KEY, "
            + PROFESSION + " TEXT, "
            + DATE + " TEXT, "
            + HEADER + " TEXT, "
            + SALARY + " TEXT, "
            + BODY + " TEXT, "
            + TEL + " TEXT, "
            + SITE + " TEXT, "
            + LINK + " TEXT, "
            + PROFILE + " TEXT, "
            + RATING + " INTEGER);";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV_NAME);
        onCreate(db);
    }

    public long saveSingleFavVacancy(VacancyModel vacancyModel) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PID, vacancyModel.getPid());
        contentValues.put(PROFESSION, vacancyModel.getProfession());
        contentValues.put(DATE, vacancyModel.getUpdateDate());
        contentValues.put(HEADER, vacancyModel.getHeader());
        contentValues.put(SALARY, vacancyModel.getSalary());
        contentValues.put(BODY, vacancyModel.getBody());
        contentValues.put(TEL, vacancyModel.getTelephone());
        contentValues.put(SITE, vacancyModel.getSiteAddress());
        contentValues.put(LINK, vacancyModel.getLink());
        contentValues.put(PROFILE, vacancyModel.getProfile());
        contentValues.put(RATING, vacancyModel.getRaiting());

        long rowInserted = database.insert(TABLE_FAV_NAME, null, contentValues);
        database.close();

        return rowInserted;
    }

    public int deleteSingleFavVacancy(String pId) {
        SQLiteDatabase database = getWritableDatabase();

        int countDel = database.delete(TABLE_FAV_NAME, String.format("%s=%s", PID, pId), null);
        database.close();

        return countDel;
    }

    public ArrayList<VacancyModel> getAllFavVacancies() {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<VacancyModel> vacancyModels = new ArrayList<>();
        Cursor cursor = database.query(TABLE_FAV_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                VacancyModel vacancyModel = new VacancyModel();

                vacancyModel.setPid(cursor.getString(cursor.getColumnIndex(PID)));
                vacancyModel.setProfession(cursor.getString(cursor.getColumnIndex(PROFESSION)));
                vacancyModel.setUpdateDate(cursor.getString(cursor.getColumnIndex(DATE)));
                vacancyModel.setHeader(cursor.getString(cursor.getColumnIndex(HEADER)));
                vacancyModel.setSalary(cursor.getString(cursor.getColumnIndex(SALARY)));
                vacancyModel.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                vacancyModel.setTelephone(cursor.getString(cursor.getColumnIndex(TEL)));
                vacancyModel.setSiteAddress(cursor.getString(cursor.getColumnIndex(SITE)));
                vacancyModel.setLink(cursor.getString(cursor.getColumnIndex(LINK)));
                vacancyModel.setProfile(cursor.getString(cursor.getColumnIndex(PROFILE)));
                vacancyModel.setRaiting(cursor.getInt(cursor.getColumnIndex(RATING)));

                vacancyModels.add(vacancyModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return vacancyModels;
    }
}
