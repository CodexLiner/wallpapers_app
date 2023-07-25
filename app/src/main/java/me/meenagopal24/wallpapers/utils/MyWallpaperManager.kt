package me.meenagopal24.wallpapers.utils

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.interfaces.WallpaperResponse
import me.meenagopal24.wallpapers.utils.Constants.BASE_URL_IMAGE
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


public class MyWallpaperManager(
    private val uri: String,
    val context: Context,
    val flag: Int,
    val close: WallpaperResponse,
) {
    private val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
    val name: FrameLayout = (context as Activity).findViewById<FrameLayout>(R.id.main_layout)
    fun getWallpaperReady() {
        try {
            val icon = BitmapFactory.decodeResource(context.resources, R.drawable.androidw)
            val screenWidth = context.resources.displayMetrics.widthPixels
            val screenHeight = context.resources.displayMetrics.heightPixels
            // Set the wallpaper
            Glide.with(context).asBitmap().load(BASE_URL_IMAGE + uri)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        bitmap: Bitmap,
                        transition: Transition<in Bitmap?>?,
                    ) {
                        val scaledBitmap =
                            Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, false)
                        setWallpaper(bitmap)

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        Snackbar.make(name, "Failed to Apply Wallpaper", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                })


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setWallpaper(bitmap: Bitmap) {
        Thread {
            when (flag) {
                0 -> { //0 for home
                    wallpaperManager.setBitmap(
                        bitmap, null, true, WallpaperManager.FLAG_SYSTEM
                    )

                }
                1 -> { // 1 for lock screen
                    wallpaperManager.setBitmap(
                        bitmap, null, true, WallpaperManager.FLAG_LOCK
                    )
                }
                else -> {
                    wallpaperManager.setBitmap(
                        bitmap,
                        null,
                        true,
                        WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
                    )
                }
            }
            (context as Activity).runOnUiThread {
                Snackbar.make(name, "Wallpaper Applied successfully", Snackbar.LENGTH_SHORT).show()
            }
            try {
                close.onWallpaperApplied()
            } catch (ignored: Exception) {
                Log.d("TAG", "setWallpaper: $ignored")
            }
        }.start()

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Throws(IOException::class)
    fun saveBitmapToStorage(displayName: String) {
        if (Build.VERSION.SDK_INT <= 29 && ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        }
        Snackbar.make(name, "Downloading...", Snackbar.LENGTH_SHORT).show()
        Glide.with(context).asBitmap().load(BASE_URL_IMAGE + uri)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: Transition<in Bitmap?>?,
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val values = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                            put(MediaStore.MediaColumns.MIME_TYPE, ".jpg")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                        }

                        val resolver = context.contentResolver
                        var uri: Uri? = null

                        try {
                            uri = resolver.insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values
                            )
                                ?: throw IOException("Failed to create new MediaStore record.")

                            resolver.openOutputStream(uri)?.use {
                                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 20, it))
                                    throw IOException("Failed to save bitmap.")
                            } ?: throw IOException("Failed to open output stream.")

                        } catch (e: IOException) {
                            uri?.let { orphanUri ->
                                resolver.delete(orphanUri, null, null)
                            }
                            throw e
                        }
                    } else {
                        val picturesDir =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        if (picturesDir != null) {
                            val imageFile = File(picturesDir, displayName)
                            try {
                                FileOutputStream(imageFile).use { fos ->
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos)
                                }
                                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                                mediaScanIntent.data = Uri.fromFile(imageFile)
                                context.sendBroadcast(mediaScanIntent)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle the placeholder if needed
                }
            })
        Snackbar.make(name, "Downloading Complete", Snackbar.LENGTH_SHORT).show()

    }


    private val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1

//    fun saveBitmapToStorage(fileName: String) {
//
//        if (Build.VERSION.SDK_INT <= 29 && ContextCompat.checkSelfPermission(
//                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                context as Activity,
//                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
//            )
//            return
//        }
//        Snackbar.make(name, "Downloading...", Snackbar.LENGTH_SHORT).show()
//        Glide.with(context).asBitmap().load(uri).into(object : CustomTarget<Bitmap?>() {
//            override fun onResourceReady(
//                resource: Bitmap,
//                transition: Transition<in Bitmap?>?,
//            ) {
//                val picturesDir =
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                if (picturesDir != null) {
//                    val imageFile = File(picturesDir, fileName)
//                    try {
//                        FileOutputStream(imageFile).use { fos ->
//                            resource.compress(Bitmap.CompressFormat.JPEG, 10, fos)
//                            Snackbar.make(name, "Downloading Complete", Snackbar.LENGTH_SHORT)
//                                .show()
//                        }
//                        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//                        mediaScanIntent.data = Uri.fromFile(imageFile)
//                        context.sendBroadcast(mediaScanIntent)
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            override fun onLoadCleared(placeholder: Drawable?) {
//                // Handle the placeholder if needed
//            }
//        })
//
//
//    }
}