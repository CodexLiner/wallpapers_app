package me.meenagopal24.wallpapers.adapter

//import android.app.Activity
//import android.util.DisplayMetrics
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.AdSize
//import com.google.android.gms.ads.AdView
//import me.meenagopal24.wallpapers.R
//import me.meenagopal24.wallpapers.interfaces.ChangeInterface
//import me.meenagopal24.wallpapers.models.wallpapers
//import me.meenagopal24.wallpapers.utils.Constants.*


//class StaggeredAdapter(
//    private val list: MutableList<wallpapers.item>,
//    private val change: ChangeInterface,
//) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val sImage: ImageView = itemView.findViewById(R.id.staggered_image)
//
//    }
//
//    class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var adView: AdView = itemView.findViewById(R.id.adView)
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == VIEW_TYPE_CONTENT) {
//            Holder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.staggered_single, parent, false)
//            )
//        } else {
//            AdViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.staggered_single_ad, parent, false)
//            )
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (position != 0 && position % 4 == 0) {
//            VIEW_TYPE_AD;
//        } else VIEW_TYPE_CONTENT
//    }
//
//    override fun getItemCount(): Int {
//        return list.size ?: 0
//    }
//
//    override fun onBindViewHolder(args: RecyclerView.ViewHolder, position: Int) {
//        if (getItemViewType(position) == VIEW_TYPE_CONTENT) {
//            val holder: Holder = args as Holder;
//            Glide.with(holder.itemView.context)
//                .load(BASE_URL_IMAGE + list[position].category.trim() + "/" + list[position].image.trim())
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .placeholder(R.drawable.grey).into(holder.sImage)
//            holder.sImage.setOnClickListener {
//                change.changeFragment(position)
//            }
//        } else {
//            val holder: AdViewHolder = args as AdViewHolder
//            val activity = holder.adView.context as Activity
//            val adRequest: AdRequest = AdRequest.Builder().build()
////            holder.adView.setAdSize(getCustomAdSize(activity))
////            holder.adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
//            holder.adView.loadAd(adRequest)
//        }
//
//    }
//
//    private fun getCustomAdSize(activity: Activity): AdSize {
//        val widthInPixels = activity.resources.getDimensionPixelSize(R.dimen.ad_width)
//        val heightInPixels = activity.resources.getDimensionPixelSize(R.dimen.ad_height)
//        return AdSize(widthInPixels, heightInPixels)
//    }
//
//    fun getAdaptiveBannerAdSize(activity: Activity, adView: AdView): AdSize {
//        val displayMetrics = DisplayMetrics()
//        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
//
//        val adWidthPixels = adView.width.toFloat()
//        val density = displayMetrics.density
//        val adWidth = (adWidthPixels / density).toInt()
//
//        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
//    }
//}


import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.utils.Constants.*


class StaggeredAdapter(
    private val list: MutableList<wallpapers.item>,
    private val change: ChangeInterface,
    private var adLoaded: Boolean = false,
    private var ad: NativeAd? = null,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_CONTENT = 0
    private val VIEW_TYPE_AD = 1
    private val AD_POSITION_INTERVAL = 9

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sImage: ImageView = itemView.findViewById(R.id.staggered_image)
    }

    class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adView: NativeAdView = itemView.findViewById(R.id.ad_unified_native)
        val add_layout: CardView = itemView.findViewById(R.id.main_ad_layout)
        var adLoaded: Boolean = false // Add this property to track ad loading status

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CONTENT) {
            Holder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.staggered_single, parent, false)
            )
        } else {
//            AdViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.ad_unified_native, parent, false)
//            )
            val adHolder = AdViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.ad_unified_native, parent, false)
            )

            // Set the ad layout visibility based on the isAdLoaded flag
            adHolder.add_layout.visibility = if (adLoaded) View.VISIBLE else View.VISIBLE

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
            // Bind your regular content data to the content item view holder
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
//            // Here, you'll load and bind the native ad to the native ad item view holder
//            val adHolder = holder as AdViewHolder
//            loadNativeAd(adHolder.adView, holder.itemView, position)
//            if (adHolder.adLoaded) {
//                adHolder.add_layout.visibility = View.VISIBLE
//            } else {
////             adHolder.add_layout.visibility = View.GONE
//            }

            //here new one
            val adHolder = holder as AdViewHolder
            if (ad != null) {
                bindNativeAd(adHolder.adView, ad, holder.itemView)
            } else {
                loadNativeAd(adHolder.adView, adHolder.add_layout, holder.itemView)
            }

        }

    private fun changeParams(adHolder: CardView) {
        adHolder.setPadding(0, 0, 0, 0)
        val layoutParams = adHolder.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.setMargins(0, 0, 0, 0)
        adHolder.layoutParams = layoutParams

        adHolder.layoutParams.height = 0
        adHolder.requestLayout()
    }

    private fun loadNativeAd(adView: NativeAdView, adLayout: CardView, itemView: View) {
        val adLoader: AdLoader = AdLoader.Builder(adView.context, NATIVE_AD_ID)
            .forNativeAd { nativeAd ->
                ad = nativeAd
                bindNativeAd(adView, ad, itemView)
                adLoaded = true

                // Notify the adapter that data has changed
                notifyDataSetChanged()
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    ad = null
                    changeParams(adLayout)
                    adLoaded = false

                    // Notify the adapter that data has changed
                    notifyDataSetChanged()
                }
            })
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun bindNativeAd(adView: NativeAdView, ad: NativeAd?, itemView: View) {
        if (ad != null) {
            val color = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.white))
            val styles: NativeTemplateStyle =
                NativeTemplateStyle
                    .Builder()
                    .withMainBackgroundColor(color)
                    .build()
            val template: TemplateView = itemView.findViewById(R.id.my_template)
            template.setStyles(styles)
            template.setNativeAd(ad)
        }
    }


//    @SuppressLint("NotifyDataSetChanged")
//    private fun loadNativeAd(adView: NativeAdView, itemView: View, position: Int) {
////        if (adLoaded && ad != null) {
////            return
////        }
////        val adSize = AdSize(300, AdSize.BANNER.height)
////        val builder = AdLoader.Builder(adView.context, "ca-app-pub-3940256099942544/2247696110")
////        val adLoader = builder.forNativeAd { nativeAd ->
////            ad = nativeAd
////            adLoaded = true
////            notifyDataSetChanged() // Notify the adapter once the ad is loaded
////        }.build()
//        val color = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.white))
//        val adLoader: AdLoader = AdLoader.Builder(itemView.context, NATIVE_AD_ID)
//            .forNativeAd { nativeAd ->
//                val styles: NativeTemplateStyle =
//                    NativeTemplateStyle.Builder().withMainBackgroundColor(color).build()
//                val template: TemplateView = itemView.findViewById(R.id.my_template)
//                template.setStyles(styles)
//                template.setNativeAd(nativeAd)
//            }.withAdListener(object : AdListener() {
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    notifyDataSetChanged()
//                }
//            })
//            .build()
//        adLoader.loadAd(AdRequest.Builder().build())
//    }
}
