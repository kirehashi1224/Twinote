package com.example.yamashita.twinote

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_intent.*
import twitter4j.Status


class IntentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent)

        val tweetId = intent.getLongExtra("tweetId", 0)

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
                    val builder = StringBuilder()
                    builder.append("user:").append(status.user.screenName).append(" / ").append(status.text)
                    tweetTest.text = builder.toString()
                }
            }
        }.execute()

        /*callSingle(tweetId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterSuccess {
                val builder = StringBuilder()
                builder.append("user:").append(it.user.screenName).append(" / ").append(it.text)
                tweetTest.text = builder.toString()
            }
            .doOnError {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                it.printStackTrace(pw)
                Log.e("twinote", sw.toString())
            }
            .subscribe()*/

        /*Single.create(SingleOnSubscribe<Status> {
            val status = MainActivity.twitter.showStatus(tweetId)
            try{
                it.onSuccess(status)
            }catch(e: Exception){
                it.onError((Throwable("Fail to get status")))
            }
        }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                val builder = StringBuilder()
                builder.append("user:").append(it.user.screenName).append(" / ").append(it.text)
                tweetTest.text = builder.toString()
            }
            .doOnError {
                Log.v("Twinote", it.message)
            }
            .subscribe()*/
    }

    fun callSingle(tweetId: Long): Single<Status> {
        return Single.create {

            try{
                val status = MainActivity.twitter.showStatus(tweetId)
                it.onSuccess(status)
            }catch(e: Exception){
                it.onError((Throwable("Fail to get status")))
            }
        }
    }
}
