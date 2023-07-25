package me.meenagopal24.wallpapers.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import me.meenagopal24.wallpapers.R

/**
 * A simple [Fragment] subclass.
 * Use the [ConnectivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConnectivityFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connectivity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LottieAnimationView>(R.id.no_connection).setOnClickListener {
            return@setOnClickListener
        }
    }

}