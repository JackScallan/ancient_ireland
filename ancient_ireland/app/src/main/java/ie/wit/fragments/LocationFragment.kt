package ie.wit.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import ie.wit.adapters.AiAdapter
import ie.wit.adapters.LocationListener
import ie.wit.main.AiApp
import ie.wit.models.AiModel
import ie.wit.utils.*
import kotlinx.android.synthetic.main.fragment_location_all.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class LocationFragment : Fragment(), AnkoLogger,
    LocationListener {

    lateinit var app: AiApp
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as AiApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_location_all, container, false)
        activity?.title = getString(R.string.action_location)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))

        var query = FirebaseDatabase.getInstance()
            .reference
            .child("user-ratings").child(app.currentUser.uid)

        var options = FirebaseRecyclerOptions.Builder<AiModel>()
            .setQuery(query, AiModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.recyclerView.adapter = AiAdapter(options, this)

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteRating((viewHolder.itemView.tag as AiModel).uid)
                deleteUserRating(app.currentUser!!.uid,
                    (viewHolder.itemView.tag as AiModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onRatingClick(viewHolder.itemView.tag as AiModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerView)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LocationFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    fun deleteUserRating(userId: String, uid: String?) {
        app.database.child("user-ratings").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Rating error : ${error.message}")
                    }
                })
    }

    fun deleteRating(uid: String?) {
        app.database.child("ratings").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Rating error : ${error.message}")
                    }
                })
    }

    override fun onRatingClick(ai: AiModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditFragment.newInstance(ai))
            .addToBackStack(null)
            .commit()
    }
}