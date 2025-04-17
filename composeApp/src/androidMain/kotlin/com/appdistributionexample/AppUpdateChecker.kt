package com.appdistributionexample

import android.app.AlertDialog
import android.content.Context
import com.google.firebase.appdistribution.FirebaseAppDistribution
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AppUpdateChecker(private val context: Context) {
    private val firebaseAppDistribution: FirebaseAppDistribution = Firebase.appDistribution

    suspend fun checkForUpdates() {
        try {
            if (!firebaseAppDistribution.isTesterSignedIn) {
                firebaseAppDistribution.signInTester().await()
            }

            val releaseInfo = firebaseAppDistribution.checkForNewRelease().await()

            if (releaseInfo != null) {
                showUpdateNotification()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            android.widget.Toast.makeText(
                context,
                "Ошибка при проверке обновлений: ${e.message}",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showUpdateNotification() {
        AlertDialog.Builder(context)
            .setTitle("Доступно обновление")
            .setMessage("Рекомендуем обновиться до последней версии приложения.")
            .setNegativeButton("Закрыть", null)
            .setCancelable(false)
            .show()
    }

}