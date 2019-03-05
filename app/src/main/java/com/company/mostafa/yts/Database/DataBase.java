package com.company.mostafa.yts.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.company.mostafa.yts.Models.Movies;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mostafa on 2/18/2018.
 */

public class DataBase extends SQLiteAssetHelper {
    private static final String DB_NAME = "favo.db";
    private static final int DB_VER = 2;

    public DataBase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    //select from OrderDetain
    public List<Movies> getMovies() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qp = new SQLiteQueryBuilder();
        String[] sqlSelect = {"movie_id","movie_name","movie_rate","movie_img"};
        String sqlTable = "favMovies";
        qp.setTables(sqlTable);
        Cursor c = qp.query(db, sqlSelect, null, null, null, null, null);
        final List<Movies> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Movies(c.getString(c.getColumnIndex("movie_id")),
                        c.getString(c.getColumnIndex("movie_name")),
                        c.getString(c.getColumnIndex("movie_rate")),
                        c.getString(c.getColumnIndex("movie_img"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    //Insert into orderDeatail
    public void addFavo(String movie_id,String movie_name,String movie_rate,String movie_img) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO favMovies (movie_id,movie_name,movie_rate,movie_img) VALUES ('%S','%S','%S','%S'); ",
                movie_id,
                movie_name,
                movie_rate,
                movie_img

        );
        db.execSQL(query);

    }

    public boolean isFavorites(String movie_Id){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM favMovies WHERE movie_id ='%S';",movie_Id);
        Cursor cursor=db.rawQuery(query,null);
        if (cursor.getCount()<=0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }


    public void removeFromFavorites(String movie_Id){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM favMovies  WHERE movie_id = '%S';",movie_Id);
        db.execSQL(query);
    }


}
