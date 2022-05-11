package com.prdcv.ehust.base.recyclerview

import androidx.annotation.IntDef

@IntDef(
    TYPE_UNKNOWN,
    TYPE_HEADER,
    TYPE_ITEM,
    TYPE_QUESTION_PART_1,
    TYPE_FOOTER
)
@Retention(AnnotationRetention.SOURCE)
annotation class RecyclerType

const val TYPE_UNKNOWN = -1
const val TYPE_HEADER = 0
const val TYPE_ITEM = 1
const val TYPE_FOOTER = 2
const val TYPE_QUESTION_PART_1 = 3
const val TYPE_QUESTION_PART_2 = 4
const val TYPE_QUESTION_PART_3 = 5
const val TYPE_QUESTION_PART_4 = 6
const val TYPE_QUESTION_PART_5 = 7
const val TYPE_QUESTION_PART_6 = 8
const val TYPE_QUESTION_PART_7 = 9
