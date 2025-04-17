package com.appdistributionexample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.appdistribution.FirebaseAppDistribution
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {

    val context = LocalContext.current
    val appDistribution  = FirebaseAppDistribution.getInstance()
    var showUpdateDialog by remember { mutableStateOf(false) }
    var updateVersion by remember { mutableStateOf("") }
    var updateNotes by remember { mutableStateOf("") }
    //var updateUrl by remember { mutableStateOf<android.net.Uri?>(null) }

    LaunchedEffect(Unit) {
        try {
            val release = appDistribution.checkForNewRelease().await()
            if (release != null) {
                updateVersion = release.displayVersion ?: "Unknown Version"
                updateNotes = release.releaseNotes ?: "No release notes available"
                //updateUrl = release.downloadUri
                showUpdateDialog = true
            }
        } catch (e: Exception) {
            println("Error checking for update: $e")
        }
    }

    if (showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            title = { Text("Доступна новая версия: $updateVersion") },
            text = { Text("Что нового:\n$updateNotes") },
            confirmButton = {

            },
            dismissButton = {
                Button(onClick = { showUpdateDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    App()
}