package me.meenagopal24.wallpapers.MVVM.Category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.meenagopal24.wallpapers.MVVM.home.HomeViewModel
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.CategoryAdapter
import me.meenagopal24.wallpapers.databases.CategoryListHelper
import me.meenagopal24.wallpapers.databinding.FragmentCategoryBinding
import me.meenagopal24.wallpapers.interfaces.ChangeInterface

@AndroidEntryPoint
class CategoryFragment : Fragment(), ChangeInterface {
    lateinit var binding: FragmentCategoryBinding
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryRecycler.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

        getCategories()
    }

    private fun getCategories() {
        viewModel.allCategoryData.observe(this@CategoryFragment) {
            binding.categoryRecycler.adapter = CategoryAdapter(
                it, this@CategoryFragment
            )
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