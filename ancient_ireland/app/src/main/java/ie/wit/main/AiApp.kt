package ie.wit.main

import android.app.Application
import android.location.Location
import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AiApp : Application(), AnkoLogger {

    lateinit var currentUser: FirebaseUser
    lateinit var database: DatabaseReference
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var storage: StorageReference
    lateinit var userImage: Uri
    lateinit var currentLocation : Location
    lateinit var locationClient : FusedLocationProviderClient
    lateinit var mMap : GoogleMap
    lateinit var marker : Marker

    override fun onCreate() {
        super.onCreate()
        info("Ancient Ireland App started")
    }
}

