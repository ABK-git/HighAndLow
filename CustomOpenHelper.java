package com.example.highandlow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CustomOpenHelper extends SQLiteOpenHelper {
    //データベースの名前
    private static final String DB_NAME = "casino";
    //バージョン管理
    private static final int DB_VERSION = 1;
    //テーブル作成SQL
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE casino("+
                    "name TEXT PRIMARY KEY,"+
                    "chip INTEGER)";

    //コンストラクタ
    public CustomOpenHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //テーブル作成
        db.execSQL(CREATE_TABLE_SQL);
    }

    //チップを保存する
    public void updateChip(String name,int chip) throws SQLException {
        //ContentValuesにデータを入れる
        ContentValues val = new ContentValues();
        val.put("name",name);
        val.put("chip",chip);
        try(SQLiteDatabase db = getWritableDatabase()){
            db.replaceOrThrow(DB_NAME,null,val);
        }
    }

    //チップを読み取る
    public int getChip(String name) throws SQLException{
        try(SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(
                    DB_NAME,new String[]{"chip"},
                    "name == ?",new String[]{name},
                    null,null,null,null
            )){
            if(cursor.moveToFirst()){
                return cursor.getInt(0);
            }
        }
        return 0;
    }

    //登録されているチップを削除する
    public void deleteChip(String name) throws SQLException{
        try(SQLiteDatabase db = getWritableDatabase()){
            db.delete(DB_NAME,"name == ?",new String[]{name});
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
