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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tools.ui.theme.MultiToolAppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultiToolAppTheme {
                Tools()
            }
        }
    }
}

fun changeLanguage(languageCode: String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
    AppCompatDelegate.setApplicationLocales(appLocale)
}

enum class AppScreen {
    DATE_CALCULATOR,
    DATE_DIFFERENCE,
    NUMBER_CONVERTER,
    MAP_VIEWER,
    LIST_MANAGER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tools(
    mainViewModel: MainViewModel = viewModel()
) {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.DATE_CALCULATOR) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val screenTitle = when (currentScreen) {
        AppScreen.DATE_CALCULATOR -> stringResource(id = R.string.nav_date_calculator)
        AppScreen.DATE_DIFFERENCE -> stringResource(id = R.string.nav_date_diff)
        AppScreen.NUMBER_CONVERTER -> stringResource(id = R.string.nav_number_converter)
        AppScreen.MAP_VIEWER -> stringResource(id = R.string.nav_map_viewer)
        AppScreen.LIST_MANAGER -> stringResource(id = R.string.nav_list_manager)
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
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_date_calculator), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                currentScreen = AppScreen.DATE_CALCULATOR
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_date_diff), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                currentScreen = AppScreen.DATE_DIFFERENCE
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_number_converter), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                currentScreen = AppScreen.NUMBER_CONVERTER
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_map_viewer), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                currentScreen = AppScreen.MAP_VIEWER
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_list_manager), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                currentScreen = AppScreen.LIST_MANAGER
                                isMenuExpanded = false
                            }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

                        DropdownMenuItem(
                            text = { Text("Polski 🇵🇱", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                changeLanguage("pl")
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("English 🇺🇸", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                changeLanguage("en")
                                isMenuExpanded = false
                            }
                        )

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.theme_system), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                mainViewModel.updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.theme_light), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                mainViewModel.updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.theme_dark), color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                mainViewModel.updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            when (currentScreen) {
                AppScreen.DATE_CALCULATOR -> DateCalculatorScreen()
                AppScreen.DATE_DIFFERENCE -> DateDifferenceScreen()
                AppScreen.NUMBER_CONVERTER -> NumberConverterScreen()
                AppScreen.MAP_VIEWER -> MapScreen()
                AppScreen.LIST_MANAGER -> ListTransferScreen()
            }
        }
    }
}