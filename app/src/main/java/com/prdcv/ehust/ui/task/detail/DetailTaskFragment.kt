package com.prdcv.ehust.ui.task.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.prdcv.ehust.base.BaseFragment
import com.prdcv.ehust.calendar.CalendarScreen
import com.prdcv.ehust.viewmodel.DetailTaskViewModel

class DetailTaskFragment : BaseFragment() {

    private var idTask: Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initArg()
    }
    private fun initArg(){
        val args: DetailTaskFragmentArgs by navArgs()
        idTask = args.idTask
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.Home.route) {
                    composable(Routes.Home.route) {
                        val viewModel = hiltViewModel<DetailTaskViewModel>()
                        DetailTask(
                            onDateSelectionClicked = {
                                navController.navigate(Routes.Calendar.route)
                            },
                            mainViewModel = viewModel,
                            id = idTask,
                            mNavController = findNavController()
                        )
                        Log.d("hadinh", "onViewCreated: "+ viewModel?.calendarState.calendarUiState.value.selectedDatesFormatted)
                    }
                    composable(Routes.Calendar.route) {
                        val parentEntry = remember {
                            navController.getBackStackEntry(Routes.Home.route)
                        }
                        val parentViewModel = hiltViewModel<DetailTaskViewModel>(
                            parentEntry
                        )
                        CalendarScreen(onBackPressed = {
                            navController.popBackStack()
                        }, mainViewModel = parentViewModel)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}