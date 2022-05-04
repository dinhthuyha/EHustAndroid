package com.prdcv.ehust.ui.infromationStudent

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prdcv.ehust.R

class InformationStudentFragment : Fragment() {

    companion object {
        fun newInstance() = InformationStudentFragment()
    }

    private lateinit var viewModel: InformationStudentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.infromation_student_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InformationStudentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}