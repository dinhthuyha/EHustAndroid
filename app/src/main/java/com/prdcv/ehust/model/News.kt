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
    @SerializedName("name_user_update")
    val nameUserUpdate: String,
    @SerializedName("id_user_update")
    val idUserUpdate: Int,
    @SerializedName("id_task")
    val idTask: Int? = null
) : Parcelable
