package com.prdcv.ehust.ui.admin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

sealed class UserUIAction {
    object OnAddressAutoCompleteDone : UserUIAction()
    data class OnAddressAutoCompleteClear(val userSelect: MutableState<String>) : UserUIAction()
    data class OnAddressChange(val value: String) : UserUIAction()
    data class OnAddressSelected(val selectedItem: String) : UserUIAction()
}