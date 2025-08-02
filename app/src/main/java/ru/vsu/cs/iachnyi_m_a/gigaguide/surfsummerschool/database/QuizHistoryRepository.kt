package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.database

import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.util.DateTimeUtils

class QuizHistoryRepository(private val quizDao: QuizDao) {
    suspend fun getAllQuizzes(): List<QuizWithQuestions> {
        return quizDao.getAllQuizzes()
    }

    suspend fun getQuizById(quizId: Long): QuizWithQuestions? {
        return quizDao.getQuizById(quizId)
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

        val quizId = quizDao.insertQuiz(quiz)

        questions.forEach { (question, options) ->
            val questionWithQuizId = question.copy(quizId = quizId)
            val questionId = quizDao.insertQuestion(questionWithQuizId)

            options.forEachIndexed { index, optionText ->
                quizDao.insertOption(OptionDBEntity(
                    questionId = questionId,
                    text = optionText,
                    order = index
                ))
            }
        }
    }

    suspend fun deleteQuiz(quizId: Long) {
        quizDao.deleteQuiz(quizId)
    }
}