package me.meenagopal24.wallpapers.UI

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.databases.FavouriteWallpaperHelper
import me.meenagopal24.wallpapers.databases.favourite
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.network.RetrofitClient
import me.meenagopal24.wallpapers.utils.Constants.PREVIEW_FRAGMENT
import me.meenagopal24.wallpapers.utils.Functions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(),
    ChangeInterface {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var list: ArrayList<wallpapers.item>
    lateinit var wallpapersRecycler: RecyclerView

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
        Functions.windowTrans(requireActivity(), false)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        wallpapersRecycler = view.findViewById(R.id.staggered_recycler)
        wallpapersRecycler.layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        getWallpapers()
        return view;
    }

    private fun getWallpapers() {
        val call: Call<wallpapers> = RetrofitClient.getInstance().api.wallpaper
        call.enqueue(object : Callback<wallpapers> {
            override fun onResponse(call: Call<wallpapers>, response: Response<wallpapers>) {
                list = response.body()?.list as ArrayList<wallpapers.item>
                wallpapersRecycler.adapter =
                    response.body()?.list?.let { StaggeredAdapter(it, this@HomeFragment) }
            }

            override fun onFailure(call: Call<wallpapers>, t: Throwable) {
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
            }

        })
        CategoryFragment
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
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(PREVIEW_FRAGMENT)
            .add(R.id.main_layout, PreviewFragment.newInstance(list, openPosition = position))
            .commit()
    }

    override fun changeFragment(title: String?, category: String?) {

    }
}