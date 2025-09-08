package com.andro_sk.eventnotes.ui.navigation


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andro_sk.eventnotes.data.local.navigation.NavigationAction
import com.andro_sk.eventnotes.domain.contracts.NavigationReceiver
import com.andro_sk.eventnotes.ui.viewmodels.UserSettingsViewModel
import com.andro_sk.eventnotes.ui.screen.upsert.AddEventView
import com.andro_sk.eventnotes.ui.screen.home.HomeView
import com.andro_sk.eventnotes.ui.screen.upsert.UpdateEventView
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(navigationReceiver: NavigationReceiver, userSettingsViewModel: UserSettingsViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    val isDarkMode by userSettingsViewModel.isDarkMode.collectAsStateWithLifecycle()

    val colorScheme = if (isDarkMode) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    LifecycleResumeEffect(Unit) {
        userSettingsViewModel.getUserSettings()
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

    MaterialTheme(
        colorScheme = colorScheme,
        content = {
            NavHost(
                navController = navController,
                startDestination = AppRoutes.HOME
            ) {
                composable(route = AppRoutes.HOME) {
                    HomeView(isDarkMode = isDarkMode){
                        userSettingsViewModel.updateTheme()
                    }
                }
                composable(
                    route = AppRoutes.ADD_EVENT
                ) {
                    AddEventView()
                }
                composable(
                    route = "${AppRoutes.EVENT_DETAILS}/{${AppRoutesArgs.EVENT_ID}}",
                    arguments = listOf(
                        navArgument(AppRoutesArgs.EVENT_ID) {
                            type = NavType.StringType
                            defaultValue = ""
                        })
                ) {

                    val eventId = it.arguments?.getString(AppRoutesArgs.EVENT_ID)

                    eventId?.let {
                        UpdateEventView(eventId = eventId)
                    }
                }
            }
        })
}