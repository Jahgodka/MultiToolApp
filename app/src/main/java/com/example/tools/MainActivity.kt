package com.example.tools

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tools.ui.theme.MultiToolAppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            MultiToolAppTheme {
                Tools()
            }
        }
    }
}

object Routes {
    const val HOME = "home"
    const val DATE_CALC = "date_calc"
    const val DATE_DIFF = "date_diff"
    const val NUMBER_CONV = "number_conv"
    const val MAP = "map"
    const val LIST = "list"
    const val WEB_VIEW = "web_view"
}

fun changeLanguage(languageCode: String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
    AppCompatDelegate.setApplicationLocales(appLocale)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tools(
    mainViewModel: MainViewModel = viewModel()
) {
    val navController = rememberNavController()
    var isMenuExpanded by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screenTitle = when (currentRoute) {
        Routes.HOME -> stringResource(id = R.string.app_name)
        Routes.DATE_CALC -> stringResource(id = R.string.nav_date_calculator)
        Routes.DATE_DIFF -> stringResource(id = R.string.nav_date_diff)
        Routes.NUMBER_CONV -> stringResource(id = R.string.nav_number_converter)
        Routes.MAP -> stringResource(id = R.string.nav_map_viewer)
        Routes.LIST -> stringResource(id = R.string.nav_list_manager)
        Routes.WEB_VIEW -> stringResource(id = R.string.nav_web_view)
        else -> stringResource(id = R.string.app_name)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { isMenuExpanded = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        val navigateAndClose = { route: String ->
                            navController.navigate(route) {
                                launchSingleTop = true
                            }
                            isMenuExpanded = false
                        }

                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.app_name), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { navigateAndClose(Routes.HOME) }
                        )

                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_date_calculator), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { navigateAndClose(Routes.DATE_CALC) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_date_diff), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { navigateAndClose(Routes.DATE_DIFF) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_number_converter), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { navigateAndClose(Routes.NUMBER_CONV) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_map_viewer), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { navigateAndClose(Routes.MAP) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_list_manager), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { navigateAndClose(Routes.LIST) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_web_view), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { navigateAndClose(Routes.WEB_VIEW) }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

                        DropdownMenuItem(
                            text = { Text("Polski 🇵🇱", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { changeLanguage("pl"); isMenuExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text("English 🇺🇸", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { changeLanguage("en"); isMenuExpanded = false }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.theme_light), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { mainViewModel.updateTheme(AppCompatDelegate.MODE_NIGHT_NO); isMenuExpanded = false }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.theme_dark), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { mainViewModel.updateTheme(AppCompatDelegate.MODE_NIGHT_YES); isMenuExpanded = false }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            composable(Routes.HOME) { HomeScreen(navController) }
            composable(Routes.DATE_CALC) { DateCalculatorScreen() }
            composable(Routes.DATE_DIFF) { DateDifferenceScreen() }
            composable(Routes.NUMBER_CONV) { NumberConverterScreen() }
            composable(Routes.MAP) { MapScreen() }
            composable(Routes.LIST) { ListTransferScreen() }
            composable(Routes.WEB_VIEW) { WebViewScreen() }
        }
    }
}