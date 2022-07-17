package com.prdcv.ehust.ui.admin

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.prdcv.ehust.R
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.viewmodel.AssignViewModel

private sealed class Screen(val route: String, val label: String, @DrawableRes val icon: Int) {
    object Assignment : Screen("assignment", "Trang chủ", R.drawable.ic_baseline_home_24)
    object Statistic : Screen("statistic", "Dashboard", R.drawable.ic_baseline_insert_chart_24)
}

@Composable
fun AdminMainScreen(viewModel: AssignViewModel, hideKeyboard: () -> Unit) {
    val navController = rememberNavController()

    DefaultTheme {
        Scaffold(bottomBar = { AdminBottomNavigation(navController) }) {
            NavHost(
                navController = navController,
                startDestination = Screen.Assignment.route,
                modifier = Modifier.padding(it)
            ) {
                composable(Screen.Statistic.route) { DashboardScreen(viewModel) }
                composable(Screen.Assignment.route) { AssignScreen(viewModel, hideKeyboard) }
            }
        }
    }
}

@Composable
fun AdminBottomNavigation(navController: NavController) {
    val items = listOf(Screen.Assignment, Screen.Statistic)
    BottomNavigation(backgroundColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = screen.icon), contentDescription = null) },
                selectedContentColor = colorResource(id = R.color.text_color),
                unselectedContentColor = Color.Gray,
                label = { Text(screen.label, fontSize = 10.sp) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }

}

