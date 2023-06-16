package me.meenagopal24.wallpapers.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.meenagopal24.wallpapers.models.wallpapers

class WallpaperListHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "wallpaper_list.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "wallpaper"
        private const val WALLPAPER_NAME = "name"
        private const val WALLPAPER_URL = "url"
        private const val WALLPAPER_POS = "position"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($WALLPAPER_NAME TEXT , $WALLPAPER_URL TEXT , $WALLPAPER_POS INTEGER DEFAULT 0 )"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
    }

    fun saveList(myList: ArrayList<wallpapers.item> , position: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)

        val values = ContentValues()
        values.put(WALLPAPER_POS , position)
        for (item in myList) {
            values.put(WALLPAPER_NAME, item.name)
            values.put(WALLPAPER_URL, item.image)
            db.insert(TABLE_NAME, null, values)
        }
        db.close()
    }
    fun getPosition():Int{
        val db = readableDatabase
        val query = "SELECT $WALLPAPER_POS FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        return 0

    }

    fun getList(): ArrayList<wallpapers.item> {
        val db = readableDatabase
        val list = ArrayList<wallpapers.item>()

        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(WALLPAPER_NAME))
                val url = cursor.getString(cursor.getColumnIndex(WALLPAPER_URL))
                list.add(wallpapers.item(name, url , ""))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }
}
