package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.QuizDatabase
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.QuizHistoryRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.QuizWithQuestions
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.domain.model.completed_quiz.CompletedQuiz

class QuizReviewScreenViewModel : ViewModel() {
    private lateinit var quizHistoryRepository: QuizHistoryRepository
    var quiz by mutableStateOf<CompletedQuiz?>(null)

    fun initQuizHistoryRepository(context: Context) {
        val quizDao = QuizDatabase.getDatabase(context).quizDao()
        quizHistoryRepository = QuizHistoryRepository(quizDao)
    }

    fun loadQuiz(id: Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    quiz = quizHistoryRepository.getQuizById(id)
                } catch (e: Exception){

                }
            }
        }
    }
}