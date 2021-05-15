package ie.wit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ie.wit.R
import ie.wit.main.AiApp
import ie.wit.utils.getAllRatings
import ie.wit.utils.getIcons
import ie.wit.utils.setMapMarker
import kotlinx.android.synthetic.main.fragment_icon.*


class IconFragment : Fragment() {

    lateinit var app: AiApp
    var viewIcons = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as AiApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout = inflater.inflate(R.layout.fragment_icon, container, false)

        return layout;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.map_title)

        imageMapIcons.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                app.mMap.clear()
                setMapMarker(app)
                if (!viewIcons) {
                    imageMapIcons.setImageResource(R.drawable.ic_favorite_on)
                    viewIcons = true
                    getIcons(app)
                }
                else {
                    imageMapIcons.setImageResource(R.drawable.ic_favorite_off)
                    viewIcons = false
                    getAllRatings(app)
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            IconFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}