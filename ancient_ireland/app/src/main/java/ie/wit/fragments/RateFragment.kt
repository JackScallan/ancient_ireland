package ie.wit.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import ie.wit.main.AiApp
import ie.wit.models.AiModel
import ie.wit.utils.*
import kotlinx.android.synthetic.main.fragment_rate.*
import kotlinx.android.synthetic.main.fragment_rate.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.lang.String.format
import java.util.HashMap


class RateFragment : Fragment(), AnkoLogger {

    lateinit var app: AiApp
    var totalRating = 0
    lateinit var loader : AlertDialog
    lateinit var eventListener : ValueEventListener
    var icon = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as AiApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_rate, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_rate)

        root.progressBar.max = 90000
        root.agePicker.minValue = 1770
        root.agePicker.maxValue = 1970


        root.agePicker.setOnValueChangedListener { picker, oldVal, newVal ->
            root.ratingAmount.setText("$newVal")
        }
        setSettingButtonListener(root)
        setIconListener(root)
        return root;
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            RateFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    fun setSettingButtonListener( layout: View) {
        layout.rateButton.setOnClickListener {
            val amount = if (layout.ratingAmount.text.isNotEmpty())
                layout.ratingAmount.text.toString().toInt() else layout.agePicker.value
            if(totalRating >= layout.progressBar.max)
                activity?.toast("Rating Exceeded!")
            else {
                val ratingmethod = if(layout.ratingMethod.checkedRadioButtonId == R.id.church) "Church"
                else if (layout.ratingMethod.checkedRadioButtonId == R.id.outstanding) "Outstanding"
                else if (layout.ratingMethod.checkedRadioButtonId == R.id.museum) "Museum"
                else if (layout.ratingMethod.checkedRadioButtonId == R.id.palace) "Palace"
                else "Residential"
                writeNewRating(AiModel(ratingtype = ratingmethod, amount = amount,
                    profilepic = app.userImage.toString(),
                    isicon = icon,
                    latitude = app.currentLocation.latitude,
                    longitude = app.currentLocation.longitude,
                    email = app.currentUser?.email))
            }
        }
    }


    fun setIconListener (layout: View) {
        layout.imageicon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (!icon) {
                    layout.imageicon.setImageResource(android.R.drawable.star_big_on)
                    icon = true
                }
                else {
                    layout.imageicon.setImageResource(android.R.drawable.star_big_off)
                    icon = false
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getTotalRating(app.currentUser?.uid)
    }

    override fun onPause() {
        super.onPause()
        if(app.currentUser.uid != null)
            app.database.child("user-ratings")
                    .child(app.currentUser!!.uid)
                    .removeEventListener(eventListener)
    }

    fun writeNewRating(ai: AiModel) {
        showLoader(loader, "Adding Rating to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.currentUser!!.uid
        val key = app.database.child("ratings").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        ai.uid = key
        val ratingValues = ai.toMap()

        val childUpdates = HashMap<String, Any>()
        childUpdates["/ratings/$key"] = ratingValues
        childUpdates["/user-ratings/$uid/$key"] = ratingValues

        app.database.updateChildren(childUpdates)
        hideLoader(loader)
    }

    fun getTotalRating(userId: String?) {
        eventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info("Firebase Rating error : ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                totalRating = 0
                val children = snapshot.children
                children.forEach {
                    val rating = it.getValue<AiModel>(AiModel::class.java)
                    totalRating += rating!!.amount
                }
                progressBar.progress = totalRating
            }
        }

        app.database.child("user-ratings").child(userId!!)
            .addValueEventListener(eventListener)
    }
}
