package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.AudioEngine
import com.example.data.ArabicLearningData
import com.example.data.QuizQuestion
import com.example.data.UserProgressRepository
import com.example.ui.components.CelebrationOverlay
import com.example.ui.components.TopBarKidsHeader
import com.example.ui.theme.*

@Composable
fun QuizScreen(
    starsCount: Int,
    audioEngine: AudioEngine,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var questionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnsweredCorrectly by remember { mutableStateOf<Boolean?>(null) }
    var isQuizCompleted by remember { mutableStateOf(false) }
    var showCelebration by remember { mutableStateOf(false) }

    val questions = ArabicLearningData.quizQuestions
    val currentQuestion = if (questionIndex < questions.size) questions[questionIndex] else null

    LaunchedEffect(questionIndex) {
        selectedOptionIndex = null
        isAnsweredCorrectly = null
        currentQuestion?.let { q ->
            when (q) {
                is QuizQuestion.MissingLetter -> {
                    audioEngine.speak("أَكْمِلِ الكَلِمَةَ بِالحَرْفِ المُنَاسِب")
                }
                is QuizQuestion.SoundIdentification -> {
                    audioEngine.speak("اسْتَمِعْ وَاخْتَرِ الحَرْفَ الصَّحِيح: ${q.targetSound}")
                }
                is QuizQuestion.AssembleSyllables -> {}
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidsBackground)
    ) {
        TopBarKidsHeader(
            title = "مُسَابَقَةُ الأَبْطَال 🏆",
            starsCount = starsCount,
            onAudioClick = {
                audioEngine.speak("مُسَابَقَةُ الأَبْطَال! أَجِبْ عَلَى الأَسْئِلَةِ وَاجْمَعِ النُّجُوم!")
            }
        )

        if (isQuizCompleted) {
            // Quiz Finish Screen
            QuizCompletionView(
                score = score,
                totalQuestions = questions.size,
                audioEngine = audioEngine,
                onRestart = {
                    questionIndex = 0
                    score = 0
                    isQuizCompleted = false
                }
            )
        } else if (currentQuestion != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Progress Bar
                LinearProgressIndicator(
                    progress = { (questionIndex + 1).toFloat() / questions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    color = KidsPrimary,
                    trackColor = KidsCardYellow
                )

                Text(
                    text = "السُّؤَال ${questionIndex + 1} مِنْ ${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                // Question Box
                when (currentQuestion) {
                    is QuizQuestion.MissingLetter -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(28.dp)),
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(containerColor = KidsSurface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = currentQuestion.emoji,
                                    fontSize = 72.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "أَكْمِلِ الحَرْفَ المَفْقُود:",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = KidsSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = currentQuestion.word,
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = KidsPrimary
                                )
                            }
                        }

                        // Options
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            currentQuestion.options.forEachIndexed { index, option ->
                                QuizOptionTile(
                                    text = option,
                                    isSelected = selectedOptionIndex == index,
                                    isCorrect = if (selectedOptionIndex == index) isAnsweredCorrectly else null,
                                    onClick = {
                                        if (selectedOptionIndex == null) {
                                            selectedOptionIndex = index
                                            if (index == currentQuestion.correctIndex) {
                                                isAnsweredCorrectly = true
                                                score++
                                                showCelebration = true
                                                audioEngine.playVictorySound()
                                                audioEngine.speak("إِجَابَةٌ صَحِيحَةٌ! بَطَل!")
                                                UserProgressRepository.addStars(2)
                                            } else {
                                                isAnsweredCorrectly = false
                                                audioEngine.playWrongSound()
                                                audioEngine.speak("حَاوِلْ فِي السُّؤَالِ القَادِم")
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    is QuizQuestion.SoundIdentification -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(28.dp)),
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(containerColor = KidsSurface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "اسْتَمِعْ إِلَى الصَّوْتِ وَاخْتَرِ الحَرْف:",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextDark,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        audioEngine.speak(currentQuestion.targetSound)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = KidsSecondary),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "تشغيل الصوت"
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("اسْتَمِعْ لِلصَّوْت 🔊", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Options Grid
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            currentQuestion.options.forEachIndexed { index, option ->
                                QuizOptionTile(
                                    text = option,
                                    isSelected = selectedOptionIndex == index,
                                    isCorrect = if (selectedOptionIndex == index) isAnsweredCorrectly else null,
                                    onClick = {
                                        if (selectedOptionIndex == null) {
                                            selectedOptionIndex = index
                                            if (index == currentQuestion.correctIndex) {
                                                isAnsweredCorrectly = true
                                                score++
                                                showCelebration = true
                                                audioEngine.playVictorySound()
                                                audioEngine.speak("أَحْسَنْت! إِجَابَةٌ صَحِيحَة!")
                                                UserProgressRepository.addStars(2)
                                            } else {
                                                isAnsweredCorrectly = false
                                                audioEngine.playWrongSound()
                                                audioEngine.speak("إِجَابَةٌ غَيْرُ صَحِيحَة")
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    else -> {}
                }

                // Next Question Button
                Button(
                    onClick = {
                        if (questionIndex < questions.size - 1) {
                            questionIndex++
                        } else {
                            isQuizCompleted = true
                            UserProgressRepository.incrementQuizzesCompleted()
                        }
                    },
                    enabled = selectedOptionIndex != null,
                    colors = ButtonDefaults.buttonColors(containerColor = KidsPrimary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(54.dp)
                ) {
                    Text(
                        text = if (questionIndex < questions.size - 1) "السُّؤَالُ التَّالِي ➔" else "إِنْهَاءُ المُسَابَقَة 🎉",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    CelebrationOverlay(
        isVisible = showCelebration,
        onDismiss = { showCelebration = false }
    )
}

@Composable
private fun QuizOptionTile(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean?,
    onClick: () -> Unit
) {
    val bgColor = when {
        isSelected && isCorrect == true -> KidsCardGreen
        isSelected && isCorrect == false -> KidsCardPink
        else -> KidsSurface
    }

    val borderColor = when {
        isSelected && isCorrect == true -> SuccessGreen
        isSelected && isCorrect == false -> BadgeRed
        else -> KidsCardBlueBorder
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.5.dp, borderColor, RoundedCornerShape(20.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun QuizCompletionView(
    score: Int,
    totalQuestions: Int,
    audioEngine: AudioEngine,
    onRestart: () -> Unit
) {
    LaunchedEffect(Unit) {
        audioEngine.playVictorySound()
        audioEngine.speak("مَبْرُوك! لَقَدْ أَنْمَيْتَ المًسَابَقَةَ بِنَجَاح!")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.size(160.dp),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_quiz_trophy_1785232380634),
                contentDescription = "الكأس",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "🏆 أَلْفُ مَبْرُوك يَا بَطَل! 🏆",
            style = MaterialTheme.typography.displayMedium,
            color = KidsPrimary,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "نَتِيجَتُكَ: $score مِنْ $totalQuestions",
            style = MaterialTheme.typography.titleLarge,
            color = TextDark,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = KidsSecondary),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "إعادة"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("اللَّعِبُ مَرَّةً أُخْرَى 🔄", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}
