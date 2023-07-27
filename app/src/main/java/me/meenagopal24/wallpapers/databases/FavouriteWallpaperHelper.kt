package me.meenagopal24.wallpapers.databases

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.meenagopal24.wallpapers.models.ApiResponseDezky

class FavouriteWallpaperHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "fav_wallpaper_list.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "wallpaper"
        private const val WALLPAPER_NAME = "name"
        private const val WALLPAPER_URL = "url"
        private const val WALLPAPER_UUID = "uuid"
        private const val WALLPAPER_CATEGORY = "category"
        private const val ID = "id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($WALLPAPER_NAME TEXT , $WALLPAPER_URL TEXT ,$ID INTEGER PRIMARY KEY , $WALLPAPER_UUID TXT , $WALLPAPER_CATEGORY TXT )"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
    }

    fun addFav(data: ApiResponseDezky.item) {
        val db = writableDatabase
//        db.delete(TABLE_NAME, null, null)
        val values = ContentValues()

        values.put(WALLPAPER_UUID, data.uuid)
        values.put(WALLPAPER_NAME, data.name)
        values.put(WALLPAPER_URL, data.image)
        values.put(WALLPAPER_CATEGORY, data.category)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getFavList(): ArrayList<ApiResponseDezky.item> {
        val db = readableDatabase
        val list = ArrayList<ApiResponseDezky.item>()

        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(WALLPAPER_NAME))
                val url = cursor.getString(cursor.getColumnIndex(WALLPAPER_URL))
                val uuid = cursor.getString(cursor.getColumnIndex(WALLPAPER_UUID))
                val category = cursor.getString(cursor.getColumnIndex(WALLPAPER_CATEGORY))
                list.add(ApiResponseDezky.item(name, url, uuid ,url, category))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }
    @SuppressLint("Recycle")
    fun getIsContains(uuid: String) : Boolean{
        val db = readableDatabase
        val query = "SELECT $WALLPAPER_UUID FROM $TABLE_NAME WHERE $WALLPAPER_UUID = ?"
        val search = db.rawQuery(query , arrayOf(uuid.toString()))
        while (search.moveToNext()){
            return uuid.equals(search.getString(search.getColumnIndex(WALLPAPER_UUID)) , true)
        }
        search.close()
        return false
    }

    fun removeFav(uuid: String) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$WALLPAPER_UUID=?", arrayOf(uuid.toString()))
        db.close()
    }
}
