package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz

import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.QuizHistoryDao
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.QuizWithQuestions
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.entity.OptionDBEntity
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.entity.QuestionDBEntity
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.entity.QuizDBEntity
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.domain.model.completed_quiz.CompletedQuestion
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.domain.model.completed_quiz.CompletedQuiz
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.util.DateTimeUtils

class QuizHistoryRepository(private val quizHistoryDao: QuizHistoryDao) {
    suspend fun getAllQuizzes(): List<CompletedQuiz> {
        return quizHistoryDao.getAllQuizzes().map { mapDaoToModel(it) }
    }

    suspend fun getQuizById(quizId: Long): CompletedQuiz? {
        var quizWithQuestions = quizHistoryDao.getQuizById(quizId)
        return quizWithQuestions?.let {
            return mapDaoToModel(it)
        }
    }

    suspend fun addQuiz(
        name: String,
        questions: List<Pair<QuestionDBEntity, List<String>>>,
        result: Int = 0
    ) {

        val quiz = QuizDBEntity(
            name = name,
            dateTime = DateTimeUtils.currentDateTimeToLong(),
            result = result
        )

        val quizId = quizHistoryDao.insertQuiz(quiz)

        questions.forEach { (question, options) ->
            val questionWithQuizId = question.copy(quizId = quizId)
            val questionId = quizHistoryDao.insertQuestion(questionWithQuizId)

            options.forEachIndexed { index, optionText ->
                quizHistoryDao.insertOption(
                    OptionDBEntity(
                        questionId = questionId,
                        text = optionText,
                        order = index
                    )
                )
            }
        }
    }

    suspend fun deleteQuiz(quizId: Long) {
        quizHistoryDao.deleteQuiz(quizId)
    }

    private fun mapDaoToModel(quizWithQuestions: QuizWithQuestions): CompletedQuiz {
        return CompletedQuiz(
            id = quizWithQuestions.quiz.id,
            name = quizWithQuestions.quiz.name,
            dateTime = quizWithQuestions.quiz.dateTime,
            result = quizWithQuestions.quiz.result,
            questions = quizWithQuestions.questions.map {
                CompletedQuestion(
                    text = it.question.text,
                    correctAnswer = it.question.correctAnswer,
                    selectedAnswer = it.question.selectedAnswer,
                    options = it.options.map { it.text })
            }
        )

    }
}