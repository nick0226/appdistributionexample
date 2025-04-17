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
            // Проверяем, вошел ли тестер в систему
            if (!firebaseAppDistribution.isTesterSignedIn) {
                // Если тестер не вошел в систему, попробуем войти
                firebaseAppDistribution.signInTester().await()
            }

            // Проверяем наличие обновления
            val releaseInfo = firebaseAppDistribution.checkForNewRelease().await()

            if (releaseInfo != null) {
                // Если есть информация о релизе, значит есть обновление
                showUpdateNotification()
            }
        } catch (e: Exception) {
            // Обработка ошибок (например, нет интернета)
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