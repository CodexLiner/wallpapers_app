package me.meenagopal24.wallpapers.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.interfaces.PreviewInterface

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(), PreviewInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var list: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val wallpapers: RecyclerView = view.findViewById(R.id.staggered_recycler)
        wallpapers.layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

        list = arrayListOf()
        list.add("https://images.pexels.com/photos/1420226/pexels-photo-1420226.jpeg?auto=compress&cs=tinysrgb&w=1600")
        list.add("https://images.pexels.com/photos/6691950/pexels-photo-6691950.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
        list.add("https://images.pexels.com/photos/14286814/pexels-photo-14286814.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
        list.add("https://images.pexels.com/photos/11200581/pexels-photo-11200581.jpeg")
        list.add("https://images.pexels.com/photos/10585143/pexels-photo-10585143.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
        list.add("https://images.pexels.com/photos/5846273/pexels-photo-5846273.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
        list.add("https://images.pexels.com/photos/11200581/pexels-photo-11200581.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
        list.add("https://images.pexels.com/photos/13530493/pexels-photo-13530493.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")

        wallpapers.adapter = StaggeredAdapter(list, this)
        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun changeFragment(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack("old")
            .add(R.id.main_layout, PreviewFragment(list, position)).commit()
    }
}