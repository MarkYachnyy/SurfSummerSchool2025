package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.api.QuizAPI
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.model.Question
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.model.Quiz
import java.util.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class QuizRepositoryRetrofit : QuizRepository {

    private val URL = "https://opentdb.com/"
    private val API =
        Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build()
            .create(
                QuizAPI::class.java
            )

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun getRandomQuiz(): Quiz? {
        val req = API.getRandomQuiz()
        val resp = req.execute()
        if (resp.isSuccessful && resp.body() != null) {
            return Quiz(ArrayList(resp.body()!!.results.map {
                Question(
                    decodeBase64(it.question),
                    decodeBase64(it.correct_answer),
                    it.incorrect_answers.map { decodeBase64(it) }
                )
            }))
        } else {
            return null
        }
    }
}

private fun decodeBase64(base64String: String): String {
    return String(Base64.getDecoder().decode(base64String), Charsets.UTF_8)
}