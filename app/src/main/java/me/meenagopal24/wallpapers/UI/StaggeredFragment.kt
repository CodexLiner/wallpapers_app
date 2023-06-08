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
import me.meenagopal24.wallpapers.ScalingItemDecoration
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.interfaces.PreviewInterface
import me.meenagopal24.wallpapers.utils.Constants.PREVIEW_FRAGMENT

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StaggeredFragment : Fragment(), PreviewInterface {
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
        val view = inflater.inflate(R.layout.fragment_staggered, container, false)
        val staggred: RecyclerView = view.findViewById(R.id.staggered_recycler)
        staggred.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

        list = arrayListOf()
        list.add("https://images.pexels.com/photos/1624496/pexels-photo-1624496.jpeg")
        list.add("https://images.pexels.com/photos/937980/pexels-photo-937980.jpeg")
        list.add("https://images.pexels.com/photos/937980/pexels-photo-937980.jpeg")
        list.add("https://images.pexels.com/photos/1212693/pexels-photo-1212693.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
        list.add("https://images.pexels.com/photos/15239/flower-roses-red-roses-bloom.jpg?auto=compress&cs=tinysrgb&w=1600")
        list.add("https://images.pexels.com/photos/937980/pexels-photo-937980.jpeg")
        list.add("https://images.pexels.com/photos/1624496/pexels-photo-1624496.jpeg")
        list.add("https://images.pexels.com/photos/937980/pexels-photo-937980.jpeg")

        staggred.adapter = StaggeredAdapter(list, this)
//        staggered.addItemDecoration(SpacesItemDecoration(16))
        return view
    }

    override fun changeFragment(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(PREVIEW_FRAGMENT)
            .add(R.id.main_layout, PreviewFragment(list, position)).commit()
    }


}