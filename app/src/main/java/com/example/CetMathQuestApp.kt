package com.example

import android.app.Application
import com.example.data.local.AppDatabase
import com.example.data.local.DataStoreManager
import com.example.data.local.DatabaseInitializer
import com.example.data.repository.AuthRepository
import com.example.data.repository.FirestoreSyncRepository
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
    lateinit var firestoreSyncRepository: FirestoreSyncRepository
        private set

    override fun onCreate() {
        super.onCreate()

        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
        } catch (_: Exception) {
        }

        database = AppDatabase.getInstance(this)
        dataStoreManager = DataStoreManager(this)
        firestoreSyncRepository = FirestoreSyncRepository(dataStoreManager)
        repository = MathRepository(database, dataStoreManager, firestoreSyncRepository)

        authRepository = AuthRepository(
            mathRepository = repository,
            dataStoreManager = dataStoreManager
        )

        // Seed initial data and restore cloud progress asynchronously if logged in
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseInitializer.populateIfEmpty(database)
            try {
                firestoreSyncRepository.fetchAndMergeCloudProgress()
            } catch (_: Exception) {}
        }
    }
}
