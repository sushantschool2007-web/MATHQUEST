package com.example.data.repository

import android.util.Log
import com.example.data.local.DataStoreManager
import com.example.data.local.UserPreferences
import com.example.ui.gamemodes.LeaderboardEntry
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class CloudSyncStatus(
    val isSyncing: Boolean = false,
    val lastSyncedTime: Long? = null,
    val isConnected: Boolean = true,
    val message: String? = null
)

class FirestoreSyncRepository(
    private val dataStoreManager: DataStoreManager,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val _syncStatus = MutableStateFlow(CloudSyncStatus())
    val syncStatus: StateFlow<CloudSyncStatus> = _syncStatus.asStateFlow()

    companion object {
        private const val TAG = "FirestoreSync"
        private const val USERS_COLLECTION = "users"
    }

    /**
     * Sync local user progress to Cloud Firestore.
     * STRICT REQUIREMENT: Only sync if user is signed in with a real, valid email address.
     * Anonymous, guest, or unverified accounts are NEVER synced to the cloud leaderboard.
     */
    suspend fun syncProgressToCloud(prefs: UserPreferences) {
        val currentUser = auth.currentUser ?: return
        if (currentUser.isAnonymous) {
            Log.d(TAG, "Skipping cloud sync: Guest account")
            return
        }

        val email = currentUser.email
        if (email.isNullOrBlank() || !email.contains("@") || !email.contains(".")) {
            Log.d(TAG, "Skipping cloud sync: No proper email address")
            return
        }

        // Only verified email accounts are permitted on the cloud leaderboard
        if (!currentUser.isEmailVerified) {
            Log.d(TAG, "Skipping cloud sync: Email not yet verified ($email)")
            _syncStatus.value = CloudSyncStatus(
                isSyncing = false,
                isConnected = true,
                message = "Verify email to join live Leaderboard"
            )
            return
        }

        _syncStatus.value = _syncStatus.value.copy(isSyncing = true)
        try {
            val uid = currentUser.uid
            val name = prefs.studentName.ifBlank {
                currentUser.displayName ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
            }

            val userData = hashMapOf<String, Any>(
                "uid" to uid,
                "displayName" to name,
                "email" to email,
                "isEmailVerified" to true,
                "totalXp" to prefs.totalXp,
                "level" to prefs.currentLevel,
                "streak" to prefs.streakDays,
                "totalSolved" to prefs.totalSolved,
                "totalCorrect" to prefs.totalCorrect,
                "targetScore" to prefs.targetScore,
                "targetYear" to prefs.targetYear,
                "city" to "Maharashtra CET Aspirant",
                "updatedAt" to System.currentTimeMillis()
            )

            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .set(userData, SetOptions.merge())
                .awaitTask()

            _syncStatus.value = CloudSyncStatus(
                isSyncing = false,
                lastSyncedTime = System.currentTimeMillis(),
                isConnected = true,
                message = "Synced to Cloud"
            )
            Log.d(TAG, "Successfully synced verified student progress to Firestore for $uid ($email)")
        } catch (e: Exception) {
            Log.w(TAG, "Firestore sync skipped or error: ${e.message}")
            _syncStatus.value = _syncStatus.value.copy(
                isSyncing = false,
                isConnected = false,
                message = e.localizedMessage
            )
        }
    }

    /**
     * Fetch user progress from Cloud Firestore and restore or merge locally
     * if the cloud copy has higher XP or progress.
     */
    suspend fun fetchAndMergeCloudProgress(): Boolean {
        val currentUser = auth.currentUser ?: return false
        if (currentUser.isAnonymous) return false

        return try {
            val doc = firestore.collection(USERS_COLLECTION)
                .document(currentUser.uid)
                .get()
                .awaitTask()

            if (doc.exists()) {
                val cloudXp = (doc.getLong("totalXp") ?: 0L).toInt()
                val cloudLevel = (doc.getLong("level") ?: 1L).toInt()
                val cloudSolved = (doc.getLong("totalSolved") ?: 0L).toInt()
                val cloudCorrect = (doc.getLong("totalCorrect") ?: 0L).toInt()
                val cloudStreak = (doc.getLong("streak") ?: 0L).toInt()

                dataStoreManager.mergeCloudProgress(
                    cloudXp = cloudXp,
                    cloudLevel = cloudLevel,
                    cloudSolved = cloudSolved,
                    cloudCorrect = cloudCorrect,
                    cloudStreak = cloudStreak
                )

                _syncStatus.value = CloudSyncStatus(
                    isSyncing = false,
                    lastSyncedTime = System.currentTimeMillis(),
                    isConnected = true,
                    message = "Cloud progress restored"
                )
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.w(TAG, "Could not fetch cloud progress: ${e.message}")
            false
        }
    }

    /**
     * Fetches live leaderboard strictly from real, verified email users in Firestore.
     * NO FAKE ACCOUNTS, NO MOCK ASPIRANTS.
     * Only actual users authenticated with a valid email address will appear.
     */
    suspend fun fetchLiveLeaderboard(
        currentUserXp: Int,
        currentUserName: String,
        currentUserSolved: Int
    ): List<LeaderboardEntry> {
        val currentUser = auth.currentUser
        val currentUid = currentUser?.uid ?: ""
        val isCurrentEmailVerified = currentUser?.isEmailVerified == true && !currentUser.isAnonymous

        val entries = mutableListOf<LeaderboardEntry>()
        try {
            val snapshot = firestore.collection(USERS_COLLECTION)
                .orderBy("totalXp", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .awaitTask()

            for (doc in snapshot.documents) {
                val uid = doc.getString("uid") ?: doc.id
                val email = doc.getString("email") ?: ""
                val isVerified = doc.getBoolean("isEmailVerified") ?: false

                // Strictly filter out anonymous/fake accounts without a proper email
                if (email.isBlank() || !email.contains("@") || !email.contains(".")) {
                    continue
                }
                if (!isVerified) {
                    continue
                }

                val name = doc.getString("displayName") ?: email.substringBefore("@").replaceFirstChar { it.uppercase() }
                val city = doc.getString("city") ?: "Maharashtra Aspirant"
                val xp = (doc.getLong("totalXp") ?: 0L).toInt()
                val solved = (doc.getLong("totalSolved") ?: 0L).toInt()
                val isUser = (uid == currentUid)

                entries.add(
                    LeaderboardEntry(
                        rank = 0,
                        name = if (isUser) "$name (You)" else name,
                        city = city,
                        xp = xp,
                        solved = solved,
                        isUser = isUser
                    )
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "Live leaderboard query failed: ${e.message}")
        }

        // If the current user has verified their email and has not been synced into Firestore documents yet, include them
        if (isCurrentEmailVerified && entries.none { it.isUser }) {
            entries.add(
                LeaderboardEntry(
                    rank = 0,
                    name = "$currentUserName (You)",
                    city = "Maharashtra Aspirant",
                    xp = currentUserXp,
                    solved = currentUserSolved,
                    isUser = true
                )
            )
        }

        // Sort strictly by XP descending and calculate ranks 1, 2, 3...
        return entries
            .sortedByDescending { it.xp }
            .mapIndexed { index, entry -> entry.copy(rank = index + 1) }
    }

    private suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { cont ->
        addOnSuccessListener { cont.resume(it) }
        addOnFailureListener { cont.resumeWithException(it) }
        addOnCanceledListener { cont.cancel() }
    }
}
