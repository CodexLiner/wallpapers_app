package me.meenagopal24.wallpapers.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.ApiResponseDezky
import me.meenagopal24.wallpapers.network.RetrofitClient
import me.meenagopal24.wallpapers.temp.PreviewActivity
import me.meenagopal24.wallpapers.utils.Constants.VIEW_TYPE_AD
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TITLE = "name"
private const val CATEGORY = "category"


class SelectedCategoryFragment() : ChangeInterface,
    Fragment() {
    private var name: String? = null
    private var category: String? = null
    lateinit var list: ArrayList<ApiResponseDezky.item>
    lateinit var wallpapersRecycler: RecyclerView
    lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(TITLE)
            category = it.getString(CATEGORY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.title_selected).text = name
        progress = view.findViewById(R.id.progress)
        wallpapersRecycler = view.findViewById(R.id.staggered_recycler)
        wallpapersRecycler.layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

        // TODO: change this code for span count adjustment
        val gridLayoutManager = GridLayoutManager(requireContext(), 2) // Use 2 for the span count
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (wallpapersRecycler.adapter?.getItemViewType(position) == VIEW_TYPE_AD) 2 else 1
            }
        }
        wallpapersRecycler.layoutManager = gridLayoutManager
        getItemsFromCategory()
    }

    private fun getItemsFromCategory() {
        val whenFavourite: LinearLayout? = view?.findViewById(R.id.when_favourite)
        val call: Call<ApiResponseDezky> =
            RetrofitClient.getInstance().api.getWallpaperByCategory(category)
        call.enqueue(object : Callback<ApiResponseDezky> {
            override fun onResponse(call: Call<ApiResponseDezky>, response: Response<ApiResponseDezky>) {
                progress.visibility = View.GONE
                if (response.body()?.list != null && response.body()?.list?.size != 0) {
                    list = response.body()?.list as ArrayList<ApiResponseDezky.item>
                    wallpapersRecycler.adapter =
                        response.body()?.list?.let {
                            StaggeredAdapter(
                                it,
                                this@SelectedCategoryFragment
                            )
                        }
                    whenFavourite?.visibility = View.INVISIBLE
                } else {
                    whenFavourite?.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ApiResponseDezky>, t: Throwable) {
                whenFavourite?.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun changeFragment(position: Int) {
        val intent = Intent(requireContext(), PreviewActivity::class.java)
        intent.putParcelableArrayListExtra("pre_list", list)
        intent.putExtra("position", position)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun changeFragment(title: String?, category: String?) {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TAG", "onViewWorking saving data : ")
        outState.putString("category", category)
        outState.putString("name", name)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            Log.d("TAG", "onViewWorking onViewStateRestored: ")
            category = savedInstanceState.getString("category")
            name = savedInstanceState.getString("name")
            Log.d("TAG", "onViewWorking onViewStateRestored: data is $category   $name ")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, category: String) =
            SelectedCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(CATEGORY, category)
                }
            }
    }

}