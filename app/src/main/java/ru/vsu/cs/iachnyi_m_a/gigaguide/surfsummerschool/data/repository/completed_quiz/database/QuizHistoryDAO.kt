package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.entity.OptionDBEntity
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.entity.QuestionDBEntity
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.entity.QuizDBEntity

@Dao
interface QuizHistoryDao {
    @Insert
    suspend fun insertQuiz(quiz: QuizDBEntity): Long

    @Insert
    suspend fun insertQuestion(question: QuestionDBEntity): Long

    @Insert
    suspend fun insertOption(option: OptionDBEntity)

    @Transaction
    @Query("SELECT * FROM quizzes")
    suspend fun getAllQuizzes(): List<QuizWithQuestions>

    @Transaction
    @Query("SELECT * FROM quizzes WHERE id = :quizId")
    suspend fun getQuizById(quizId: Long): QuizWithQuestions?

    @Query("DELETE FROM quizzes WHERE id = :quizId")
    suspend fun deleteQuiz(quizId: Long)
}

data class QuizWithQuestions(
    @Embedded val quiz: QuizDBEntity,
    @Relation(
        entity = QuestionDBEntity::class,
        parentColumn = "id",
        entityColumn = "quizId"
    )
    val questions: List<QuestionWithOptions>
)

data class QuestionWithOptions(
    @Embedded val question: QuestionDBEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val options: List<OptionDBEntity>
)