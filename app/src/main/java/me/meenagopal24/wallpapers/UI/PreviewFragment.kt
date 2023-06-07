package me.meenagopal24.wallpapers.UI

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.Preview_adapter
import me.meenagopal24.wallpapers.utils.wallpaper


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PreviewFragment(private val preList: List<String>, private val openPosition : Int = -1) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var position: Int = 0;
//    lateinit var list: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        context?.theme?.applyStyle(R.style.TranslucentWindow, true);
        val view: View = inflater.inflate(R.layout.fragment_preview, container, false)
        val wallpapers: RecyclerView = view.findViewById(R.id.recycler_wallaper)
        wallpapers.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//todo:for testing default list
//        prelist = arrayListOf()
//        list.add("https://images.pexels.com/photos/1624496/pexels-photo-1624496.jpeg")
//        list.add("https://images.pexels.com/photos/937980/pexels-photo-937980.jpeg")
        lateinit var adapter: Preview_adapter
        if (preList.isNotEmpty()) {
            adapter = Preview_adapter(preList)
            position = openPosition
        }
        val snapHelper = PagerSnapHelper()

        snapHelper.attachToRecyclerView(wallpapers)
        wallpapers.adapter = adapter
        if (openPosition != -1){
            wallpapers.scrollToPosition(openPosition)
        }
        val onScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    position = layoutManager!!.findFirstVisibleItemPosition()
                }
            }
        wallpapers.addOnScrollListener(onScrollListener)

        view.findViewById<ImageView>(R.id.apply_wallpaper).setOnClickListener {
            showDlg()
        }
        view.findViewById<ImageView>(R.id.download_wallpaper).setOnClickListener {
            wallpaper(
                context = requireContext(),
                uri = preList[position],
                flag = 0
            ).saveBitmapToStorage("${System.currentTimeMillis()}.jpg")
        }
        return view
    }

    private fun showDlg() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.screens_dialog)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)

        layoutParams.width = requireContext().resources.displayMetrics.widthPixels - 100

        dialog.window?.attributes = layoutParams

        dialog.findViewById<Button>(R.id.home).setOnClickListener {
            setFlag(0)
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.lock).setOnClickListener {
            setFlag(1)
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.both).setOnClickListener {
            setFlag(3)
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun setFlag(f: Int) {
        wallpaper(
            context = requireContext(),
            uri = preList[position],
            flag = f
        ).getWallpaperReady()
    }

//    companion object {
//
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) = PreviewFragment().apply {
//            arguments = Bundle().apply {
//                putString(ARG_PARAM1, param1)
//                putString(ARG_PARAM2, param2)
//            }
//        }
//    }
}