package me.meenagopal24.wallpapers.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.CategoryAdapter
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.categories
import me.meenagopal24.wallpapers.network.RetrofitClient
import me.meenagopal24.wallpapers.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CategoryFragment : Fragment(),
    ChangeInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var catRecycler: RecyclerView

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
        val view = inflater.inflate(R.layout.fragment_category, container, false)
        catRecycler = view.findViewById<RecyclerView>(R.id.category_recycler)
        catRecycler.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        getCategories()
        return view
    }

    private fun getCategories() {
        val call: Call<categories> = RetrofitClient.getInstance().api.categories
        call.enqueue(object : Callback<categories> {
            override fun onResponse(call: Call<categories>, response: Response<categories>) {
                catRecycler.adapter =
                    response.body()?.list?.let { CategoryAdapter(it, this@CategoryFragment) }
            }

            override fun onFailure(call: Call<categories>, t: Throwable) {
                Toast.makeText(requireContext(), "something went", Toast.LENGTH_SHORT).show()
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun changeFragment(position: Int) {

    }

    override fun changeFragment(title: String, category: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(Constants.PREVIEW_FRAGMENT)
            .add(R.id.main_layout, SelectedCategoryFragment.newInstance(title, category)).commit()
    }
}