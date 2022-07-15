package com.prdcv.ehust.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hadt.ehust.model.TypeNotification
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    val id: Int? = 0,
    val title:String,
    val content: String,
    @SerializedName("date_post")
    val datePost: String,
    val type: TypeNotification,
    val status: StatusNotification,
    @SerializedName("name_user_post")
    val nameUserPost: String,
    @SerializedName("id_user_post")
    val idUserPost: Int,
    @SerializedName("id_task")
    val idTask: Int? = null
) : Parcelable
