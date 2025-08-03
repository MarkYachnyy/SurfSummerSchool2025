package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.R
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.Black
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.DarkPurple
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.Grey
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.LightGrey
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.LightPurple
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.Purple
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.SurfSummerSchoolTheme
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.White
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.view.theme.Yellow
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.viewmodel.HomeScreenState
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.presentation.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel,
    toastFunction: (String) -> Unit,
    onHistoryClickedCallback: () -> Unit
) {
    SurfSummerSchoolTheme {
        var timeUpDialogOpen by remember { mutableStateOf(false) }
        when {
            timeUpDialogOpen -> {
                Dialog(onDismissRequest = {}) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(45.dp))
                            .background(color = White)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Время вышло!", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
                        Text(modifier = Modifier.padding(vertical = 20.dp), text = "Вы не успели завершить викторину. Попробуйте еще раз!", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium)
                        Button(
                            contentPadding = PaddingValues(vertical = 10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            onClick = {
                                homeScreenViewModel.endQuiz()
                                homeScreenViewModel.addCurrentQuizToHistory()
                                homeScreenViewModel.state = HomeScreenState.INITIAL
                                timeUpDialogOpen = false
                            },
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
                }
            }
        }
        if (homeScreenViewModel.state == HomeScreenState.INITIAL) {
            InitialScreen(
                modifier = Modifier.fillMaxSize(),
                onClickStart = {
                    homeScreenViewModel.loadQuiz(
                        onFailureMessageDisplay = {
                            toastFunction.invoke(
                                "Ошибка загрузки викторины!"
                            )
                        },
                        onSuccess = {
                            homeScreenViewModel.startTimer(onTimeUp = {
                                timeUpDialogOpen = true
                            })
                        })
                },
                onClickHistory = onHistoryClickedCallback
            )
        } else if (homeScreenViewModel.state == HomeScreenState.LOADING) {
            LoadingScreen(modifier.fillMaxSize())
        } else if (homeScreenViewModel.state == HomeScreenState.QUIZ) {
            QuizScreen(
                modifier = Modifier.fillMaxSize(),
                questionIndex = homeScreenViewModel.currentQuestionIndex,
                answerOptions = homeScreenViewModel.answerOptions[homeScreenViewModel.currentQuestionIndex],
                questionCount = homeScreenViewModel.questions.size,
                chosenOptionIndex = homeScreenViewModel.currentOptionChosen,
                onOptionChosen = { homeScreenViewModel.currentOptionChosen = it },
                onQuestionAnswered = {
                    homeScreenViewModel.givenAnswers.add(homeScreenViewModel.answerOptions[homeScreenViewModel.currentQuestionIndex][homeScreenViewModel.currentOptionChosen])
                    if (homeScreenViewModel.currentQuestionIndex < homeScreenViewModel.questions.size - 1) {
                        homeScreenViewModel.currentQuestionIndex++
                        homeScreenViewModel.currentOptionChosen = -1
                    } else {
                        homeScreenViewModel.endQuiz()
                        homeScreenViewModel.state = HomeScreenState.RESULT
                    }
                },
                questionText = homeScreenViewModel.questions[homeScreenViewModel.currentQuestionIndex].question,
                allTime = homeScreenViewModel.allTime,
                timeLeft = homeScreenViewModel.timeLeft
            )
        } else {
            ResultScreen(
                modifier = Modifier.fillMaxWidth(),
                result = homeScreenViewModel.getCurrentQuizResult(),
                onClickFinish = {
                    homeScreenViewModel.addCurrentQuizToHistory()
                    homeScreenViewModel.state =
                        HomeScreenState.INITIAL
                })
        }
    }

}

