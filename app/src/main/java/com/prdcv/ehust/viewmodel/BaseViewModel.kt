package com.prdcv.ehust.viewmodel

import androidx.lifecycle.ViewModel
import com.prdcv.ehust.model.User

abstract  class BaseViewModel: ViewModel() {
    var user: User? = null
}