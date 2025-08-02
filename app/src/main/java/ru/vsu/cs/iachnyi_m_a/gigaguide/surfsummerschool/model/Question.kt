package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.model

data class Question(
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)