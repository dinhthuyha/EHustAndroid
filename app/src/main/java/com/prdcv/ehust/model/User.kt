package com.prdcv.ehust.model

import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.annotations.SerializedName
import com.prdcv.ehust.ui.search.ItemSearch
import kotlinx.parcelize.Parcelize


@Parcelize
 class User(
    val id: Int,
    @SerializedName("full_name")
    val fullName: String? = null,
    @SerializedName("institute_of_management")
    val instituteOfManagement: String? = null,
    val gender: String? = null,
    val grade: String? = null,
    val course: String? = null,
    val email: String? = null,
    @SerializedName("cadre_status")
    val cadreStatus: String? = null,
    val unit: String? = null,
    @SerializedName("role")
    val roleId: Role,
    @SerializedName("image_background")
    val imageBackground: String? = null,
    @SerializedName("image_avatar")
    val imageAvatar: String? = null,
) : Parcelable, ItemSearch
