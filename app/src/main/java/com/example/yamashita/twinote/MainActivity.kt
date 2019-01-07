package com.example.yamashita.twinote

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    init {
        val builder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(BuildConfig.TWITTER_API_KEY)
        builder.setOAuthConsumerSecret(BuildConfig.TWITTER_API_SECRET)
        val configuration = builder.build()
        val factory = TwitterFactory(configuration)
        twitter = factory.instance
        val accessToken = AccessToken(BuildConfig.TWITTER_ACCESS_TOKEN, BuildConfig.TWITTER_ACCESS_TOKEN_SECRET)
        twitter.oAuthAccessToken = accessToken
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val twitter = TwitterFactory().instance
        twitter.setOAuthConsumer(BuildConfig.TWITTER_API_KEY, BuildConfig.TWITTER_API_SECRET)
        val accessToken = AccessToken(BuildConfig.TWITTER_ACCESS_TOKEN, BuildConfig.TWITTER_ACCESS_TOKEN_SECRET)
        twitter.oAuthAccessToken = accessToken

        if(Intent.ACTION_SEND == intent.action){
            val uri = intent.extras?.getCharSequence(Intent.EXTRA_TEXT)?.toString()
            val tweetId = intent.extras?.getLong("tweet_id")
            if(uri != null){
                val intent = Intent(this, IntentActivity::class.java)
                val urlPattern = Pattern.compile("(http|https)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?\$"
                    , Pattern.CASE_INSENSITIVE)
                val matcher = urlPattern.matcher(uri)
                if(matcher.find()){
                    intent.putExtra("uri", matcher.group().split("?")[0])
                    intent.putExtra("tweetId", tweetId)
                    startActivity(intent)
                }

            }
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        lateinit var twitter : Twitter
    }
}
