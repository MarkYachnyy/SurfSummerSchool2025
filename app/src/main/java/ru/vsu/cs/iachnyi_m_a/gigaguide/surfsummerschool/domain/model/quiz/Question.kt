package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.domain.model.quiz

data class Question(
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)