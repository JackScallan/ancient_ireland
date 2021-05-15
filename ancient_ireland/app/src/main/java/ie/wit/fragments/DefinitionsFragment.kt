package ie.wit.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ie.wit.R


class DefinitionsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.definitions_title)
        return inflater.inflate(R.layout.fragment_definitions, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DefinitionsFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}
