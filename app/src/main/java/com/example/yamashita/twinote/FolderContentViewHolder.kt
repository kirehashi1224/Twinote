package jp.ac.titech.itpro.sdl.cooknote

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.yamashita.twinote.R

class FolderContentViewHolder(v: View): RecyclerView.ViewHolder(v){
    val base = v
    val tweetContent: TextView = v.findViewById(R.id.tweet_content)
    val tweetAuthor: TextView = v.findViewById(R.id.tweet_author)
}