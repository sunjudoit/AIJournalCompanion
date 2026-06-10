package com.example.aijournalcompanion.ui

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import android.content.Context

@Composable
fun HelpDialog(
    context: Context,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Help")
        },

        text = {

            AndroidView(
                factory = {

                    WebView(context).apply {
                        loadUrl("file:///android_asset/help.html")
                    }
                }
            )
        },

        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Close")
            }
        }
    )
}