package com.prdcv.ehust.model

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp


data class Comment(
    val id: Int? = null,
    val content: String,
    @SerializedName(value = "id_user")
    val idUserPost: Int? = null,
    @SerializedName(value = "name_user_post")
    val nameUserPost: String? = null,
    val timestamp: Timestamp? = null
)