package com.example.yamashita.twinote

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.yamashita.twinote.db.TweetOpenHelper
import com.example.yamashita.twinote.model.Tweet

class FolderContentAdapter(tweetList: List<Tweet>): RecyclerView.Adapter<FolderContentViewHolder>(){
    private val tweetList = tweetList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_content_layout, parent, false)
        return FolderContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderContentViewHolder, position: Int) {
        holder.tweetContent.text = tweetList[position].text
        holder.tweetAuthor.text = tweetList[position].user
        val id = tweetList[position].id
        holder.base.setOnClickListener {
            val uri = tweetList[position].uri
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            it.context.startActivity(intent)
        }
        holder.base.setOnLongClickListener {
            val items = arrayOf("削除")
            AlertDialog.Builder(it.context)
                    .setTitle("設定")
                    .setItems(items) { dialog, which ->
                        when(which){
                            0 ->{
                                val db = TweetOpenHelper(it.context).writableDatabase
                                db.delete(TweetOpenHelper.TWEET_TABLE, "_id = $id", null)
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
        return tweetList.size
    }
}