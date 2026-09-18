package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.repository.AuthRepository
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.admin.AdminDashboardViewModel
import com.example.ui.auth.AuthViewModel
import com.example.ui.auth.EmailVerificationScreen
import com.example.ui.auth.LoginScreen
import com.example.ui.auth.RegisterScreen
import com.example.ui.chapter.ChapterLearningScreen
import com.example.ui.chapter.ChapterLearningViewModel
import com.example.ui.chapter.ChapterListScreen
import com.example.ui.chapter.ChapterListViewModel
import com.example.ui.formula.FormulaBookScreen
import com.example.ui.formula.FormulaBookViewModel
import com.example.ui.gamemodes.*
import com.example.ui.home.HomeScreen
import com.example.ui.home.HomeViewModel
import com.example.ui.mistake.MistakeBookScreen
import com.example.ui.mistake.MistakeBookViewModel
import com.example.ui.mocktest.MockTestResultScreen
import com.example.ui.mocktest.MockTestScreen
import com.example.ui.mocktest.MockTestViewModel
import com.example.ui.navigation.Screen
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.onboarding.OnboardingViewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.QuestAccentGold
import com.example.ui.theme.QuestNavyDark

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as CetMathQuestApp
        val repository = app.repository
        val authRepository = app.authRepository
        val firestoreSyncRepository = app.firestoreSyncRepository

        setContent {
            MyApplicationTheme {
                QuestAppNav(
                    repository = repository,
                    authRepository = authRepository,
                    firestoreSyncRepository = firestoreSyncRepository
                )
            }
        }
    }
}

