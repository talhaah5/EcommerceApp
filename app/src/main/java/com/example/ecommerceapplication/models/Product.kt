package com.example.ecommerceapplication.models

import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerceapplication.R

class Product( ) : Parcelable {


    var id: String = ""
    var title: String = ""
    var price: String = ""
    var category: String = ""
    var description: String = ""
    var image: String = ""
    var rating:Rating = Rating()
    var quantity:Int = 0


    constructor(parcel: Parcel) : this() {

        id = parcel.readString().toString()
        title = parcel.readString().toString()
        price = parcel.readString().toString()
        description = parcel.readString().toString()
        image = parcel.readString().toString()
        rating = parcel.readParcelable(Rating::class.java.classLoader)!!
        quantity = parcel.readInt()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {


        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(price)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeParcelable(rating, flags)
        parcel.writeInt(quantity)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }


        private const val TAG = "Product"
        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }

        @JvmStatic
        @BindingAdapter("app:loadImage")
        fun loadImage(view: ImageView, url: String?) {


            val reqOptions = RequestOptions()
                .fitCenter()
                .transform(RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(view.width,view.height)

            Glide.with(view.context)
                .applyDefaultRequestOptions(reqOptions)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(view)
        }


        @JvmStatic
        @BindingAdapter("app:totalPrice")
        fun totalPrice(view: TextView, product: Product) {
            view.text = (product.price.toDouble() * product.quantity.toDouble()).toString()
        }
    }


}
