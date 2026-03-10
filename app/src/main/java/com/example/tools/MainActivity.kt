package com.example.tools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Tools()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tools() {
    var currentScreen by remember { mutableStateOf("Kalkulator Dat") }
    var isMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = currentScreen) },
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
                        containerColor = colorResource(id = R.color.szary)
                    ) {
                        DropdownMenuItem(
                            text =  { Text("Kalkulator Dat", color = colorResource(id = R.color.bialy)) },
                            onClick = {
                                currentScreen = "Kalkulator Dat"
                                isMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Pusty Projekt 2", color = colorResource(id = R.color.bialy)) },
                            onClick = {
                                currentScreen = "Pusty Projekt 2"
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
                .background(colorResource(id = R.color.ciemnySzary) )
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            when (currentScreen) {
                "Kalkulator Dat" -> DateCalculatorScreen()
                "Pusty Projekt 2" -> Text("Placeholder", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
