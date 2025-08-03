package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.SurfSummerSchoolTheme
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.HistoryScreen
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.HomeScreen
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.QuizReviewScreen
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.viewmodel.HistoryScreenViewModel
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.viewmodel.HomeScreenViewModel
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.viewmodel.QuizReviewScreenViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val vmProvider = ViewModelProvider(this)
        val homeScreenViewModel = vmProvider[HomeScreenViewModel::class]
        val historyScreenViewModel = vmProvider[HistoryScreenViewModel::class]
        val quizReviewScreenViewModel = vmProvider[QuizReviewScreenViewModel::class]
        homeScreenViewModel.initQuizHistoryRepository(this)
        historyScreenViewModel.initQuizHistoryRepository(this)
        quizReviewScreenViewModel.initQuizHistoryRepository(this)
        homeScreenViewModel.getAllQuizzes()
        setContent {
            val navController = rememberNavController()
            SurfSummerSchoolTheme {

                NavHost(navController = navController, startDestination = HomeScreenObject) {
                    composable<HomeScreenObject> {
                        HomeScreen(
                            homeScreenViewModel = homeScreenViewModel,
                            modifier = Modifier.fillMaxSize(),
                            toastFunction = {
                                Toast.makeText(
                                    this@MainActivity,
                                    it,
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onHistoryClickedCallback = { navController.navigate(HistoryScreenObject) }
                        )
                    }
                    composable<HistoryScreenObject> {
                        HistoryScreen(modifier = Modifier.fillMaxSize(), historyScreenViewModel, {navController.navigate(
                            QuizReviewScreenClass(it))},
                            {navController.popBackStack()})
                    }
                    composable<QuizReviewScreenClass> {
                        QuizReviewScreen(
                            modifier = Modifier.fillMaxSize(),
                            quizId = it.toRoute<QuizReviewScreenClass>().quizId,
                            onClickFinish = { navController.popBackStack()
                                            navController.popBackStack()},
                            quizReviewScreenViewModel = quizReviewScreenViewModel
                        )
                    }
                }
            }
        }
    }
}

@Serializable
object HomeScreenObject

@Serializable
object HistoryScreenObject

@Serializable
data class QuizReviewScreenClass(var quizId: Long)