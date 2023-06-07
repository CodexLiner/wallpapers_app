package me.meenagopal24.wallpapers.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import me.meenagopal24.wallpapers.R

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(me.meenagopal24.wallpapers.ARG_PARAM1)
            param2 = it.getString(me.meenagopal24.wallpapers.ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        view.findViewById<TextView>(R.id.tempo).setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().add(R.id.main_layout , StaggeredFragment()).commit()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(me.meenagopal24.wallpapers.ARG_PARAM1, param1)
                    putString(me.meenagopal24.wallpapers.ARG_PARAM2, param2)
                }
            }
    }
}