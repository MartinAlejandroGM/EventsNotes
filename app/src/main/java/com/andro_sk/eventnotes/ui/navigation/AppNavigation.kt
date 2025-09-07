package com.andro_sk.eventnotes.ui.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andro_sk.eventnotes.data.local.navigation.NavigationAction
import com.andro_sk.eventnotes.domain.contracts.NavigationReceiver
import com.andro_sk.eventnotes.ui.views.upsert.AddEventView
import com.andro_sk.eventnotes.ui.views.home.HomeView
import com.andro_sk.eventnotes.ui.views.upsert.UpdateEventView
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(navigationReceiver: NavigationReceiver) {

    val navController = rememberNavController()

    LifecycleResumeEffect(Unit) {
        val navigationBusJob = lifecycleScope.launch {
            navigationReceiver.navigation.collect { action ->
                when (action) {
                    NavigationAction.NavigateBack -> navController.navigateUp()
                    is NavigationAction.NavigateTo -> {
                        if (action.clearBackStack) {
                            navController.navigate(action.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        } else {
                            navController.navigate(action.route)
                        }
                    }

                    is NavigationAction.NavigateToWithArgs -> {
                        val routeWithArgs = action.route + action.args.filterValues { it.isNotBlank() }.entries.joinToString(
                            prefix = "/",
                            separator = "/",
                            transform = { it.value }
                        )
                        if (action.clearBackStack) {
                            navController.navigate(action.route) {
                                popUpTo(routeWithArgs) { inclusive = true }
                            }
                        } else {
                            navController.navigate(routeWithArgs)
                        }
                    }
                }
            }
        }

        onPauseOrDispose {
            navigationBusJob.cancel()
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME
    ) {
        composable(route = AppRoutes.HOME) { HomeView() }
        composable(
            route = "${AppRoutes.ADD_EVENT}/{${AppRoutesArgs.FILTERED_BY}}",
            arguments = listOf(
                navArgument(AppRoutesArgs.FILTERED_BY) {
                    type = NavType.StringType
                    defaultValue = "date"
                }
            )
        ) {
            AddEventView()
        }
        composable(
            route = "${AppRoutes.EVENT_DETAILS}/{${AppRoutesArgs.EVENT_ID}}/{${AppRoutesArgs.FILTERED_BY}}",
            arguments = listOf(
                navArgument(AppRoutesArgs.EVENT_ID) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(AppRoutesArgs.FILTERED_BY) {
                    type = NavType.StringType
                    defaultValue = "date"
                }
            )) {

            val eventId = it.arguments?.getString(AppRoutesArgs.EVENT_ID)

            eventId?.let {
                UpdateEventView(eventId = eventId)
            }
        }
    }

}