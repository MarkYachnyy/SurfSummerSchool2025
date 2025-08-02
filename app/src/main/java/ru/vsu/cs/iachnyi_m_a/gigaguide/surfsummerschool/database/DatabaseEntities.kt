package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "quizzes")
data class QuizDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "date_time") val dateTime: Long,
    val result: Int
)

@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(
        entity = QuizDBEntity::class,
        parentColumns = ["id"],
        childColumns = ["quizId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class QuestionDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quizId: Long,
    val text: String,
    val correctAnswer: String,
    val selectedAnswer: String
)

@Entity(
    tableName = "options",
    foreignKeys = [ForeignKey(
        entity = QuestionDBEntity::class,
        parentColumns = ["id"],
        childColumns = ["questionId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class OptionDBEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionId: Long,
    val text: String,
    val order: Int
)