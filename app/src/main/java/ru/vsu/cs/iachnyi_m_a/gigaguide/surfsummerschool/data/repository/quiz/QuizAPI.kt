package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.repository.quiz

import retrofit2.Call
import retrofit2.http.GET
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.data.dto.QuizResponse

interface QuizAPI {
    @GET("api.php?amount=5&type=multiple&difficulty=easy&encode=base64")
    fun getRandomQuiz(): Call<QuizResponse>
}