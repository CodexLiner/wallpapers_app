package me.meenagopal24.wallpapers.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.meenagopal24.wallpapers.models.wallpapers

class CategoryListHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "category_list.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "category"
        private const val CATEGORY_NAME = "name"
        private const val CATEGORY_IMAGE_URL = "url"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($CATEGORY_NAME TEXT , $CATEGORY_IMAGE_URL TEXT )"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
    }

    fun saveList(myList: ArrayList<wallpapers.item>) {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)

        val values = ContentValues()
        for (item in myList) {
            values.put(CATEGORY_NAME, item.name)
            values.put(CATEGORY_IMAGE_URL, item.image)
            db.insert(TABLE_NAME, null, values)
        }
        db.close()
    }

    fun getList(): ArrayList<wallpapers.item> {
        val db = readableDatabase
        val list = ArrayList<wallpapers.item>()

        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(CATEGORY_NAME))
                val url = cursor.getString(cursor.getColumnIndex(CATEGORY_IMAGE_URL))
                list.add(wallpapers.item(name, url , ""))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }
}
