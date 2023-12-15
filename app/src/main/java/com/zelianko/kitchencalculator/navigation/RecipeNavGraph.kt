package com.zelianko.kitchencalculator.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.recipe_add_screen.RecipeAddScreen
import com.zelianko.kitchencalculator.recipe_update_screen.RecipeUpdateScreen
import com.zelianko.kitchencalculator.subactivity.Drawer
import com.zelianko.kitchencalculator.util.Routes

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun RecipeNavGraph(productViewModel: ProductViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.RECIPE_LIST_SCREEN) {
        composable(Routes.RECIPE_ADD_SCREEN) {
            RecipeAddScreen {
                navController.navigate(it)
            }
        }
        composable(Routes.RECIPE_UPDATE_SCREEN + "/{recipeId}") {
            RecipeUpdateScreen {
                navController.popBackStack()
            }
        }
        composable(Routes.RECIPE_LIST_SCREEN) {
            Drawer(
                productViewModel = productViewModel,
                mainNavHostController = navController
            )
        }
    }
}