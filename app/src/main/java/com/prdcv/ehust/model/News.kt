package com.prdcv.ehust.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    val id: Int,
    val title:String,
    val content: String,
    @SerializedName("date_post")
    val datePost: String
) : Parcelable
