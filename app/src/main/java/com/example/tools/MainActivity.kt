package com.example.tools

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Tools()
            }
        }
    }
}

fun changeLanguage(languageCode: String) {
    android.util.Log.d("MultiToolApp", "Zmieniam język na: $languageCode")
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
fun Tools() {
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
                    containerColor = colorResource(id = R.color.niebieskiGlowny),
                    titleContentColor = colorResource(id = R.color.bialy)
                ),
                actions = {
                    IconButton(onClick = { isMenuExpanded = true }) {
                        Icon(Icons.Filled.MoreVert,
                            contentDescription = "Menu",
                            tint = colorResource(id = R.color.bialy))
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        modifier = Modifier.background(colorResource(id = R.color.szary))
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_date_calculator), color = colorResource(id = R.color.bialy)) },
                            onClick = {
                                currentScreen = AppScreen.DATE_CALCULATOR
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_date_diff),
                                color = colorResource(id = R.color.bialy)) },
                            onClick = {
                                currentScreen = AppScreen.DATE_DIFFERENCE
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_number_converter), color = colorResource(id = R.color.bialy)) },
                            onClick = {
                                currentScreen = AppScreen.NUMBER_CONVERTER
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_map_viewer), color = colorResource(id = R.color.bialy)) },
                            onClick = {
                                currentScreen = AppScreen.MAP_VIEWER
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.nav_list_manager), color = colorResource(id = R.color.bialy)) },
                            onClick = {
                                currentScreen = AppScreen.LIST_MANAGER
                                isMenuExpanded = false
                            }
                        )

                        HorizontalDivider(color = colorResource(id = R.color.bardzoJasnySzary))

                        DropdownMenuItem(
                            text = { Text("Polski 🇵🇱", color = colorResource(id = R.color.bialy)) },
                            onClick = {
                                changeLanguage("pl")
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("English 🇺🇸", color = colorResource(id = R.color.bialy)) },
                            onClick = {
                                changeLanguage("en")
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
                .background(colorResource(id = R.color.ciemnySzary))
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