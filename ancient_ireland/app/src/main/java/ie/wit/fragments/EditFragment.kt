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
import ie.wit.utils.createLoader
import ie.wit.utils.hideLoader
import ie.wit.utils.showLoader
import kotlinx.android.synthetic.main.fragment_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditFragment : Fragment(), AnkoLogger {

    lateinit var app: AiApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editAi: AiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as AiApp

        arguments?.let {
            editAi = it.getParcelable("editrating")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_edit, container, false)
        activity?.title = getString(R.string.action_edit)
        loader = createLoader(activity!!)

        root.editYear.setText(editAi!!.amount.toString())
        root.editRatingtype.setText(editAi!!.ratingtype)
        root.editMessage.setText(editAi!!.message)
        root.editCondition.setText(editAi!!.condition)
        root.editSite.setText(editAi!!.site)

        root.editUpdateButton.setOnClickListener {
            showLoader(loader, "Updating Rating on Server...")
            updateRatingData()
            updateRating(editAi!!.uid, editAi!!)
            updateUserRating(app.currentUser!!.uid,
                               editAi!!.uid, editAi!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(ai: AiModel) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editrating",ai)
                }
            }
    }

    fun updateRatingData() {
        editAi!!.amount = root.editYear.text.toString().toInt()
        editAi!!.message = root.editMessage.text.toString()
        editAi!!.condition = root.editCondition.text.toString()
        editAi!!.site = root.editSite.text.toString()
    }

    fun updateUserRating(userId: String, uid: String?, ai: AiModel) {
        app.database.child("user-ratings").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(ai)
                        activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFrame, LocationFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Rating error : ${error.message}")
                    }
                })
    }

    fun updateRating(uid: String?, ai: AiModel) {
        app.database.child("ratings").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(ai)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Rating error : ${error.message}")
                    }
                })
    }
}
