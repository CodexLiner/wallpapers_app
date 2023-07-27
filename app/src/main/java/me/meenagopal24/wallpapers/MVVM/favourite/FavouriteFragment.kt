package me.meenagopal24.wallpapers.MVVM.favourite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.meenagopal24.wallpapers.MainActivity
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.databases.FavouriteWallpaperHelper
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.ApiResponseDezky
import me.meenagopal24.wallpapers.temp.PreviewActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FavouriteFragment : Fragment(), ChangeInterface {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var list: ArrayList<ApiResponseDezky.item>

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

        recyclerView.adapter =
            StaggeredAdapter(list, this@FavouriteFragment)
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
        val intent = Intent(requireContext(), PreviewActivity::class.java)
        intent.putParcelableArrayListExtra("pre_list", list)
        intent.putExtra("position", position)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun changeFragment(title: String?, category: String?) {}
}