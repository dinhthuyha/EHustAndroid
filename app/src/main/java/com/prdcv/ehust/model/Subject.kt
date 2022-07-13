package com.prdcv.ehust.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subject(
    val id: String?,
    val name: String,
    @SerializedName("is_project")
    val isPoject: Boolean,

) : Parcelable