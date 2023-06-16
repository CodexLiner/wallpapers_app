package me.meenagopal24.wallpapers.UI

import android.annotation.SuppressLint
import android.os.Bundle
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
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.network.RetrofitClient
import me.meenagopal24.wallpapers.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TITLE = "param1"
private const val CATEGORY = "param2"



class SelectedCategoryFragment() : ChangeInterface,
    Fragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var category: String? = null
    lateinit var list: ArrayList<wallpapers.item>
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
        getItemsFromCategory()
    }

    private fun getItemsFromCategory() {
        val when_favourite: LinearLayout? = view?.findViewById(R.id.when_favourite)
        val call: Call<wallpapers> =
            RetrofitClient.getInstance().api.getWallpaperByCategory(category)
        call.enqueue(object : Callback<wallpapers> {
            override fun onResponse(call: Call<wallpapers>, response: Response<wallpapers>) {
                progress.visibility = View.GONE
                if (response.body()?.list != null && response.body()?.list?.size != 0) {
                    list = response.body()?.list as ArrayList<wallpapers.item>
                    wallpapersRecycler.adapter =
                        response.body()?.list?.let {
                            StaggeredAdapter(
                                it,
                                this@SelectedCategoryFragment
                            )
                        }
                    when_favourite?.visibility = View.INVISIBLE
                } else {
                    when_favourite?.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<wallpapers>, t: Throwable) {
                when_favourite?.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun changeFragment(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(Constants.PREVIEW_FRAGMENT)
            .add(R.id.main_layout, PreviewFragment.newInstance(list, openPosition = position))
            .commit()

    }

    override fun changeFragment(title: String?, category: String?) {

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