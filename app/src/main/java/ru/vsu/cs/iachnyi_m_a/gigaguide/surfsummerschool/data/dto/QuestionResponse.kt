package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.dto

data class QuestionResponse(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)