@Composable
fun InitialScreen(modifier: Modifier, onClickStart: () -> Unit, onClickHistory: () -> Unit) {
    Box(modifier = modifier.background(Purple), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .clip(CircleShape)
                .clickable(onClick = onClickHistory)
                .background(White)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "История", color = Purple, style = MaterialTheme.typography.titleSmall)
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.history),
                tint = Purple,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(20.dp),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .aspectRatio(300f / 68)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                imageVector = ImageVector.vectorResource(R.drawable.logo),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clip(RoundedCornerShape(46.dp))
                    .background(color = White)
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    maxLines = 2,
                    text = "Добро пожаловать в DailyQuiz!",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Button(
                    contentPadding = PaddingValues(vertical = 10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    onClick = {
                        onClickStart.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "НАЧАТЬ ВИКТОРИНУ",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(modifier = modifier.background(Purple), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .aspectRatio(300f / 68)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                imageVector = ImageVector.vectorResource(R.drawable.logo),
                contentDescription = null
            )

            CircularProgressIndicator(
                modifier = Modifier
                    .padding(vertical = 75.dp)
                    .size(50.dp), color = White
            )

        }
    }
}

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    questionIndex: Int,
    questionCount: Int,
    questionText: String,
    answerOptions: List<String>,
    chosenOptionIndex: Int,
    onOptionChosen: (Int) -> Unit,
    onQuestionAnswered: () -> Unit,
    timeLeft: Int,
    allTime: Int
) {
    Column(
        modifier = modifier
            .background(color = Purple)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(30.dp)
                    .aspectRatio(300f / 68),
                contentDescription = null,
                imageVector = ImageVector.vectorResource(R.drawable.logo)
            )
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(46.dp))
                .background(color = White)
                .fillMaxWidth()
                .padding(20.dp)
                .weight(7f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = intToMins(timeLeft),
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkPurple
                )
                Text(
                    text = intToMins(allTime),
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkPurple
                )
            }
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                progress = { 1f * timeLeft / allTime },
                trackColor = LightGrey,
                color = DarkPurple,
                gapSize = 0.dp
            )
            Text(
                modifier = Modifier.padding(vertical = 20.dp),
                text = "Вопрос ${questionIndex + 1} из $questionCount",
                style = MaterialTheme.typography.titleMedium,
                color = LightPurple
            )
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                text = questionText,
                style = MaterialTheme.typography.titleLarge
            )


            Column(modifier = Modifier.fillMaxWidth()) {
                for (i in 0..answerOptions.size - 1) {
                    var chosen = i == chosenOptionIndex
                    Row(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .border(
                                width = 1.dp,
                                color = if (chosen) DarkPurple else LightGrey,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(onClick = { onOptionChosen.invoke(i) })
                            .background(color = if (chosen) White else LightGrey)
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (chosen) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.radio_button_checked),
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

                        Text(modifier = Modifier.padding(start = 10.dp), text = answerOptions[i])
                    }
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    contentPadding = PaddingValues(vertical = 10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = onQuestionAnswered,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (chosenOptionIndex >= 0) Purple else Grey,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = chosenOptionIndex >= 0
                ) {
                    Text(
                        text = if (questionIndex + 1 < questionCount) "ДАЛЕЕ" else "ЗАВЕРШИТЬ",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Вернуться к предыдущим вопросам нельзя",
                style = MaterialTheme.typography.bodySmall,
                color = White
            )

        }
    }
}

@Composable
fun ResultScreen(modifier: Modifier = Modifier, result: Int, onClickFinish: () -> Unit) {
    Column(
        modifier = modifier
            .background(color = Purple)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(style = MaterialTheme.typography.headlineLarge, text = "Результаты", color = White)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f)
        ) {
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
                            tint = if (i > result) Grey else Yellow,
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
                    text = "${result} из 5"
                )
                Text(
                    modifier = Modifier.padding(bottom = 20.dp),
                    text = headers[result],
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(bottom = 40.dp),
                    text = texts[result],
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
        }
    }
}

private fun intToMins(value: Int): String {
    var min = value / 60
    var sec = value % 60
    return (if (min < 10) "0" else "") + min.toString() + ":" + (if (sec < 10) "0" else "") + sec.toString()
}