package com.example.tools

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class ToolItem(
    val titleRes: Int,
    val route: String,
    val descriptionRes: Int
)

@Composable
fun HomeScreen(navController: NavController) {
    val tools = listOf(
        ToolItem(R.string.nav_date_calculator, Routes.DATE_CALC, R.string.desc_date_calc),
        ToolItem(R.string.nav_date_diff, Routes.DATE_DIFF, R.string.desc_date_diff),
        ToolItem(R.string.nav_number_converter, Routes.NUMBER_CONV, R.string.desc_number_conv),
        ToolItem(R.string.nav_map_viewer, Routes.MAP, R.string.desc_map),
        ToolItem(R.string.nav_list_manager, Routes.LIST, R.string.desc_list),
        ToolItem(R.string.nav_web_view, Routes.WEB_VIEW, R.string.desc_web_view),
        ToolItem(R.string.nav_text_filter, Routes.TEXT_FILTER, R.string.desc_text_filter),
        ToolItem(R.string.nav_password_validator, Routes.PASSWORD_VAL, R.string.desc_password_validator)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tools) { tool ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(tool.route) {
                            launchSingleTop = true
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(id = tool.titleRes),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = tool.descriptionRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}