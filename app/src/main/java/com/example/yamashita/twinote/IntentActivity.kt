package com.example.yamashita.twinote

import android.content.ContentValues
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.EditText
import com.example.yamashita.twinote.db.TweetOpenHelper
import com.example.yamashita.twinote.db.TweetOpenHelper.Companion.FOLDER_TABLE
import com.example.yamashita.twinote.model.Folder
import com.example.yamashita.twinote.model.Tweet
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_intent.*
import twitter4j.Status


class IntentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent)

        val tweetId = intent.getLongExtra("tweetId", 0)
        val uri = intent.getStringExtra("uri")

        object : AsyncTask<Void, Void, Status>() {
            override fun doInBackground(vararg params: Void): twitter4j.Status? {
                try {
                    Log.d("tweetid", tweetId.toString())
                    return MainActivity.twitter.showStatus(tweetId)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return null
                }

            }

            override fun onPostExecute(status: twitter4j.Status?) {
                if (status != null) {
                    val tweet = Tweet(uri, status.user.screenName, status.text)

                    val builder = StringBuilder()
                    builder.append("user:").append(tweet.user).append(" / ").append(tweet.text)
                    // tweetTest.text = builder.toString()
                    tweet_uri.text = tweet.uri
                    tweet_content.text = builder.toString()

                    val db = TweetOpenHelper(applicationContext).writableDatabase
                    val cursor = db.query(FOLDER_TABLE, arrayOf("folder_name", "_id"), null, null, null, null, null, null)
                    val folderList = mutableListOf<Folder>()
                    if(cursor.moveToFirst()){
                        do{
                            folderList.add(Folder(cursor.getString((cursor.getColumnIndex("folder_name"))),
                                cursor.getString(cursor.getColumnIndex("_id"))))
                        }while (cursor.moveToNext())
                    }

                    val recyclerView = folder_recycler
                    val llManager = LinearLayoutManager(applicationContext)
                    llManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView.layoutManager = llManager
                    val adapter = FolderAdapter(folderList, tweet, this@IntentActivity)
                    recyclerView.adapter = adapter

                    add_folder.setOnClickListener {
                        val editView = EditText(this@IntentActivity)
                        AlertDialog.Builder(this@IntentActivity)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("フォルダ名を入力してください")
                            .setView(editView)
                            .setPositiveButton("OK") { dialog, whichButton ->
                                val newFolder = ContentValues().also {
                                    it.put("folder_name", editView.text.toString())
                                }
                                val id = db.insert(FOLDER_TABLE, null, newFolder)
                                folderList.add(Folder(editView.text.toString(), id.toString()))
                                adapter.notifyItemInserted(folderList.size-1)
                            }
                            .setNegativeButton("キャンセル") { dialog, whichButton -> }
                            .show()
                    }
                }
            }
        }.execute()
    }
}
