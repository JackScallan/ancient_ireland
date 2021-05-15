package ie.wit.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

import ie.wit.R
import ie.wit.adapters.AiAdapter
import ie.wit.adapters.LocationListener
import ie.wit.models.AiModel
import kotlinx.android.synthetic.main.fragment_location_all.view.*

class LocationAllFragment : LocationFragment(),
    LocationListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_location_all, container, false)
        activity?.title = getString(R.string.menu_location_all)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))

        var query = FirebaseDatabase.getInstance()
            .reference.child("ratings")

        var options = FirebaseRecyclerOptions.Builder<AiModel>()
            .setQuery(query, AiModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.recyclerView.adapter = AiAdapter(options, this)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LocationAllFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}