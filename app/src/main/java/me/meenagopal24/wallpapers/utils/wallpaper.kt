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
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import me.meenagopal24.wallpapers.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


public class wallpaper(private val uri: String, val context: Context, val flag: Int) {
    val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
    fun getWallpaperReady() {
        try {
            val icon = BitmapFactory.decodeResource(context.resources, R.drawable.androidw)
            val screenWidth = context.resources.displayMetrics.widthPixels
            val screenHeight = context.resources.displayMetrics.heightPixels
            // Set the wallpaper
            Glide.with(context)
                .asBitmap()
                .load(uri)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        bitmap: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        val scaledBitmap =
                            Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, false)
                        if (flag == 0) { //0 for home
                            wallpaperManager.setBitmap(
                                scaledBitmap,
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )
                        } else if (flag == 1) { // 1 for lock screen
                            wallpaperManager.setBitmap(
                                scaledBitmap,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )
                        } else {
                            wallpaperManager.setBitmap(
                                scaledBitmap,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
                            )
                        }

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        Toast.makeText(
                            context,
                            "Failed to Wallpaper set successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                })

            Toast.makeText(context, "Wallpaper set successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1

    fun saveBitmapToStorage(fileName: String) {

        if (Build.VERSION.SDK_INT <= 29 && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
            return
        }
        Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    if (picturesDir != null) {
                        val imageFile = File(picturesDir, fileName)
                        try {
                            FileOutputStream(imageFile).use { fos ->
                                resource.compress(Bitmap.CompressFormat.JPEG, 10, fos)
                                Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show()
                            }
                            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                            mediaScanIntent.data = Uri.fromFile(imageFile)
                            context.sendBroadcast(mediaScanIntent)
                        } catch (e: IOException) {
                            Log.d("TAG", "onResourceReady: $e")
                            e.printStackTrace()
                        }
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle the placeholder if needed
                }
            })


    }
}