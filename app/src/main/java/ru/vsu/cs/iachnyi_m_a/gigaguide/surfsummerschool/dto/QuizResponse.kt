package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.dto

data class QuizResponse(
    val response_code: Int,
    val results: List<QuestionResponse>
)