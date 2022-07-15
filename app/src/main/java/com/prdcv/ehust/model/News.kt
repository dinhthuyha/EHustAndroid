package com.prdcv.ehust.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hadt.ehust.model.StatusNotification
import com.hadt.ehust.model.TypeNotification
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    val id: Int,
    val title:String,
    val content: String,
    @SerializedName("date_post")
    val datePost: String,
    val type: TypeNotification,
    val status: StatusNotification
) : Parcelable
