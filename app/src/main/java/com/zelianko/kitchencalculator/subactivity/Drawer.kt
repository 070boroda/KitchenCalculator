package com.zelianko.kitchencalculator.subactivity

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.activity.FirstScreen
import com.zelianko.kitchencalculator.modelview.ProductViewModel
import com.zelianko.kitchencalculator.navigation.NavGraf
import com.zelianko.kitchencalculator.recipe_list_screen.RecipeListScreen
import com.zelianko.kitchencalculator.util.Routes
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Drawer(
    productViewModel: ProductViewModel,
    mainNavHostController: NavHostController,
) {
    val items = listOf(
        NavigationItem(
            title = stringResource(R.string.draw_menu_two),
            selectedIcon = ImageVector.vectorResource(R.drawable.ic_my_recipe),
            unselectedIcon = ImageVector.vectorResource(R.drawable.ic_my_recipe),
            route = Routes.RECIPE_LIST_SCREEN
        ),
        NavigationItem(
            title = stringResource(R.string.draw_menu_one),
            selectedIcon = ImageVector.vectorResource(R.drawable.ic_vesi),
            unselectedIcon = ImageVector.vectorResource(R.drawable.ic_vesi),
            route = Routes.COUNTER_SCREEN
        ),
    )
    val navController = rememberNavController()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        var selectedItem by rememberSaveable {
            mutableStateOf(items.get(selectedItemIndex).title)
        }

        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = colorResource(id = R.color.white)
                ) {
                    Box(
                        modifier
                        = Modifier.wrapContentSize()
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.background_header_menu),
                                contentDescription = "Menu",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(32.dp))
                            items.forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = colorResource(id = R.color.orange_primary),
                                        unselectedContainerColor = colorResource(id = R.color.white)
                                    ),
                                    shape = RectangleShape,
                                    label = {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    selected = index == selectedItemIndex,
                                    onClick = {
                                        navController.navigate(item.route)
                                        selectedItemIndex = index
                                        selectedItem = item.title
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedItemIndex) {
                                                item.selectedIcon
                                            } else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    },
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    }
                }
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar =
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                colorResource(id = R.color.all_background)
                            )
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    selectedItem,
                                    style = TextStyle(fontSize = 24.sp)
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu"
                                    )
                                }
                            },
                            actions = {
                                if (selectedItemIndex == 0) {
                                    IconButton(
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .background(
                                                colorResource(id = R.color.orange_primary),
                                                shape = CircleShape
                                            ),
                                        onClick = {
                                            //Переход на экран жобавления рецепта
                                            mainNavHostController.navigate(Routes.RECIPE_ADD_SCREEN)
//                                            recipeViewModel.onEvent(
//                                                RecipeListEvent.OnItemClick(
//                                                    Routes.RECIPE_ADD_SCREEN
//                                                )
//                                            )
                                        }) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
                                            contentDescription = "plus"
                                        )
                                    }
                                }
                            },
                            colors = topAppBarColors(
                                containerColor = Color.Transparent,
                            )
                        )
                    }
                },
            ) { paddingValues ->
                NavGraf(
                    navHostController = navController,
                    generalScreenContent = {
                        FirstScreen(
                            productViewModel = productViewModel,
                            paddingValues = paddingValues
                        )
                    },
                    secondScreenContent = {
                        RecipeListScreen()
                    }
                )
            }
        }
    }
}

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: String
)