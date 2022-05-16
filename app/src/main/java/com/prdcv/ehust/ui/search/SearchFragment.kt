package com.prdcv.ehust.ui.search


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.prdcv.ehust.R

import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.SeachFragmentBinding
import com.prdcv.ehust.ui.projects.ProjectsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class SearchFragment : BaseFragmentWithBinding<SeachFragmentBinding>() {
    private val searchViewModel : SearchViewModel by viewModels()
    private val searchAdapter = SearchAdapter(
        clickListener = ::navigateToProfile
    )

    companion object {
        fun newInstance() = SearchFragment()
        private const val TAG = "SearchFragment"
    }

    override fun getViewBinding(inflater: LayoutInflater) =
        SeachFragmentBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            rvSearch.adapter = searchAdapter
        }

    override fun init() {
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (binding.edSearch.text.toString().isNotEmpty()){
                val input = binding.edSearch.text
                when (checkedId) {
                    R.id.rd_Gv -> {
                        try {
                            searchViewModel.searchUserById(input.toString().toInt())
                            hideKeyboard()
                        }catch (e: Exception){
                            
                        }

                    }

                    R.id.rd_Sv -> {
                        try {
                            searchViewModel.searchUserById(input.toString().toInt())
                            hideKeyboard()
                        }catch (e: Exception){

                        }
                    }

                    R.id.rd_class -> {
                        try {
                            searchViewModel.searchClassById(input.toString().toInt())
                            hideKeyboard()
                        }catch (e: Exception){

                        }
                    }
                }
            }


        }
        searchViewModel.userSearchState.observe(viewLifecycleOwner){
            when (it) {
                is State.Loading -> {

                }
                is State.Success -> {
                    searchAdapter.setItems(listOf(it.data))
                    binding.edSearch.text?.clear()
                }
                is State.Error -> {
                    Log.d(TAG, "error")
                }
            }
        }

        searchViewModel.classState.observe(viewLifecycleOwner){
            when (it) {
                is State.Loading -> {

                }
                is State.Success -> {
                    searchAdapter.setItems(listOf(it.data))
                    binding.edSearch.text?.clear()
                }
                is State.Error -> {
                    Log.d(TAG, "error")
                }
            }
        }
    }

    fun navigateToProfile(any: Any) {

    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}