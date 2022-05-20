package com.prdcv.ehust.ui.search


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.R

import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.SeachFragmentBinding
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.main.MainFragmentDirections
import com.prdcv.ehust.ui.projects.ProjectsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class SearchFragment : BaseFragmentWithBinding<SeachFragmentBinding>() {
    private val searchViewModel: SearchViewModel by viewModels()
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
        binding.edSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && textView.text.toString().isNotEmpty()) {
                hideKeyboard()
                val input = binding.edSearch.text
                when (binding.radioGroup.checkedRadioButtonId) {
                    R.id.rd_Gv -> {
                        try {
                            searchViewModel.searchUserById(input.toString().toInt())
                        } catch (e: Exception) {

                        }

                    }

                    R.id.rd_Sv -> {
                        try {
                            searchViewModel.searchUserById(input.toString().toInt())

                        } catch (e: Exception) {

                        }
                    }

                    R.id.rd_class -> {
                        try {
                            searchViewModel.searchClassById(input.toString().toInt())

                        } catch (e: Exception) {

                        }
                    }
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false

        }

        searchViewModel.userSearchState.observe(viewLifecycleOwner) {
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

        searchViewModel.classState.observe(viewLifecycleOwner) {
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

    fun navigateToProfile(itemSearch: ItemSearch) {
        when (itemSearch as? User) {
            null -> {

            }
            else -> {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToProfileFragment(
                        itemSearch as User
                    )
                )
            }
        }
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}