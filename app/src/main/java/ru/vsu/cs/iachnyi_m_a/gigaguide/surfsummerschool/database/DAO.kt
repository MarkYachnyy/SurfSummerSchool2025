package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.database

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction

@Dao
interface QuizDao {
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