@Composable
fun QuestAppNav(
    repository: com.example.data.repository.MathRepository,
    authRepository: AuthRepository,
    firestoreSyncRepository: com.example.data.repository.FirestoreSyncRepository? = null
) {
    val prefs by repository.userPreferencesFlow.collectAsStateWithLifecycle(initialValue = null)
    val navController = rememberNavController()

    if (prefs == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(QuestNavyDark),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = QuestAccentGold)
        }
        return
    }

    val isUserLoggedIn = authRepository.isUserLoggedIn() || (prefs?.isLoggedIn == true)
    val isEmailVerified = authRepository.isEmailVerified()
    val startDestination = when {
        !isUserLoggedIn -> Screen.Login.route
        !isEmailVerified -> Screen.EmailVerification.route
        prefs?.isOnboarded == true -> Screen.Home.route
        else -> Screen.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.Login.route) {
            val authViewModel = viewModel { AuthViewModel(authRepository) }
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = { isVerified ->
                    val nextRoute = when {
                        !isVerified -> Screen.EmailVerification.route
                        prefs?.isOnboarded == true -> Screen.Home.route
                        else -> Screen.Onboarding.route
                    }
                    navController.navigate(nextRoute) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            val authViewModel = viewModel { AuthViewModel(authRepository) }
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.EmailVerification.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.EmailVerification.route) {
            val authViewModel = viewModel { AuthViewModel(authRepository) }
            EmailVerificationScreen(
                viewModel = authViewModel,
                onVerificationSuccess = {
                    val nextRoute = if (prefs?.isOnboarded == true) Screen.Home.route else Screen.Onboarding.route
                    navController.navigate(nextRoute) {
                        popUpTo(Screen.EmailVerification.route) { inclusive = true }
                    }
                },
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            val onboardingViewModel = viewModel { OnboardingViewModel(repository) }
            OnboardingScreen(
                viewModel = onboardingViewModel,
                onComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            val homeViewModel = viewModel { HomeViewModel(repository) }
            HomeScreen(
                viewModel = homeViewModel,
                onContinueLearning = { chapter ->
                    navController.navigate(Screen.ChapterLearning.createRoute(chapter))
                },
                onQuickPractice = {
                    navController.navigate(Screen.SpeedRun.route)
                },
                onChapters = {
                    navController.navigate(Screen.ChapterList.route)
                },
                onMockTest = {
                    navController.navigate(Screen.MockTest.route)
                },
                onFormulaBook = {
                    navController.navigate(Screen.FormulaBook.route)
                },
                onMistakeBook = {
                    navController.navigate(Screen.MistakeBook.route)
                },
                onDailyChallenge = {
                    navController.navigate(Screen.DailyChallenge.route)
                },
                onBossBattle = {
                    navController.navigate(Screen.BossBattle.route)
                },
                onLeaderboard = {
                    navController.navigate(Screen.Leaderboard.route)
                },
                onAchievements = {
                    navController.navigate(Screen.Achievements.route)
                },
                onAnalytics = {
                    navController.navigate(Screen.Analytics.route)
                },
                onProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onAdminDashboard = {
                    navController.navigate(Screen.AdminDashboard.route)
                }
            )
        }

        composable(Screen.ChapterList.route) {
            val chapterListViewModel = viewModel { ChapterListViewModel(repository) }
            ChapterListScreen(
                viewModel = chapterListViewModel,
                onBack = { navController.popBackStack() },
                onChapterSelect = { chapter ->
                    navController.navigate(Screen.ChapterLearning.createRoute(chapter, 0))
                },
                onJumpToConcepts = { chapter ->
                    navController.navigate(Screen.ChapterLearning.createRoute(chapter, 0))
                },
                onJumpToFormulas = { chapter ->
                    navController.navigate(Screen.ChapterLearning.createRoute(chapter, 1))
                },
                onJumpToPractice = { chapter ->
                    navController.navigate(Screen.ChapterLearning.createRoute(chapter, 3))
                }
            )
        }

        composable(
            route = Screen.ChapterLearning.route,
            arguments = listOf(
                navArgument("chapterName") { type = NavType.StringType },
                navArgument("tab") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val chapterName = backStackEntry.arguments?.getString("chapterName") ?: "Trigonometry II"
            val tab = backStackEntry.arguments?.getInt("tab") ?: 0
            val chapterViewModel = viewModel { ChapterLearningViewModel(repository, chapterName, tab) }
            ChapterLearningScreen(
                viewModel = chapterViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MockTest.route) {
            val mockTestViewModel = viewModel { MockTestViewModel(repository) }
            MockTestScreen(
                viewModel = mockTestViewModel,
                onFinishTest = { score, percentage, accuracy, correct, incorrect, unattempted, timeTaken, xiScore, xiiScore ->
                    navController.navigate(
                        Screen.MockTestResult.createRoute(
                            score, percentage, accuracy, correct, incorrect, unattempted, timeTaken, xiScore, xiiScore
                        )
                    ) {
                        popUpTo(Screen.MockTest.route) { inclusive = true }
                    }
                },
                onExit = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.MockTestResult.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("percentage") { type = NavType.FloatType },
                navArgument("accuracy") { type = NavType.FloatType },
                navArgument("correct") { type = NavType.IntType },
                navArgument("incorrect") { type = NavType.IntType },
                navArgument("unattempted") { type = NavType.IntType },
                navArgument("timeTaken") { type = NavType.IntType },
                navArgument("xiScore") { type = NavType.IntType },
                navArgument("xiiScore") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val percentage = backStackEntry.arguments?.getFloat("percentage") ?: 0f
            val accuracy = backStackEntry.arguments?.getFloat("accuracy") ?: 0f
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val incorrect = backStackEntry.arguments?.getInt("incorrect") ?: 0
            val unattempted = backStackEntry.arguments?.getInt("unattempted") ?: 0
            val timeTaken = backStackEntry.arguments?.getInt("timeTaken") ?: 0
            val xiScore = backStackEntry.arguments?.getInt("xiScore") ?: 0
            val xiiScore = backStackEntry.arguments?.getInt("xiiScore") ?: 0

            MockTestResultScreen(
                score = score,
                percentage = percentage,
                accuracy = accuracy,
                correct = correct,
                incorrect = incorrect,
                unattempted = unattempted,
                timeTaken = timeTaken,
                xiScore = xiScore,
                xiiScore = xiiScore,
                onHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SpeedRun.route) {
            SpeedRunScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.DailyChallenge.route) {
            DailyChallengeScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.BossBattle.route) {
            BossBattleScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.FormulaBook.route) {
            val formulaViewModel = viewModel { FormulaBookViewModel(repository) }
            FormulaBookScreen(
                viewModel = formulaViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MistakeBook.route) {
            val mistakeViewModel = viewModel { MistakeBookViewModel(repository) }
            MistakeBookScreen(
                viewModel = mistakeViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(
                repository = repository,
                firestoreSyncRepository = firestoreSyncRepository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Achievements.route) {
            AchievementsScreen(
                repository = repository,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            val authViewModel = viewModel { AuthViewModel(authRepository) }
            ProfileScreen(
                repository = repository,
                firestoreSyncRepository = firestoreSyncRepository,
                onBack = { navController.popBackStack() },
                onLogout = {
                    authViewModel.signOut {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onAdminDashboard = {
                    navController.navigate(Screen.AdminDashboard.route)
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            val adminViewModel = viewModel { AdminDashboardViewModel(repository) }
            AdminDashboardScreen(
                viewModel = adminViewModel,
                onSwitchToStudentView = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
