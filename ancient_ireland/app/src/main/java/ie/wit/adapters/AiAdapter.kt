package ie.wit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import ie.wit.R
import ie.wit.fragments.LocationAllFragment
import ie.wit.models.AiModel
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.card_location.view.*
import kotlinx.android.synthetic.main.card_location.view.imageicon
import kotlinx.android.synthetic.main.fragment_rate.view.*

interface LocationListener {
    fun onRatingClick(ai: AiModel)
}

class AiAdapter(options: FirebaseRecyclerOptions<AiModel>,
                      private val listener: LocationListener?)
    : FirebaseRecyclerAdapter<AiModel,
        AiAdapter.RatingViewHolder>(options) {

    class RatingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(ai: AiModel, listener: LocationListener) {
            with(ai) {
                itemView.tag = ai
                itemView.ratingamount.text = ai.amount.toString()
                itemView.ratingmethod.text = ai.ratingtype

                if(listener is LocationAllFragment)
                    ;
                else
                    itemView.setOnClickListener { listener.onRatingClick(ai) }

                if(ai.isicon) itemView.imageicon.setImageResource(android.R.drawable.star_big_on)

                if(!ai.profilepic.isEmpty()) {
                    Picasso.get().load(ai.profilepic.toUri())
                        .transform(CropCircleTransformation())
                        .into(itemView.imageIcon)
                }
                else
                    itemView.imageIcon.setImageResource(R.mipmap.ic_launcher_shamrock_round)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {

        return RatingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_location, parent, false))
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int, model: AiModel) {
        holder.bind(model,listener!!)
    }

    override fun onDataChanged() {

    }
}