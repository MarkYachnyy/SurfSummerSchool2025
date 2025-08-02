package ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.database.QuizWithQuestions
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.DarkPurple
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.Grey
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.Purple
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.White
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.ui.theme.Yellow
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.util.DateTimeUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.surfsummerschool.viewmodel.HistoryScreenViewModel

@Composable
fun HistoryScreen(
    modifier: Modifier,
    historyScreenViewModel: HistoryScreenViewModel,
    onQuizCardClicked: (Long) -> Unit
) {
    LaunchedEffect(Unit) {
        historyScreenViewModel.loadAllQuizzes()
    }
    Column(
        modifier = modifier
            .background(color = Purple)
            .padding(vertical = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), contentAlignment = Alignment.Center
        ) {
            Text(text = "История", style = MaterialTheme.typography.headlineMedium, color = White)
        }
        if (historyScreenViewModel.quizzes.isEmpty()) {
            EmptyListPlaceholder(
                Modifier
                    .fillMaxWidth()
                    .weight(5f)
            )
        } else {
            var dialogOpen by remember { mutableStateOf(false) }
            var quizToDeleteIndex by remember { mutableStateOf(0) }
            when {
                dialogOpen -> {
                    Dialog(onDismissRequest = { dialogOpen = false }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .clip(RoundedCornerShape(45.dp))
                                .background(color = White)
                                .padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = "Удалить викторину ${historyScreenViewModel.quizzes[quizToDeleteIndex].quiz.name} ?",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                            ) {
                                Button(
                                    contentPadding = PaddingValues(vertical = 10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 10.dp),
                                    onClick = { dialogOpen = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Purple,
                                        contentColor = White
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = "ОТМЕНА",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                Button(
                                    contentPadding = PaddingValues(vertical = 10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 10.dp),
                                    onClick = {
                                        dialogOpen = false
                                        historyScreenViewModel.deleteQuiz(historyScreenViewModel.quizzes[quizToDeleteIndex].quiz.id)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Purple,
                                        contentColor = White
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = "УДАЛИТЬ",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .weight(5f)
            ) {
                for (i in 0..historyScreenViewModel.quizzes.size - 1) {
                    var quiz = historyScreenViewModel.quizzes[i]
                    QuizCard(
                        modifier = Modifier.padding(bottom = 20.dp),
                        quiz = quiz,
                        onClick = { onQuizCardClicked.invoke(quiz.quiz.id) },
                        onLongClick = {
                            quizToDeleteIndex = i
                            dialogOpen = true
                        })
                }
            }
        }
    }
}

@Composable
private fun QuizCard(
    modifier: Modifier,
    quiz: QuizWithQuestions,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(45.dp))
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .background(color = White)
            .padding(30.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = quiz.quiz.name,
                color = DarkPurple,
                style = MaterialTheme.typography.headlineSmall
            )
            Row {
                for (i in 1..5) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(15.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.star),
                        tint = if (i > quiz.quiz.result) Grey else Yellow,
                        contentDescription = null
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = DateTimeUtils.longToDateTimeString(quiz.quiz.dateTime, "dd MMMM"),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = DateTimeUtils.longToDateTimeString(quiz.quiz.dateTime, "hh:mm"),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun EmptyListPlaceholder(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(46.dp))
                .background(color = White)
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Вы еще не проходили ни одной викторины",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Button(
                contentPadding = PaddingValues(vertical = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onClick = {},
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
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .height(80.dp)
                .aspectRatio(180f / 40)
                .padding(bottom = 40.dp)
        )
    }
}