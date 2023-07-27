package me.meenagopal24.wallpapers.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.meenagopal24.wallpapers.models.ApiResponseDezky

class AllWallpaperListHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "all_wallpaper_list.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "wallpaper"
        private const val WALLPAPER_NAME = "name"
        private const val WALLPAPER_URL = "url"
        private const val THUMBNAIL = "thumbnail"
        private const val UUID = "uuid"
        private const val CATEGORY = "category"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($WALLPAPER_NAME TEXT , $WALLPAPER_URL TEXT , $THUMBNAIL TXT , $UUID TXT , $CATEGORY TXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
    }

    fun saveList(myList: ArrayList<ApiResponseDezky.item>) {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)

        val values = ContentValues()
        for (item in myList) {
            values.put(WALLPAPER_NAME, item.name)
            values.put(WALLPAPER_URL, item.image)
            values.put(THUMBNAIL, item.image)
            values.put(UUID, item.uuid)
            values.put(CATEGORY, item.category)
            db.insert(TABLE_NAME, null, values)
        }
        db.close()
    }


    fun getList(): ArrayList<ApiResponseDezky.item> {
        val db = readableDatabase
        val list = ArrayList<ApiResponseDezky.item>()

        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(WALLPAPER_NAME))
                val url = cursor.getString(cursor.getColumnIndex(WALLPAPER_URL))
                val thumbnai = cursor.getString(cursor.getColumnIndex(THUMBNAIL))
                val uuid = cursor.getString(cursor.getColumnIndex(UUID))
                val category = cursor.getString(cursor.getColumnIndex(CATEGORY))
                list.add(ApiResponseDezky.item(name, url, uuid , thumbnai , category))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }
}
