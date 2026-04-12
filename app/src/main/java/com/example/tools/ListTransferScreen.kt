package com.example.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTransferScreen() {
    val leftList = remember { mutableStateListOf("Mleko", "Chleb", "Jajka", "Masło") }
    val rightList = remember { mutableStateListOf<String>() }

    var inputText by remember { mutableStateOf("") }
    val maxLength = 20

    var selection by remember { mutableStateOf<Pair<Int, Int>?>(null) }

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
                value = inputText,
                onValueChange = { if (it.length <= maxLength) inputText = it },
                label = { Text("Nowy element ($maxLength znaków)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = colorResource(id = R.color.bardzoJasnySzary),
                    unfocusedBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                    focusedTextColor = colorResource(id = R.color.jasnyNiebieski),
                    focusedBorderColor = colorResource(id = R.color.jasnyNiebieski)
                )
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        leftList.add(inputText.trim())
                        inputText = ""
                        selection = null
                    }
                },
                modifier = Modifier.background(colorResource(id = R.color.niebieskiGlowny), shape = RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Dodaj", tint = colorResource(id = R.color.bialy))
            }

            IconButton(
                onClick = {
                    selection?.let { (listId, index) ->
                        if (listId == 1 && index < leftList.size) leftList.removeAt(index)
                        else if (listId == 2 && index < rightList.size) rightList.removeAt(index)
                        selection = null
                    }
                },
                enabled = selection != null,
                modifier = Modifier.background(
                    if (selection != null) colorResource(id = R.color.czerwonyGlowny) else colorResource(id = R.color.szary),
                    shape = RoundedCornerShape(8.dp)
                )
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Usuń", tint = colorResource(id = R.color.bialy))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.szary))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Do kupienia", color = colorResource(id = R.color.jasnyNiebieski), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(leftList) { index, item ->
                            val isSelected = selection?.first == 1 && selection?.second == index
                            Text(
                                text = "${index + 1}. $item",
                                color = if (isSelected) colorResource(id = R.color.bialy) else colorResource(id = R.color.bardzoJasnySzary),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) colorResource(id = R.color.niebieskiGlowny) else colorResource(id = R.color.szary))
                                    .clickable { selection = Pair(1, index) }
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
                val canMoveRight = selection?.first == 1
                IconButton(
                    onClick = {
                        selection?.let { (_, index) ->
                            val item = leftList.removeAt(index)
                            rightList.add(item)
                            selection = null
                        }
                    },
                    enabled = canMoveRight
                ) {
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = "Przenieś w prawo",
                        tint = if (canMoveRight) colorResource(id = R.color.jasnyNiebieski) else colorResource(id = R.color.bardzoJasnySzary)
                    )
                }

                val canMoveLeft = selection?.first == 2
                IconButton(
                    onClick = {
                        selection?.let { (_, index) ->
                            val item = rightList.removeAt(index)
                            leftList.add(item)
                            selection = null
                        }
                    },
                    enabled = canMoveLeft
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Przenieś w lewo",
                        tint = if (canMoveLeft) colorResource(id = R.color.jasnyNiebieski) else colorResource(id = R.color.bardzoJasnySzary)
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.szary))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("Kupione", color = colorResource(id = R.color.jasnyNiebieski), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(rightList) { index, item ->
                            val isSelected = selection?.first == 2 && selection?.second == index
                            Text(
                                text = "${index + 1}. $item",
                                color = if (isSelected) colorResource(id = R.color.bialy) else colorResource(id = R.color.bardzoJasnySzary),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) colorResource(id = R.color.niebieskiGlowny) else colorResource(id = R.color.szary))
                                    .clickable { selection = Pair(2, index) }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}