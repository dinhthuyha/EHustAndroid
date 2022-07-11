package com.prdcv.ehust.ui.search


import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.base.BaseFragmentWithBinding
import com.prdcv.ehust.common.State
import com.prdcv.ehust.databinding.SeachFragmentBinding
import com.prdcv.ehust.extension.hideKeyboard
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.main.MainFragmentDirections
import com.prdcv.ehust.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


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
        binding.rdSv.isChecked = true
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rd_Gv -> {
                    binding.edSearch.hint = "Mã giảng viên/ tên giảng viên"
                }

                R.id.rd_Sv -> {
                    binding.edSearch.hint = "Mã số sinh viên/ tên sinh viên"

                }

                R.id.rd_class -> {
                    binding.edSearch.hint = "Mã lớp"
                    binding.edSearch.inputType = InputType.TYPE_CLASS_NUMBER
                }
            }
        }

        binding.edSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && textView.text.toString().isNotEmpty()) {
                hideKeyboard()
                val input = binding.edSearch.text
                when (binding.radioGroup.checkedRadioButtonId) {
                    R.id.rd_Gv -> {
                        try {
                            searchViewModel.searchUserById(
                                input.toString().toInt(),
                                Role.ROLE_TEACHER
                            )
                        } catch (e: Exception) {
                            searchViewModel.searchUserByFullName(input.toString(),Role.ROLE_TEACHER)
                        }

                    }

                    R.id.rd_Sv -> {
                        try {
                            searchViewModel.searchUserById(
                                input.toString().toInt(),
                                Role.ROLE_STUDENT
                            )

                        } catch (e: Exception) {
                            searchViewModel.searchUserByFullName(input.toString(),Role.ROLE_STUDENT)
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
                    updateResultSuccessSearchUser(it)
                }
                is State.Error -> {
                    updateResultErrorSearch()
                }
            }
        }

        searchViewModel.classState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {

                }
                is State.Success -> {
                    updateResultSuccessSearchClass(it)
                }
                is State.Error -> {
                    updateResultErrorSearch()
                }
            }
        }
    }

    private fun updateResultErrorSearch() {
        searchAdapter.setItems(listOf())
        binding.searchResult.visibility = View.VISIBLE
        binding.searchResult.text = "Không tìm thấy kết quả phù hợp"
    }

    private fun updateResultSuccessSearchClass(it: State.Success<ClassStudent>) {
        searchAdapter.setItems(listOf(it.data))
        binding.searchResult.visibility = View.GONE
        binding.edSearch.text?.clear()
    }

    private fun updateResultSuccessSearchUser(it: State.Success<User>) {
        searchAdapter.setItems(listOf(it.data))
        binding.searchResult.visibility = View.GONE
        binding.edSearch.text?.clear()
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

}