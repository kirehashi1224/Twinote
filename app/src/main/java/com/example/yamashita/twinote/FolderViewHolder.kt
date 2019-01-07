package com.example.yamashita.twinote

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class FolderViewHolder(v: View): RecyclerView.ViewHolder(v){
    val base = v
    val folderName: TextView = v.findViewById(R.id.folder_name)
}