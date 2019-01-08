package com.example.yamashita.twinote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.yamashita.twinote.db.TweetOpenHelper
import com.example.yamashita.twinote.model.Tweet
import kotlinx.android.synthetic.main.activity_folder_content.*

class FolderContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_content)

        val recipeDetailIntent = intent
        val folderName = recipeDetailIntent.getStringExtra("folder")

        val db = TweetOpenHelper(this).writableDatabase
        val cursor = db.query(TweetOpenHelper.TWEET_TABLE, arrayOf("user", "uri", "text", "_id"), "folder == ?", arrayOf(folderName), null, null, null, null)
        val tweetList = mutableListOf<Tweet>()
        if(cursor.moveToFirst()){
            do {
                tweetList.add(Tweet(uri = cursor.getString(cursor.getColumnIndex("uri")),
                    user = cursor.getString(cursor.getColumnIndex("user")),
                    text = cursor.getString(cursor.getColumnIndex("text")),
                    id = cursor.getString(cursor.getColumnIndex("_id"))))
            }while (cursor.moveToNext())

            val recyclerView = folder_content_recycler
            val llManager = LinearLayoutManager(applicationContext)
            llManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = llManager
            val adapter = FolderContentAdapter(tweetList)
            recyclerView.adapter = adapter
        }
    }
}
