package com.example.yamashita.twinote

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.yamashita.twinote.db.TweetOpenHelper
import com.example.yamashita.twinote.db.TweetOpenHelper.Companion.FOLDER_TABLE
import com.example.yamashita.twinote.model.Folder


class ShowFolderAdapter(folders: List<Folder>): RecyclerView.Adapter<FolderViewHolder>(){
    private val folderList = folders
    init {
        Log.d("FolderAdapter", folderList.toString())
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_layout, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folderName = folderList[position].folder_name
        val id = folderList[position].id
        holder.folderName.text = folderName
        holder.base.setOnClickListener {
            val intent = Intent(it.context, FolderContentActivity::class.java)
            intent.putExtra("folder", folderName)
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
                                db.delete(FOLDER_TABLE, "_id = $id", null)
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