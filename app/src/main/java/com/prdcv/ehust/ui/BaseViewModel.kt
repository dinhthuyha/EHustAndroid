package com.prdcv.ehust.ui

import androidx.lifecycle.ViewModel
import com.prdcv.ehust.data.model.User

abstract  class BaseViewModel: ViewModel() {
    var user: User? = null
}