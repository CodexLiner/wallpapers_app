package me.meenagopal24.wallpapers.temp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.adapter.StaggeredAdapter
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.ApiResponseDezky
import me.meenagopal24.wallpapers.network.RetrofitClient
import me.meenagopal24.wallpapers.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : AppCompatActivity(), ChangeInterface {
    private var name: String? = null
    private var category: String? = null
    lateinit var list: ArrayList<ApiResponseDezky.item>
    lateinit var wallpapersRecycler: RecyclerView
    lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        name = intent.getStringExtra("name")!!
        category = intent.getStringExtra("category");


        findViewById<TextView>(R.id.title_selected).text = name
        progress = findViewById(R.id.progress)
        wallpapersRecycler = findViewById(R.id.staggered_recycler)
        wallpapersRecycler.layoutManager =
            GridLayoutManager(this@CategoryActivity, 2, LinearLayoutManager.VERTICAL, false)

        val gridLayoutManager =
            GridLayoutManager(this@CategoryActivity, 2) // Use 2 for the span count
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (wallpapersRecycler.adapter?.getItemViewType(position) == Constants.VIEW_TYPE_AD) 2 else 1
            }
        }
        wallpapersRecycler.layoutManager = gridLayoutManager
        getItemsFromCategory()
    }

    private fun getItemsFromCategory() {
        val whenFavourite: LinearLayout? = findViewById(R.id.when_favourite)
        val call: Call<ApiResponseDezky> =
            RetrofitClient.getInstance().api.getWallpaperByCategory(category)
        call.enqueue(object : Callback<ApiResponseDezky> {
            override fun onResponse(call: Call<ApiResponseDezky>, response: Response<ApiResponseDezky>) {
                progress.visibility = View.GONE
                if (response.body()?.list != null && response.body()?.list?.size != 0) {
                    list = response.body()?.list as ArrayList<ApiResponseDezky.item>
                    wallpapersRecycler.adapter =
                        response.body()?.list?.let {
                            StaggeredAdapter(
                                it,
                                this@CategoryActivity
                            )
                        }
                    whenFavourite?.visibility = View.INVISIBLE
                } else {
                    whenFavourite?.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ApiResponseDezky>, t: Throwable) {
                whenFavourite?.visibility = View.VISIBLE
                Toast.makeText(this@CategoryActivity, "something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun changeFragment(position: Int) {
        val intent = Intent(this@CategoryActivity, PreviewActivity::class.java)
        intent.putParcelableArrayListExtra("pre_list", list)
        intent.putExtra("position", position)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun changeFragment(title: String?, category: String?) {

    }
}