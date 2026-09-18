package com.example.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object EmailVerification : Screen("email_verification")
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object ChapterList : Screen("chapter_list")
    data object ChapterLearning : Screen("chapter_learning/{chapterName}?tab={tab}") {
        fun createRoute(chapterName: String, tab: Int = 0): String = "chapter_learning/$chapterName?tab=$tab"
    }
    data object MockTest : Screen("mock_test")
    data object MockTestResult : Screen("mock_test_result/{score}/{percentage}/{accuracy}/{correct}/{incorrect}/{unattempted}/{timeTaken}/{xiScore}/{xiiScore}") {
        fun createRoute(
            score: Int, percentage: Float, accuracy: Float,
            correct: Int, incorrect: Int, unattempted: Int,
            timeTaken: Int, xiScore: Int, xiiScore: Int
        ): String = "mock_test_result/$score/$percentage/$accuracy/$correct/$incorrect/$unattempted/$timeTaken/$xiScore/$xiiScore"
    }
    data object SpeedRun : Screen("speed_run")
    data object DailyChallenge : Screen("daily_challenge")
    data object BossBattle : Screen("boss_battle")
    data object FormulaBook : Screen("formula_book")
    data object MistakeBook : Screen("mistake_book")
    data object Analytics : Screen("analytics")
    data object Leaderboard : Screen("leaderboard")
    data object Achievements : Screen("achievements")
    data object Profile : Screen("profile")
    data object AdminDashboard : Screen("admin_dashboard")
}
