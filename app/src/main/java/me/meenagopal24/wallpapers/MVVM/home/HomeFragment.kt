package me.meenagopal24.wallpapers.MVVM.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.databases.AllWallpaperListHelper
import me.meenagopal24.wallpapers.databinding.FragmentHomeBinding
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.ApiResponseDezky
import me.meenagopal24.wallpapers.temp.PreviewActivity
import me.meenagopal24.wallpapers.utils.Constants.VIEW_TYPE_AD
import me.meenagopal24.wallpapers.utils.Functions

@AndroidEntryPoint
class HomeFragment : Fragment(), ChangeInterface, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    lateinit var list: ArrayList<ApiResponseDezky.item>
    private var scrollPosition = 0
    lateinit var adapter: StaggeredAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Functions.windowTrans(requireActivity(), false)
        binding.staggeredRecycler.layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        binding.swipeRefresh.setOnRefreshListener(this)
        binding.progress.visibility = View.INVISIBLE
        getRecentWallpapers()
    }

    private fun getRecentWallpapers() {
        viewModel.recentlyAddedWallpapers.observe(this@HomeFragment) {
            AllWallpaperListHelper(requireContext()).saveList(it)
            this.list = it
            binding.staggeredRecycler.apply {
                adapter = StaggeredAdapter(it, this@HomeFragment)
                scrollToPosition(scrollPosition)
            }
            setAdapter()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setAdapter() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2) // Use 2 for the span count
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (binding.staggeredRecycler.adapter?.getItemViewType(position) == VIEW_TYPE_AD) 2 else 1
            }
        }
        binding.staggeredRecycler.layoutManager = gridLayoutManager
    }

    override fun changeFragment(position: Int) {
        val intent = Intent(requireContext(), PreviewActivity::class.java)
        intent.putParcelableArrayListExtra("pre_list", list)
        intent.putExtra("position", position)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun changeFragment(title: String?, category: String?) {}

    override fun onRefresh() {
        viewModel.onRefreshWallpapers()
    }

}