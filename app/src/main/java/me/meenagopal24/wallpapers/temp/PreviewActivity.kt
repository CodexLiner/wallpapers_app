package me.meenagopal24.wallpapers.temp

import android.app.Dialog
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.PreviewAdapter
import me.meenagopal24.wallpapers.databases.FavouriteWallpaperHelper
import me.meenagopal24.wallpapers.databases.WallpaperListHelper
import me.meenagopal24.wallpapers.interfaces.WallpaperResponse
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.utils.Constants
import me.meenagopal24.wallpapers.utils.Functions
import me.meenagopal24.wallpapers.utils.MyWallpaperManager
import kotlin.math.log

class PreviewActivity : AppCompatActivity(), WallpaperResponse {
    lateinit var preList: ArrayList<wallpapers.item>
    private var openPosition: Int = -1
    private var hideStatus: Boolean = false
    private var position: Int = 0;
    lateinit var wallpapers: RecyclerView
    private lateinit var progressDialog: Dialog
    lateinit var name: TextView
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Functions.windowTrans(this@PreviewActivity, true)
        setContentView(R.layout.activity_preview2)
        wallpapers = findViewById(R.id.recycler_wallaper)!!
        preList = intent.getParcelableArrayListExtra("pre_list")!!
        openPosition = intent.getIntExtra("position", 0);
        if (Constants.recyclerState != null) {
            wallpapers.layoutManager?.onRestoreInstanceState(Constants.recyclerState)
        }
        if (hideStatus) {
            findViewById<LinearLayout>(R.id.text_area_for_favourite).visibility =
                View.INVISIBLE
        }
        name = findViewById(R.id.wallpaper_text)
        imageView = findViewById(R.id.add_to_fav)
        initializeRecyclerView(preList, position)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(wallpapers)
//       Todo when scroll we need to perform some action
        wallpapers.addOnScrollListener(onScrollListener())
        findViewById<Button>(R.id.apply_wallpaper).setOnClickListener {
            showDlg()
        }
        findViewById<Button>(R.id.download_wallpaper).setOnClickListener {
            preList[position].let { it1 ->
                val uri = it1.category + "/" + it1.image
                MyWallpaperManager(
                    uri = uri,
                    context = applicationContext,
                    flag = 0,
                    close = this,
                ).saveBitmapToStorage("${System.currentTimeMillis()}.jpg")
            }
        }
        imageView.setOnClickListener {
            addOrRemoveFav(imageView)
        }
    }


    private fun changeText(position: Int, name: TextView) {
        name.text = preList[position].name
    }

    private fun addOrRemoveFav(imageView: ImageView) {
        if (!FavouriteWallpaperHelper(applicationContext).getIsContains(preList[position].uuid)) {
            FavouriteWallpaperHelper(applicationContext).addFav(
                me.meenagopal24.wallpapers.models.wallpapers
                    .item(
                        preList[position].name,
                        preList[position].image,
                        preList[position].uuid,
                        preList[position].thumbnail,
                        preList[position].category
                    )
            )
            imageView.let {
                Snackbar.make(
                    applicationContext,
                    it, "Added to favourites", Snackbar.LENGTH_SHORT
                ).show()
            }
            imageView.setImageResource(R.drawable.fi_fill_heart)
        } else {
            FavouriteWallpaperHelper(applicationContext).removeFav(preList[position].uuid)
            imageView?.let {
                Snackbar.make(
                    applicationContext,
                    it, "Removed from favourite", Snackbar.LENGTH_SHORT
                ).show()
            }
            imageView.setImageResource(R.drawable.fi_ss_heart)

        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Constants.recyclerState = wallpapers.layoutManager?.onSaveInstanceState()
    }

    private fun initializeRecyclerView(preList: ArrayList<wallpapers.item>, position: Int) {
        wallpapers.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        lateinit var adapter: PreviewAdapter
        if (preList.isNotEmpty()) {
            adapter = PreviewAdapter(preList)
            this@PreviewActivity.position = position
        } else {
            this@PreviewActivity.preList = WallpaperListHelper(applicationContext).getList()
            adapter = PreviewAdapter(preList)
        }
        wallpapers.adapter = adapter
        if (openPosition != -1) {
            wallpapers.scrollToPosition(openPosition)
        }
    }

    private fun showDlg() {
        val dialog = Dialog(this@PreviewActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.screens_dialog)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)

        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)

        layoutParams.width = resources.displayMetrics.widthPixels - 100

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

    @OptIn(DelicateCoroutinesApi::class)
    private fun setFlag(f: Int) {
        try {
            progressDialog = Dialog(this@PreviewActivity)
            progressDialog.setContentView(R.layout.loading)
            progressDialog.setCancelable(false)

            val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(progressDialog.window?.attributes)
            layoutParams.width = (resources?.displayMetrics?.widthPixels ?: 0) - 100

            progressDialog.window?.attributes = layoutParams
            progressDialog.window?.setBackgroundDrawableResource(R.color.transparent)
            progressDialog.show()
        }catch (e:Exception){
            Log.d("TAG", "workingmenUCrash: on setFlag function ")
        }
        preList[position].let {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    MyWallpaperManager(
                        context = this@PreviewActivity,
                        uri = it.category.trim() + "/" + it.image.trim(),
                        flag = f,
                        close = this@PreviewActivity,
                    ).getWallpaperReady()
                }
                Log.d("TAG", "workingmenUCrash: got result")
            }

        }
    }

    override fun onWallpaperApplied() {
        progressDialog.dismiss()
        try {
            runOnUiThread {
//                wallpaperSuccessEvent()
            }
        } catch (_: Exception) {
        }
    }

    private fun wallpaperSuccessEvent() {
        findViewById<LottieAnimationView>(R.id.lottie_layer_success)?.playAnimation()
        val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.succes_sound)
        mediaPlayer.start()
    }

    private fun onScrollListener(): RecyclerView.OnScrollListener {
        val onScrollListener: RecyclerView.OnScrollListener =
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    this@PreviewActivity.position = layoutManager!!.findFirstVisibleItemPosition()
                    changeText(position, name)
                    if (FavouriteWallpaperHelper(applicationContext).getIsContains(preList[position].uuid)) {
                        imageView.setImageResource(R.drawable.fi_fill_heart)
                    } else {
                        imageView.setImageResource(R.drawable.fi_ss_heart)
                    }
                }
            }
        return onScrollListener
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0);
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissDialog();
    }

    private fun dismissDialog() {
        if (::progressDialog.isInitialized){
            progressDialog.dismiss()
        }
    }
}