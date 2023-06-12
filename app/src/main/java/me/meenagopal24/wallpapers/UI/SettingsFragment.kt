package me.meenagopal24.wallpapers.UI

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.utils.Constants.BASE_URL
import me.meenagopal24.wallpapers.utils.Constants.FAVOURITE_FRAGMENT
import me.meenagopal24.wallpapers.utils.Functions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LinearLayout>(R.id.rateUs).setOnClickListener {
            try {
                val marketUri: Uri =
                    Uri.parse("market://details?id=" + requireActivity().packageName)
                val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                startActivity(marketIntent)
            } catch (e: ActivityNotFoundException) {
                Functions.o(
                    requireContext(),
                    "https://play.google.com/store/apps/details?id=" + requireActivity().packageName
                )
            }
        }
        view.findViewById<LinearLayout>(R.id.aboutUs).setOnClickListener {
            Functions.o(requireContext(), "$BASE_URL/about")
        }
        view.findViewById<LinearLayout>(R.id.m_favs).setOnClickListener {
            Functions(requireActivity().supportFragmentManager).switchFragment(FAVOURITE_FRAGMENT)
        }
        view.findViewById<LinearLayout>(R.id.account).setOnClickListener {
            Snackbar.make(requireContext(), view, "Something went wrong", Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}