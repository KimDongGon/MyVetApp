package com.example.myvetapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.identity.TwitterLoginButton
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import javax.security.auth.callback.Callback

class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null
    var callbackManager: CallbackManager?= null
    val GOOGLE_LOGIN_CODE = 9001
//    var twitterAuthClient: TwitterAuthClient? = null
//    var twitterauthconfig:TwitterAuthConfig? = null
//    var twitterConfig: TwitterConfig? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        twitterauthconfig = TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
//        getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET))
//        twitterConfig = TwitterConfig.Builder(this)
//            .twitterAuthConfig(twitterauthconfig)
//            .build()
//        Twitter.initialize(twitterConfig)
//        Twitter.initialize(this)
        setContentView(R.layout.activity_login)
        init()
    }
    fun init() {
        auth = FirebaseAuth.getInstance()

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        callbackManager = CallbackManager.Factory.create()

//        twitterAuthClient = TwitterAuthClient()

        email_login_button.setOnClickListener {
            if(email_editText.text.toString().isNullOrEmpty() || password_editText.text.toString().isNullOrEmpty()){
                Toast.makeText(this, getString(R.string.signout), Toast.LENGTH_SHORT).show()
            }
            else{
                progress_bar.visibility = View.VISIBLE
                createAndLoginEmail()
            }
        }
        google_sign_in_button.setOnClickListener{
            progress_bar.visibility = View.VISIBLE
            var signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
        }
        facebook_login_button.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager, object:
                FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    progress_bar.visibility = View.GONE
                }

                override fun onError(error: FacebookException?) {
                    progress_bar.visibility = View.GONE
                }

            })
        }
//        twitter_login_button.setOnClickListener {
//            progress_bar.visibility = View.VISIBLE
//            twitterAuthClient?.authorize(this, object : com.twitter.sdk.android.core.Callback<TwitterSession>() {
//                override fun success(result: Result<TwitterSession>?) {
//                    Log.d("test", "success")
//                    val credential = TwitterAuthProvider.getCredential(
//                        result?.data?.authToken?.token!!,
//                        result?.data?.authToken?.secret!!)
//                    auth?.signInWithCredential(credential)?.addOnCompleteListener { task ->
//                        progress_bar.visibility = View.GONE
//                        if (task.isSuccessful){
//                            moveMainPage(auth?.currentUser)
//                        }
//                    }
//                }
//
//                override fun failure(exception: TwitterException?) {
//                    Log.d("test", "fail")
//                    progress_bar.visibility = View.GONE
//                }
//            })
//        }
    }

    fun moveMainPage(user: FirebaseUser?){
        if(user != null){
            Toast.makeText(this, getString(R.string.signin_complete), Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun createAndLoginEmail(){
        auth?.createUserWithEmailAndPassword(email_editText.text.toString(), password_editText.text.toString())
            ?.addOnCompleteListener { task ->
                progress_bar.visibility = View.GONE
                if(task.isSuccessful){
                    Toast.makeText(this, getString(R.string.signup_complete), Toast.LENGTH_SHORT).show()
                    moveMainPage(auth?.currentUser)
                }
                else if(task.exception?.message.isNullOrEmpty()){
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
                else{
                    signinEmail()
                }
            }
    }

    fun signinEmail(){
        auth?.signInWithEmailAndPassword(email_editText.text.toString(), password_editText.text.toString())
            ?.addOnCompleteListener { task ->
                progress_bar.visibility = View.GONE

                if(task.isSuccessful){
                    moveMainPage(auth?.currentUser)
                }
                else{
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun handleFacebookAccessToken(token: AccessToken){
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                progress_bar.visibility = View.GONE
                if(task.isSuccessful){
                    moveMainPage(auth?.currentUser)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)

//        twitterAuthClient?.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_LOGIN_CODE){
            var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if (result != null) {
                if(result.isSuccess){
                    var account = result.signInAccount
                    firebaseAuthWithGoogle(account!!)
                } else{
                    progress_bar.visibility = View.GONE
                }
            }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                progress_bar.visibility = View.GONE
                if (task.isSuccessful){
                    moveMainPage(auth?.currentUser)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }
}