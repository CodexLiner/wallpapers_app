package me.meenagopal24.wallpapers.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.databases.AllWallpaperListHelper
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.network.RetrofitClient
import me.meenagopal24.wallpapers.utils.Constants.PREVIEW_FRAGMENT
import me.meenagopal24.wallpapers.utils.Constants.VIEW_TYPE_AD
import me.meenagopal24.wallpapers.utils.Functions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(),
    ChangeInterface {
    lateinit var list: ArrayList<wallpapers.item>
    lateinit var wallpapersRecycler: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var when_favourite: LinearLayout
    lateinit var progress: ProgressBar
    private var scrollPosition = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Functions.windowTrans(requireActivity(), false)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        wallpapersRecycler = view.findViewById(R.id.staggered_recycler)
        when_favourite = view.findViewById(R.id.when_favourite)
        progress = view.findViewById(R.id.progress)

        wallpapersRecycler.layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener { getWallpapers() }
        view.findViewById<Button>(R.id.refresh).setOnClickListener { getWallpapers() }
        if (AllWallpaperListHelper(requireContext()).getList().isNotEmpty()) {
            setAdapter(AllWallpaperListHelper(requireContext()).getList())
        }
//        wallpapersRecycler.addOnScrollListener(onScrollListener())
        getWallpapers()
        return view;
    }

    private fun getWallpapers() {

        val call: Call<wallpapers> = RetrofitClient.getInstance().api.wallpaper
        call.enqueue(object : Callback<wallpapers> {
            override fun onResponse(call: Call<wallpapers>, response: Response<wallpapers>) {
                try {
                    if (swipeRefresh.isRefreshing || AllWallpaperListHelper(requireContext()).getList()
                            .isEmpty()
                    ) {
                        setAdapter(response.body()?.list)
                        swipeRefresh.isRefreshing = false
                    }
                    requireActivity().runOnUiThread {
                        response.body()
                            ?.let { AllWallpaperListHelper(requireContext()).saveList(it.list) }
                    }
                } catch (_: java.lang.Exception) {
                }
            }

            override fun onFailure(call: Call<wallpapers>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
            }

        })

        CategoryFragment
    }

    private fun setAdapter(list: java.util.ArrayList<wallpapers.item>?) {
        progress.visibility = View.GONE
        if (list != null && list.size != 0) {
            this.list = list as ArrayList<wallpapers.item>
            wallpapersRecycler.adapter = StaggeredAdapter(list, this@HomeFragment)
            wallpapersRecycler.scrollToPosition(scrollPosition)
            swipeRefresh.isRefreshing = false
            when_favourite.visibility = View.INVISIBLE

        } else {
            when_favourite.visibility = View.VISIBLE
        }

        // TODO: change this code for span count adjustment
        val gridLayoutManager = GridLayoutManager(requireContext(), 2) // Use 2 for the span count
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (wallpapersRecycler.adapter?.getItemViewType(position) == VIEW_TYPE_AD) 2 else 1
            }
        }
        wallpapersRecycler.layoutManager = gridLayoutManager
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun changeFragment(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(PREVIEW_FRAGMENT)
            .add(R.id.main_layout, PreviewFragment.newInstance(list, openPosition = position))
            .commit()
    }

    override fun changeFragment(title: String?, category: String?) {}

    private fun onScrollListener(): RecyclerView.OnScrollListener {
        val onScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    scrollPosition = layoutManager!!.findFirstVisibleItemPosition()
                }
            }
        return onScrollListener
    }


}