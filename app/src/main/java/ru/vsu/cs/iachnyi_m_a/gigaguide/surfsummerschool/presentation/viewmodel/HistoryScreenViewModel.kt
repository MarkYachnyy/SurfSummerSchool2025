package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.QuizDatabase
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.QuizHistoryRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.QuizWithQuestions
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.domain.model.completed_quiz.CompletedQuiz

class HistoryScreenViewModel : ViewModel() {
    private lateinit var quizHistoryRepository: QuizHistoryRepository
    var quizzes = mutableStateListOf<CompletedQuiz>()

    fun initQuizHistoryRepository(context: Context) {
        val quizDao = QuizDatabase.getDatabase(context).quizDao()
        quizHistoryRepository = QuizHistoryRepository(quizDao)
    }

    fun loadAllQuizzes() {
        quizzes.clear()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    var res = quizHistoryRepository.getAllQuizzes()
                    quizzes.addAll(res)
                } catch (e: Exception){

                }
            }
        }
    }

    fun deleteQuiz(id: Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    quizHistoryRepository.deleteQuiz(id)
                    loadAllQuizzes()
                } catch (e: Exception){

                }
            }
        }
    }

}