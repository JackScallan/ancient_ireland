package ie.wit.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ie.wit.R
import ie.wit.main.AiApp
import org.jetbrains.anko.startActivity

class Login : AppCompatActivity(), View.OnClickListener {

    lateinit var app: AiApp

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as AiApp

        checkSignedIn()
    }

    private fun checkSignedIn() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            app.currentUser = FirebaseAuth.getInstance().currentUser!!
            app.database = FirebaseDatabase.getInstance().reference
            app.storage = FirebaseStorage.getInstance().reference
            startActivity<Home>()
        }
        else
            createSignInIntent()
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        val customLayout = AuthMethodPickerLayout
            .Builder(R.layout.login)
            .setGoogleButtonId(R.id.googleSignInButton)
            .setEmailButtonId(R.id.emailSignInButton)
            .setPhoneButtonId(R.id.phoneSignInButton)
            .build()

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false,true)
                .setTheme(R.style.FirebaseLoginTheme)
                .setAuthMethodPickerLayout(customLayout)
                .build(),
            123)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                app.currentUser = FirebaseAuth.getInstance().currentUser!!
                app.database = FirebaseDatabase.getInstance().reference
                app.storage = FirebaseStorage.getInstance().reference

                startActivity<Home>()
            } else {
                startActivity<Login>()
            }
        }
    }

    override fun onClick(v: View) { createSignInIntent() }

}
