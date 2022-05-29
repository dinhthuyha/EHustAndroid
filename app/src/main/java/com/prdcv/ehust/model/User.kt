package com.prdcv.ehust.model

import android.os.Parcelable
import com.prdcv.ehust.ui.search.ItemSearch
import kotlinx.parcelize.Parcelize


@Parcelize
 class User(
    val id: Int,
    val fullName: String? = null,
    val instituteOfManagement: String? = null,
    val gender: String? = null,
    val grade: String? = null,
    val course: String? = null,
    val email: String? = null,
    val cadreStatus: String? = null,
    val unit: String? = null,
    val roleId: Role,
    val imageBackground: String? = null,
    val imageAvatar: String? = null,
) : Parcelable, ItemSearch
