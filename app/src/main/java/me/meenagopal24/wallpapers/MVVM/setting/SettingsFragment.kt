package me.meenagopal24.wallpapers.MVVM.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.snackbar.Snackbar
import me.meenagopal24.wallpapers.BuildConfig
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.utils.Constants
import me.meenagopal24.wallpapers.utils.Functions

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
            Functions.o(requireContext(), "${Constants.BASE_URL}/about")
        }
        view.findViewById<LinearLayout>(R.id.m_favs).setOnClickListener {
            findNavController().navigate(R.id.fav_bottom)
        }
        view.findViewById<LinearLayout>(R.id.account).setOnClickListener {
            Snackbar.make(requireContext(), view, "Something went wrong", Snackbar.LENGTH_SHORT).show()
        }
        view.findViewById<TextView>(R.id.version).text = "Version ${BuildConfig.VERSION_NAME}".toString()
        val adView : AdView = view.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
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