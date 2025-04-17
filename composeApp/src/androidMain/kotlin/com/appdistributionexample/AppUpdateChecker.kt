package com.appdistributionexample

import android.app.AlertDialog
import android.content.Context
import com.google.firebase.appdistribution.FirebaseAppDistribution
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AppUpdateChecker(private val context: Context) {
    private val firebaseAppDistribution: FirebaseAppDistribution = Firebase.appDistribution
    private val scope = CoroutineScope(Dispatchers.Main)

    suspend fun checkForUpdates() {
        try {
            if (!firebaseAppDistribution.isTesterSignedIn) {
                firebaseAppDistribution.signInTester().await()
            }

            val releaseInfo = firebaseAppDistribution.checkForNewRelease().await()

            scope.launch {
                if (releaseInfo != null) {
                    showUpdateDialog()
                }
            }
        } catch (e: Exception) {
            scope.launch {
                showToast("Ошибка проверки обновлений: ${e.message ?: "Unknown error"}")
            }
        }
    }

    private fun showUpdateDialog() {
        AlertDialog.Builder(context)
            .setTitle("Доступно обновление")
            .setMessage("Доступна новая версия приложения")
            .setNegativeButton("Закрыть", null)
            .setCancelable(false)
            .show()
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(
            context,
            message,
            android.widget.Toast.LENGTH_LONG
        ).show()
    }
}