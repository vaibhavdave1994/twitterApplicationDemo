package com.e.twitterapplicationdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var twitterAuthClient: TwitterAuthClient
    lateinit var twitterSession: TwitterSession


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authConfig = TwitterAuthConfig(
            this.getString(R.string.twitter_consumer_key),
            this.getString(R.string.twitter_consumer_secret)
        )
        Fabric.with(this, Twitter(authConfig))

        setContentView(R.layout.activity_main)

        twitterAuthClient = TwitterAuthClient()
        login.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                twitterLogin()

            }
        })
    }


    private fun twitterLogin() {
        twitterAuthClient.authorize(this, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                twitterSession = result.data
                Twitter.getApiClient(twitterSession).accountService.verifyCredentials(
                    true,
                    false,
                    object : Callback<User>() {
                        override fun success(userResult: Result<User>) {
                            val currentUser = userResult.data
                            val name = currentUser.name
                            val userName = currentUser.screenName
                            val profilePicture = currentUser.profileImageUrl
                            val twiiterSession =
                                Twitter.getInstance().core.sessionManager.activeSession
                            val userId = twiiterSession.userId.toString()
                            Log.d("name", name)
                            Log.d("userName", userName)
                            Log.d("profilePicture", profilePicture)
                            Log.d("userId", userId)
                            login.visibility = View.GONE
                            logout.visibility = View.VISIBLE
                            Toast.makeText(
                                applicationContext,
                                name + "\n" + userName + "\n" + profilePicture + "\n" + userId,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun failure(e: TwitterException) {

                        }
                    })


            }

            override fun failure(exception: TwitterException) {

            }
        })


    }


    override fun onActivityResult(requestCode: Int, responseCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, responseCode, intent)
        twitterAuthClient.onActivityResult(requestCode, responseCode, intent)
    }
}


