package com.example.yamashita.twinote

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.yamashita.twinote.db.TweetOpenHelper
import com.example.yamashita.twinote.db.TweetOpenHelper.Companion.TWEET_TABLE
import com.example.yamashita.twinote.model.Folder
import com.example.yamashita.twinote.model.Tweet

class FolderAdapter(folders: List<Folder>, tweet: Tweet, context: Context): RecyclerView.Adapter<FolderViewHolder>(){
    private val folderList = folders
    private val tweet = tweet
    private val activity = context as Activity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_layout, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folderName = folderList[position].folder_name
        val id = folderList[position].id
        holder.folderName.text = folderName
        holder.base.setOnClickListener {
            val db = TweetOpenHelper(holder.base.context).writableDatabase
            val contentValues = ContentValues().also {
                it.put("uri", tweet.uri)
                it.put("user", tweet.user)
                it.put("text", tweet.text)
                it.put("folder", folderName)
            }
            db.insert(TWEET_TABLE, null, contentValues)
            Toast.makeText(it.context, "${folderName}に登録しました", Toast.LENGTH_SHORT).show()
            activity.finishAndRemoveTask()
        }
        holder.base.setOnLongClickListener {
            val items = arrayOf("削除")
            AlertDialog.Builder(it.context)
                .setTitle("設定")
                .setItems(items) { dialog, which ->
                    when(which){
                        0 ->{
                            val db = TweetOpenHelper(it.context).writableDatabase
                            db.delete(TweetOpenHelper.FOLDER_TABLE, "_id = $id", null)
                            this.notifyDataSetChanged()
                            val intent = Intent(it.context, MainActivity::class.java)
                            it.context.startActivity(intent)
                        }
                    }
                }.show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return folderList.size
    }
}