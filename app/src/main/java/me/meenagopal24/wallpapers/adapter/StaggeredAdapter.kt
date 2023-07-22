package me.meenagopal24.wallpapers.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.utils.Constants.*


class StaggeredAdapter(
    private val list: MutableList<wallpapers.item>,
    private val change: ChangeInterface,
) :

    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_CONTENT = 0
    private val VIEW_TYPE_AD = 1
    private val AD_POSITION_INTERVAL = 9
    private var loadedAds: HashMap<Int, NativeAd> = HashMap()

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sImage: ImageView = itemView.findViewById(R.id.staggered_image)
    }

    class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val add_layout: CardView = itemView.findViewById(R.id.main_ad_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CONTENT) {
            Holder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.staggered_single, parent, false)
            )
        } else {
            val adHolder = AdViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.ad_unified_native, parent, false)
            )
            adHolder
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return VIEW_TYPE_CONTENT
        return if (position == 4 || position % AD_POSITION_INTERVAL == 0) {
            VIEW_TYPE_AD
        } else {
            VIEW_TYPE_CONTENT
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (getItemViewType(position) == VIEW_TYPE_CONTENT) {
            val contentHolder = holder as Holder
            val item = list[position]
            Glide.with(contentHolder.itemView.context)
                .load(BASE_URL_IMAGE + item.category.trim() + "/" + item.image.trim())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.grey)
                .into(contentHolder.sImage)

            contentHolder.sImage.setOnClickListener {
                change.changeFragment(position)
            }
        } else {
            val adHolder = holder as AdViewHolder
            if (loadedAds.containsKey(position)) {
                val ad = loadedAds[position]
                bindNativeAd(ad, holder.itemView)
            } else {
                loadNativeAd(adHolder.add_layout, holder.itemView, position)
            }
        }

    private fun loadNativeAd(
        adLayout: CardView,
        itemView: View,
        position: Int,
    ) {
        val adOptions = NativeAdOptions.Builder()
            .setMediaAspectRatio(MediaAspectRatio.SQUARE)
            .build()
        val adLoader: AdLoader =
            AdLoader.Builder(itemView.context, NATIVE_AD_ID)
                .withNativeAdOptions(adOptions)
                .forNativeAd { nativeAd ->
                    bindNativeAd(nativeAd, itemView)
                    loadedAds[position] = nativeAd
                }
                .withAdListener(object : AdListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle ad loading failure if needed
                        changeParams(adLayout, h = 0, m = 0)
                        notifyDataSetChanged()
                    }
                })
                .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun changeParams(adHolder: CardView, h: Int, m: Int) {
        adHolder.setPadding(0, 0, 0, 0)
        val layoutParams = adHolder.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.setMargins(m, m, m, m)
        adHolder.layoutParams = layoutParams

        val heightInPixels = dpToPx(adHolder.context, h)
        adHolder.layoutParams.height = heightInPixels
        adHolder.requestLayout()
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics
        ).toInt()
    }


    @SuppressLint("CutPasteId")
    private fun bindNativeAd(ad: NativeAd?, itemView: View) {
        if (ad != null) {
            val adView = itemView.findViewById<NativeAdView>(R.id.ad_unified_native)
            adView.visibility = View.VISIBLE

            adView.headlineView = itemView.findViewById(R.id.ad_headline)
            adView.bodyView = itemView.findViewById(R.id.ad_body)
            adView.callToActionView = itemView.findViewById(R.id.ad_call_to_action)
            adView.iconView = itemView.findViewById(R.id.ad_icon)
            adView.mediaView = itemView.findViewById(R.id.ad_media)

            // Populate the NativeAdView with the ad data
            (itemView.findViewById<TextView>(R.id.ad_headline)).text = ad.headline
            (itemView.findViewById<TextView>(R.id.ad_body)).text = ad.body
            (itemView.findViewById<Button>(R.id.ad_call_to_action)).text = ad.callToAction

            // Load the ad icon and images into their views
            val iconView = itemView.findViewById<ImageView>(R.id.ad_icon)
            if (ad.icon != null) {
                iconView.setImageDrawable(ad.icon!!.drawable)
            }

            val mediaView = itemView.findViewById<MediaView>(R.id.ad_media)
            if (ad.mediaContent != null) {
                mediaView.mediaContent = ad.mediaContent
            }
            val imageView = itemView.findViewById<ImageView>(R.id.ad_image_view)
            if (ad.mediaContent?.mainImage != null) {
                imageView.setImageDrawable(ad.mediaContent?.mainImage)
            }
            // Register the ad view for ad impression and click tracking
            adView.setNativeAd(ad)
        }
    }
}

