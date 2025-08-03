package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.QuizHistoryRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.QuizDatabase
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.entity.QuestionDBEntity
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.quiz.QuizRepository
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.domain.model.quiz.Question
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.domain.model.quiz.Quiz

enum class HomeScreenState {
    INITIAL,
    LOADING,
    QUIZ,
    RESULT
}

class HomeScreenViewModel() : ViewModel() {
    var state by mutableStateOf(HomeScreenState.INITIAL)
    var questions = mutableStateListOf<Question>()
    var currentQuestionIndex by mutableIntStateOf(-1)
    var currentOptionChosen by mutableIntStateOf(-1)
    var answerOptions: MutableList<List<String>> = ArrayList()
    var givenAnswers: MutableList<String> = ArrayList()
    var allTime by mutableIntStateOf(300)
    var timeLeft by mutableIntStateOf(300)
    private var timerIsOn = false

    private val quizRepository: QuizRepository = QuizRepository()
    private lateinit var quizHistoryRepository: QuizHistoryRepository

    fun initQuizHistoryRepository(context: Context) {
        val quizDao = QuizDatabase.getDatabase(context).quizDao()
        quizHistoryRepository = QuizHistoryRepository(quizDao)
    }

    fun startTimer(onTimeUp: () -> Unit) {
        timeLeft = allTime
        timerIsOn = true
        viewModelScope.launch {
            while (timerIsOn && timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            if (timeLeft == 0 && timerIsOn) {
                onTimeUp.invoke()
            }
        }
    }

    fun endQuiz() {
        timerIsOn = false
        if (givenAnswers.size < questions.size) {
            for (i in currentQuestionIndex..questions.size - 1) {
                givenAnswers.add(questions[i].incorrectAnswers.random())
            }
        }
    }

    fun setQuiz(quiz: Quiz) {
        givenAnswers.clear()
        questions.clear()
        answerOptions.clear()
        currentOptionChosen = -1
        questions.addAll(quiz.questions)
        for (question in quiz.questions) {
            var options: ArrayList<String> = ArrayList(question.incorrectAnswers)
            options.add(question.correctAnswer)
            options.shuffle()
            answerOptions.add(options)
        }
        currentQuestionIndex = 0
    }

    fun loadQuiz(onFailureMessageDisplay: () -> Unit, onSuccess: () -> Unit) {
        state = HomeScreenState.LOADING
        viewModelScope.launch {
            var quiz: Quiz? = null
            try {
                withContext(Dispatchers.IO) {
                    quiz = quizRepository.getRandomQuiz()
                }
            } catch (e: Exception) {
                quiz = null
            }
            if (quiz == null) {
                state = HomeScreenState.INITIAL
                onFailureMessageDisplay.invoke()
            } else {
                setQuiz(quiz!!)
                state = HomeScreenState.QUIZ
                onSuccess.invoke()
            }
        }
    }

    fun getCurrentQuizResult(): Int {
        return (0..givenAnswers.size - 1).map {
            if (givenAnswers[it].equals(questions[it].correctAnswer)) 1 else 0
        }.sum()
    }

    fun addCurrentQuizToHistory() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    var allQuizzes = quizHistoryRepository.getAllQuizzes()
                    var id = 1
                    if (allQuizzes.isNotEmpty()) {
                        id = allQuizzes.maxOfOrNull { it.id }!!.toInt()
                    }
                    quizHistoryRepository.addQuiz(
                        name = "Quiz $id",
                        result = getCurrentQuizResult(),
                        questions = (0..givenAnswers.size - 1).map {
                            Pair(
                                QuestionDBEntity(
                                    id = 0,
                                    quizId = 0,
                                    text = questions[it].question,
                                    correctAnswer = questions[it].correctAnswer,
                                    selectedAnswer = givenAnswers[it]
                                ),
                                answerOptions[it]
                            )
                        })
                }
            } catch (e: Exception) {

            }
        }
    }

    fun getAllQuizzes() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("db", quizHistoryRepository.getAllQuizzes().toString())
            }
        }
    }
}