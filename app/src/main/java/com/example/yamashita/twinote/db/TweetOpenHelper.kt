package com.example.yamashita.twinote.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TweetOpenHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "tweet_db"
        const val DB_VERSION = 1
        const val TWEET_TABLE = "tweets"
        const val FOLDER_TABLE = "folder"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TWEET_TABLE ("+
                "_id integer primary key autoincrement,"+
                "user text not null,"+
                "uri text not null,"+
                "text text not null,"+
                "folder text not null"+
                ");")
        db?.execSQL("create table $FOLDER_TABLE ("+
                "_id integer primary key autoincrement,"+
                "folder_name text not null"+
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $DB_NAME")
        onCreate(db)
    }
}