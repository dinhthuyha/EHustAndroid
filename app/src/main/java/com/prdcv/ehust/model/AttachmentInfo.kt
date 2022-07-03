package com.prdcv.ehust.model

import java.io.InputStream

data class AttachmentInfo(val inputStream: InputStream, val filename: String, val contentType: String?)