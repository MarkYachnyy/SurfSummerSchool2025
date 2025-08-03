package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.domain.model.completed_quiz

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class CompletedQuiz (
    val id: Long,
    val name: String,
    val dateTime: Long,
    val result: Int,
    val questions: List<CompletedQuestion>
)