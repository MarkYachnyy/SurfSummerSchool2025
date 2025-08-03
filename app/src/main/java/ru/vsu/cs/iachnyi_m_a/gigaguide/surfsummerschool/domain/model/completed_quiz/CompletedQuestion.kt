package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.domain.model.completed_quiz

data class CompletedQuestion(
    val text: String,
    val correctAnswer: String,
    val selectedAnswer: String,
    val options: List<String>
)
