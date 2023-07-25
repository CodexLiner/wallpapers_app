package me.meenagopal24.wallpapers.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.CategoryAdapter
import me.meenagopal24.wallpapers.databases.CategoryListHelper
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : Fragment(),
    ChangeInterface {
    lateinit var catRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        catRecycler = view.findViewById(R.id.category_recycler)
        catRecycler.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

        if (CategoryListHelper(requireContext()).getList().isNotEmpty()) {
            catRecycler.adapter = CategoryAdapter(
                CategoryListHelper(requireContext()).getList(),
                this@CategoryFragment
            )
        }
        getCategories()
    }

    private fun getCategories() {
        val call: Call<wallpapers> = RetrofitClient.getInstance().api.categories
        call.enqueue(object : Callback<wallpapers> {
            override fun onFailure(call: Call<wallpapers>, t: Throwable) {
                Toast.makeText(requireContext(), "something went", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<wallpapers>, response: Response<wallpapers>) {
                requireActivity().runOnUiThread {
                    CategoryListHelper(requireContext()).saveList(response.body()?.list as ArrayList<wallpapers.item>)
                    if (CategoryListHelper(requireContext()).getList().isEmpty()) {
                        catRecycler.adapter = response.body()?.list?.let {
                            CategoryAdapter(
                                it,
                                this@CategoryFragment
                            )
                        }
                    }
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun changeFragment(position: Int) {}
    override fun changeFragment(title: String, category: String) {
        val args = Bundle()
        args.putString("name", title)
        args.putString("category", category)
        findNavController().navigate(R.id.selected_category_fragment, args)
    }

}