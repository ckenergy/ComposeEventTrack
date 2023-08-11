package com.ckenergy.compose.event.track

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.*
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.*
import com.google.accompanist.navigation.animation.*

/**
 * 导航图
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    startDestination: String = "ROUTE_MAIN"
) {
    val navController = rememberAnimatedNavController()

    val springSpec = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = springSpec)
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = springSpec
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = springSpec
            )

        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = springSpec
            )

        },
    ) {
        composableHorizontal("ROUTE_MAIN") {
            MainPage {
                navController.navigate("ROUTE_SECOND")
            }
        }
        composableHorizontal("ROUTE_SECOND") {
            SecondPage()
        }

    }
}


/**
 * Compose composableHorizontal
 */
@ExperimentalAnimationApi
fun NavGraphBuilder.composableHorizontal(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    this@composableHorizontal.composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        content = content,
    )
}
