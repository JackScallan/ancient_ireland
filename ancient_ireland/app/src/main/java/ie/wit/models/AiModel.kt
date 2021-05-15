package ie.wit.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class AiModel(
    var uid: String? = "",
    var ratingtype: String = "N/A",
    var amount: Int = 0,
    var message: String = "a message",
    var condition: String = "poor",
    var site: String = "new",
    var profilepic: String = "",
    var isicon: Boolean = false,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var email: String? = "joe@bloggs.com")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "ratingtype" to ratingtype,
            "amount" to amount,
            "message" to message,
            "condition" to condition,
            "site" to site,
            "profilepic" to profilepic,
            "isicon" to isicon,
            "latitude" to latitude,
            "longitude" to longitude,
            "email" to email
        )
    }
}


