package com.example.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

enum class ListSide { LEFT, RIGHT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTransferScreen(
    viewModel: ListViewModel = viewModel()
) {
    val selection = viewModel.selection

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viewModel.inputText,
                onValueChange = { viewModel.onInputTextChanged(it) },
                label = { Text(stringResource(id = R.string.label_new_item, 20)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            IconButton(
                onClick = { viewModel.addItem() },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.desc_add), tint = MaterialTheme.colorScheme.onPrimary)
            }

            IconButton(
                onClick = { viewModel.deleteSelectedItem() },
                enabled = selection != null,
                modifier = Modifier.background(
                    if (selection != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
            ) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(id = R.string.desc_delete), tint = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(stringResource(id = R.string.title_to_buy), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(viewModel.leftList) { index, item ->
                            val isSelected = selection?.first == ListSide.LEFT && selection?.second == index
                            Text(
                                text = "${index + 1}. $item",
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                                    .clickable { viewModel.selectItem(ListSide.LEFT, index) }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val canMoveRight = selection?.first == ListSide.LEFT
                IconButton(
                    onClick = { viewModel.moveRight() },
                    enabled = canMoveRight
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(id = R.string.desc_move_right),
                        tint = if (canMoveRight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                val canMoveLeft = selection?.first == ListSide.RIGHT
                IconButton(
                    onClick = { viewModel.moveLeft() },
                    enabled = canMoveLeft
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.desc_move_left),
                        tint = if (canMoveLeft) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(stringResource(id = R.string.title_bought), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(viewModel.rightList) { index, item ->
                            val isSelected = selection?.first == ListSide.RIGHT && selection?.second == index
                            Text(
                                text = "${index + 1}. $item",
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                                    .clickable { viewModel.selectItem(ListSide.RIGHT, index) }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}