package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.R
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.Black
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.DarkPurple
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.Green
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.Grey
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.LightGrey
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.LightPurple
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.Purple
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.Red
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.White
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.Yellow
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.viewmodel.QuizReviewScreenViewModel


@Composable
fun QuizReviewScreen(
    modifier: Modifier = Modifier,
    quizId: Long,
    quizReviewScreenViewModel: QuizReviewScreenViewModel,
    onClickFinish: () -> Unit
) {
    LaunchedEffect(Unit) {
        quizReviewScreenViewModel.loadQuiz(quizId)
    }
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .background(color = Purple)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        if (quizReviewScreenViewModel.quiz != null) {
            var quiz = quizReviewScreenViewModel.quiz!!
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    style = MaterialTheme.typography.headlineLarge,
                    text = "Результаты",
                    color = White
                )
            }

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(46.dp))
                    .background(color = White)
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (i in 1..5) {
                        Icon(
                            modifier = Modifier
                                .weight(1f)
                                .height(45.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.star),
                            tint = if (i > quiz.quiz.result) Grey else Yellow,
                            contentDescription = null
                        )
                    }
                }
                val headers = listOf<String>(
                    "Бывает и так!",
                    "Сложный вопрос?",
                    "Есть над чем поработать",
                    "Хороший результат!",
                    "Почти идеально!",
                    "Идеально!"
                )
                val texts = listOf<String>(
                    "0/5 — не отчаивайтесь. Начните заново и удивите себя!",
                    "1/5 — иногда просто не ваш день. Следующая попытка будет лучше!",
                    "2/5 — не расстраивайтесь, попробуйте ещё раз!",
                    "3/5 — вы на верном пути. Продолжайте тренироваться!",
                    "4/5 — очень близко к совершенству. Ещё один шаг!",
                    "5/5 — вы ответили на всё правильно. Это блестящий результат!"
                )
                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = Yellow,
                    text = "${quiz.quiz.result} из 5"
                )
                Text(
                    modifier = Modifier.padding(bottom = 20.dp),
                    text = headers[quiz.quiz.result],
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(bottom = 40.dp),
                    text = texts[quiz.quiz.result],
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Button(
                    contentPadding = PaddingValues(vertical = 10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = onClickFinish,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "НАЧАТЬ ЗАНОВО",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            for (i in 0..quiz.questions.size - 1) {
                var question = quiz.questions[i]
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .clip(RoundedCornerShape(46.dp))
                        .background(color = White)
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Вопрос ${i + 1} из ${quiz.questions.size}",
                            style = MaterialTheme.typography.titleMedium,
                            color = LightPurple
                        )
                        var correct =
                            question.question.correctAnswer.equals(question.question.selectedAnswer)
                        Image(
                            modifier = Modifier.size(20.dp),
                            imageVector = ImageVector.vectorResource(if (correct) R.drawable.icon_correct else R.drawable.icon_incorrect),
                            contentDescription = null
                        )
                    }

                    Text(
                        modifier = Modifier.padding(top = 20.dp),
                        textAlign = TextAlign.Center,
                        text = question.question.text,
                        style = MaterialTheme.typography.titleLarge
                    )


                    Column(modifier = Modifier.fillMaxWidth()) {
                        for (i in 0..question.options.size - 1) {
                            var chosen =
                                question.options[i].text == question.question.selectedAnswer
                            var chosenCorrect =
                                chosen && question.options[i].text == question.question.correctAnswer
                            Row(
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .border(
                                        width = 1.dp,
                                        color = if (chosen) (if (chosenCorrect) Green else Red) else LightGrey,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(color = if (chosen) White else LightGrey)
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (chosen) {
                                    Image(
                                        imageVector = ImageVector.vectorResource(if (chosenCorrect) R.drawable.icon_correct else R.drawable.icon_incorrect),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Spacer(
                                        modifier = Modifier
                                            .border(
                                                shape = CircleShape,
                                                border = BorderStroke(width = 1.dp, color = Black)
                                            )
                                            .size(20.dp)
                                    )
                                }

                                Text(
                                    modifier = Modifier.padding(start = 10.dp),
                                    text = question.options[i].text
                                )
                            }
                        }
                    }


                }

            }
            Button(
                contentPadding = PaddingValues(vertical = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                onClick = onClickFinish,
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = DarkPurple
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "НАЧАТЬ ЗАНОВО",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}