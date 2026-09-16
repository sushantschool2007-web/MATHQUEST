package com.example

import android.app.Application
import com.example.data.local.AppDatabase
import com.example.data.local.DataStoreManager
import com.example.data.local.DatabaseInitializer
import com.example.data.repository.AuthRepository
import com.example.data.repository.MathRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CetMathQuestApp : Application() {
    lateinit var database: AppDatabase
        private set
    lateinit var dataStoreManager: DataStoreManager
        private set
    lateinit var repository: MathRepository
        private set
    lateinit var authRepository: AuthRepository
        private set

    override fun onCreate() {
        super.onCreate()
        initializeFirebaseSafely()

        database = AppDatabase.getInstance(this)
        dataStoreManager = DataStoreManager(this)
        repository = MathRepository(database, dataStoreManager)

        val validFirebaseAuth = try {
            if (FirebaseApp.getApps(this).isNotEmpty()) {
                val app = FirebaseApp.getInstance()
                val key = app.options.apiKey
                if (key.isNotBlank() && !key.contains("FakeKey", ignoreCase = true) && !key.contains("AIzaSyFake", ignoreCase = true)) {
                    FirebaseAuth.getInstance(app)
                } else {
                    null
                }
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }

        authRepository = AuthRepository(
            mathRepository = repository,
            dataStoreManager = dataStoreManager,
            firebaseAuth = validFirebaseAuth
        )

        // Seed initial data asynchronously if empty
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseInitializer.populateIfEmpty(database)
        }
    }

    private fun initializeFirebaseSafely() {
        try {
            // Google Services plugin initializes FirebaseApp if google-services.json is present.
            // We do not inject a placeholder key with "AIzaSyFakeKey..." because fake keys cause
            // Firebase Recaptcha and identity verification calls to fail with "API key not valid".
        } catch (_: Exception) {
        }
    }
}
