package com.prdcv.ehust.model

import com.google.gson.annotations.SerializedName

data class Attachment(
    @SerializedName("filename")
    val filename: String? = null,
    @SerializedName("file_path")
    val filePath: String? = null,
)