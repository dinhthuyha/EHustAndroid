package com.prdcv.ehust.data.model

import com.google.gson.annotations.SerializedName

enum class ProgressStatus(val text: String) {
    @SerializedName("Đang thực hiện")
    RESPONDING("Đang thực hiện"),

    @SerializedName("Đã hoàn thành")
    DONE("Đã hoàn thành"),

    @SerializedName("Chưa hoàn thành")
    UNFINISHED("Chưa hoàn thành")

}