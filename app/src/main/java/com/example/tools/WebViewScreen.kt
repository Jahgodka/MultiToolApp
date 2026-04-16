@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.example.tools

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    viewModel: WebViewModel = viewModel()
) {
    var currentUrlToLoad by remember { mutableStateOf(viewModel.urlInput) }

    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    var canGoBack by remember { mutableStateOf(false) }

    BackHandler(enabled = canGoBack) {
        webViewRef?.goBack()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = viewModel.urlInput,
                onValueChange = { viewModel.updateUrl(it) },
                label = { Text(stringResource(id = R.string.label_url)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.primary
                )
            )
            Button(
                onClick = {
                    currentUrlToLoad = viewModel.getValidUrlAndSave()
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Text("GO")
            }
        }

        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient() {
                        override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                            super.doUpdateVisitedHistory(view, url, isReload)
                            canGoBack = view?.canGoBack() ?: false
                        }
                    }
                    settings.javaScriptEnabled = true
                    webViewRef = this
                    loadUrl(currentUrlToLoad)
                }
            },
            update = { webView ->
                webView.loadUrl(currentUrlToLoad)
            },
            modifier = Modifier.weight(1f)
        )
    }
}