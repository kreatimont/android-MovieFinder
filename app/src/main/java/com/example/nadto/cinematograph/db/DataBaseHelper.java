package com.example.nadto.cinematograph.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nadto.cinematograph.model.Film;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "favorite_db";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE favorite ( id INTEGER PRIMARY KEY, type INTEGER )";
        sqLiteDatabase.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS favorite");
        this.onCreate(sqLiteDatabase);
    }

    public void addFilm(Film film) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("id",film.getId());
        contentValues.put("type",film.getType());
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert("favorite", null, contentValues);
        database.close();

    }

    public boolean isExistFilm(int id, int type) {

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(
                "favorite",
                new String[] {"id","type"},
                "id = ? AND type = ?",
                new String[] {String.valueOf(id), String.valueOf(type)},
                null,null,null,null
        );

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Film> getAllFilm() {

        ArrayList<Film> films = new ArrayList<>();

        String query = "SELECT * FROM " + "favorite";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        Film film;
        if(cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(0);
                int type = cursor.getInt(1);
                film = new Film(id, type);
                films.add(film);

            } while (cursor.moveToNext());

        }
        cursor.close();
        return films;
    }

    public void deleteFilm(int id, int type) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("favorite", "id = ? AND type = ?", new String[] {String.valueOf(id),String.valueOf(type)});
        database.close();
    }
}
