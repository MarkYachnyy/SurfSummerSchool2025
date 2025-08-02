package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.repository

import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.model.Question
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.model.Quiz

interface QuizRepository {
    suspend fun getRandomQuiz(): Quiz?
}