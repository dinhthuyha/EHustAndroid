package com.prdcv.ehust

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.databinding.HomeFragmentBinding

class HomeFragment : BaseFragmentWithBinding<HomeFragmentBinding>(){

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun getViewBinding(inflater: LayoutInflater): HomeFragmentBinding = HomeFragmentBinding.inflate(inflater)

    override fun init() {
        binding.name.text =
            "Trong cuộc họp báo trước Madrid Open, tay vợt số một thế giới Novak Djokovic một lần nữa nhấn mạnh sự hà khắc mà Wimbledon đưa ra với các tay vợt Nga, Belarus. Anh nói: Tôi nghĩ quyết định đó là sai lầm. Chúng ta không thể chống lại các cá nhân đơn lẻ, vì những vấn đề tầm vóc quốc gia. Điều mà các tay vợt muốn chỉ đơn giản là thi đấu. Họ được điều chỉnh bởi luật lệ của ATP, WTA, ITF và không làm gì sai trong trường hợp này"
        binding.name.isSelected = true

    }
}