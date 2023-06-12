package me.meenagopal24.wallpapers.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.meenagopal24.wallpapers.MainActivity
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.databases.FavouriteWallpaperHelper
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.utils.Constants.PREVIEW_FRAGMENT
import me.meenagopal24.wallpapers.utils.Functions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class FavouriteFragment : Fragment(), ChangeInterface {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var list: ArrayList<wallpapers.item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        try {
            (requireActivity() as MainActivity).bottomNav.selectedItemId = R.id.fav_bottom
        } catch (_: java.lang.Exception) {
        }
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = FavouriteWallpaperHelper(requireContext()).getFavList()
        val recyclerView: RecyclerView = view.findViewById(R.id.favourite_recycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = StaggeredAdapter(list, this@FavouriteFragment)
        if (list.isNotEmpty()) {
            view.findViewById<LinearLayout>(R.id.when_favourite).visibility = View.GONE
        }

        view.findViewById<Button>(R.id.fav_now).setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun changeFragment(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_layout,
                PreviewFragment.newInstance(preList = list, position , true),
                "old_preview_fav"
            ).addToBackStack("old_preview_fav").commit()
    }

    override fun changeFragment(title: String?, category: String?) {}
}