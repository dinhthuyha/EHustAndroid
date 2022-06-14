package com.prdcv.ehust.model

import com.google.gson.annotations.SerializedName

data class Subject(
    val id: String,
    val name: String,
    @SerializedName("is_project")
    val isPoject: Boolean,

)