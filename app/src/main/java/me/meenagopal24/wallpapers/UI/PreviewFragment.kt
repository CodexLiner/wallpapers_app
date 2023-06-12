package me.meenagopal24.wallpapers.UI

import android.app.Activity
import android.app.Dialog
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import me.meenagopal24.wallpapers.MainActivity
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.PreviewAdapter
import me.meenagopal24.wallpapers.interfaces.WallpaperResponse
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.utils.Constants
import me.meenagopal24.wallpapers.utils.Functions
import me.meenagopal24.wallpapers.utils.MyWallpaperManager
import me.meenagopal24.wallpapers.utils.WallpaperListHelper


private const val ARG_PARAM1 = "param1"
public const val PRE_LIST = "PreList"
public const val OPEN_POSITION = "openPosition"

class PreviewFragment() :
    Fragment(), WallpaperResponse {
    lateinit var preList: ArrayList<wallpapers.item>
    private var openPosition: Int = -1
    private var position: Int = 0;
    lateinit var wallpapers: RecyclerView
    private lateinit var progressDialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.setTheme(R.style.Theme_TranslucentWindow)
        super.onCreate(savedInstanceState)
        arguments?.let {
            openPosition = it.getInt(OPEN_POSITION)
            preList = it.getParcelableArrayList(PRE_LIST)!!
            WallpaperListHelper(requireContext()).saveList(preList, openPosition)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        Functions.windowTrans(requireActivity(), true)
        (requireActivity() as MainActivity).bottomModify(false)
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wallpapers = view.findViewById(R.id.recycler_wallaper)!!
        if (Constants.recyclerState != null) {
            wallpapers.layoutManager?.onRestoreInstanceState(Constants.recyclerState)
        }
        initializeRecyclerView(preList, position)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(wallpapers)
        val onScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    this@PreviewFragment.position = layoutManager!!.findFirstVisibleItemPosition()
                }
            }
        wallpapers.addOnScrollListener(onScrollListener)
        view.findViewById<Button>(R.id.apply_wallpaper).setOnClickListener {
            showDlg()
        }
        view.findViewById<Button>(R.id.download_wallpaper).setOnClickListener {
            preList[position].let { it1 ->
                MyWallpaperManager(
                    context = requireContext(),
                    uri = it1.url,
                    flag = 0,
                    close = this
                ).saveBitmapToStorage("${System.currentTimeMillis()}.jpg")
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Constants.recyclerState = wallpapers.layoutManager?.onSaveInstanceState()
    }

    private fun initializeRecyclerView(preList: ArrayList<wallpapers.item>, position: Int) {
        wallpapers.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        lateinit var adapter: PreviewAdapter
        if (preList.isNotEmpty()) {
            adapter = PreviewAdapter(preList)
            this@PreviewFragment.position = position
        } else {
            this@PreviewFragment.preList = WallpaperListHelper(requireContext()).getList()
            adapter = PreviewAdapter(preList)
        }
        wallpapers.adapter = adapter
        if (openPosition != -1) {
            wallpapers.scrollToPosition(openPosition)
        }
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
        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.loading)
        progressDialog.setCancelable(false)

        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(progressDialog.window?.attributes)
        layoutParams.width = (context?.resources?.displayMetrics?.widthPixels ?: 0) - 100

        progressDialog.window?.attributes = layoutParams
        progressDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        progressDialog.show()

        preList[position].let {
            MyWallpaperManager(
                context = requireContext(),
                uri = it.url,
                flag = f,
                close = this
            ).getWallpaperReady()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(preList: ArrayList<wallpapers.item>?, openPosition: Int = -1) =
            PreviewFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PRE_LIST, preList)
                    putInt(OPEN_POSITION, openPosition)
                }
            }
    }

    override fun onWallpaperApplied() {
        progressDialog.dismiss()
        try {
            requireActivity().runOnUiThread {
                wallpaperSuccessEvent()
            }
        } catch (_: Exception) {
        }
    }

    private fun wallpaperSuccessEvent() {
        view?.findViewById<LottieAnimationView>(R.id.lottie_layer_success)?.playAnimation()
        val mediaPlayer = MediaPlayer.create(context, R.raw.succes_sound)
        mediaPlayer.start()
    }
}