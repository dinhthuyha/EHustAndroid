package com.prdcv.ehust.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hadt.ehust.model.TypeSubject
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subject(
    val id: String?,
    val name: String,
    val type: TypeSubject
) : Parcelable