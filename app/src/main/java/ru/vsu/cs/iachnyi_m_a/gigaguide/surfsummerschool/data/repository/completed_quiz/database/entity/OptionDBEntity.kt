package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.completed_quiz.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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