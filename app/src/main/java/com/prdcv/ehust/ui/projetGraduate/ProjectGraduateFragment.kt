package com.prdcv.ehust.ui.projetGraduate

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prdcv.ehust.R

class ProjectGraduateFragment : Fragment() {

    companion object {
        fun newInstance() = ProjectGraduateFragment()
    }

    private lateinit var viewModel: ProjectGraduateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.project_graduate_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProjectGraduateViewModel::class.java)
        // TODO: Use the ViewModel
    }

}