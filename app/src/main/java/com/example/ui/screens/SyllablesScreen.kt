package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.AudioEngine
import com.example.data.ArabicLearningData
import com.example.data.SyllableWordModel
import com.example.data.UserProgressRepository
import com.example.ui.components.CelebrationOverlay
import com.example.ui.components.MascotBanner
import com.example.ui.components.TactileBlock
import com.example.ui.components.TopBarKidsHeader
import com.example.ui.theme.*

@Composable
fun SyllablesScreen(
    starsCount: Int,
    audioEngine: AudioEngine,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentModel = ArabicLearningData.syllableWords[currentIndex]

    // Selected syllables in user's construction order
    val userPlacedSyllables = remember(currentIndex) { mutableStateListOf<String?>() }
    val availableSyllables = remember(currentIndex) {
        val all = (currentModel.correctSyllables + currentModel.distractorSyllables).shuffled()
        mutableStateListOf(*all.toTypedArray())
    }

    var showSuccessEffect by remember { mutableStateOf(false) }
    var isCorrectAssembly by remember { mutableStateOf<Boolean?>(null) }

    // Initialize slot size matching target correct count
    LaunchedEffect(currentIndex) {
        userPlacedSyllables.clear()
        repeat(currentModel.correctSyllables.size) {
            userPlacedSyllables.add(null)
        }
        isCorrectAssembly = null
        showSuccessEffect = false
        audioEngine.speak("رَكِّبِ المَقَاطِعَ لِتَكْوِينِ كَلِمَةِ ${currentModel.word}")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidsBackground)
    ) {
        TopBarKidsHeader(
            title = "تَرْكِيبُ المَقَاطِع 🧩",
            starsCount = starsCount,
            onAudioClick = {
                audioEngine.speak("${currentModel.word} ... ${currentModel.wordPhonetic}")
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Picture / Category Card
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
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = KidsCardPurple
                        ) {
                            Text(
                                text = "الفِئَة: ${currentModel.category}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextDark,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                audioEngine.speak(currentModel.wordPhonetic)
                            },
                            modifier = Modifier
                                .background(KidsCardBlue, CircleShape)
                                .border(1.5.dp, KidsSecondary, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "نطق المقاطع",
                                tint = KidsSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentModel.emoji,
                        fontSize = 72.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isCorrectAssembly == true) currentModel.word else "؟؟؟",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isCorrectAssembly == true) SuccessGreen else KidsPrimary
                    )
                }
            }

            // Word Slots Area (الأَمَاكِنُ الشَّاغِرَة)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = KidsCardYellow)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, KidsCardYellowBorder, RoundedCornerShape(24.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ضَعِ المَقَاطِعَ هُنَا بِالتَّرْتِيبِ الصَّحِيح:",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        userPlacedSyllables.forEachIndexed { index, syllable ->
                            Box(
                                modifier = Modifier
                                    .size(width = 68.dp, height = 68.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        if (syllable != null) KidsSurface else KidsCardYellow.copy(
                                            alpha = 0.6f
                                        )
                                    )
                                    .border(
                                        width = 2.5.dp,
                                        color = when {
                                            isCorrectAssembly == true -> SuccessGreen
                                            isCorrectAssembly == false -> BadgeRed
                                            syllable != null -> StarGold
                                            else -> KidsCardYellowBorder
                                        },
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .clickable {
                                        if (syllable != null) {
                                            // Return syllable to bank
                                            audioEngine.playPopSound()
                                            availableSyllables.add(syllable)
                                            userPlacedSyllables[index] = null
                                            isCorrectAssembly = null
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = syllable ?: "${index + 1}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (syllable != null) TextDark else TextDark.copy(alpha = 0.35f)
                                )
                            }
                        }
                    }
                }
            }

            // Syllables Bank (المَقَاطِعُ المَتَاحَة)
            Text(
                text = "اخْتَرْ المَقْطَعَ الصَّوْتِيَّ المناسب:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                items(availableSyllables) { syllable ->
                    TactileBlock(
                        text = syllable,
                        backgroundColor = KidsCardOrange,
                        borderColor = KidsCardOrangeBorder,
                        onClick = {
                            // Find first open slot
                            val firstEmptyIndex = userPlacedSyllables.indexOfFirst { it == null }
                            if (firstEmptyIndex != -1) {
                                audioEngine.playPopSound()
                                audioEngine.speak(syllable)
                                userPlacedSyllables[firstEmptyIndex] = syllable
                                availableSyllables.remove(syllable)

                                // Check if all slots filled
                                if (userPlacedSyllables.all { it != null }) {
                                    val constructed = userPlacedSyllables.joinToString("")
                                    if (userPlacedSyllables == currentModel.correctSyllables) {
                                        // Victory!
                                        isCorrectAssembly = true
                                        showSuccessEffect = true
                                        audioEngine.playVictorySound()
                                        audioEngine.speak("أَحْسَنْت! كَلِمَةُ ${currentModel.word}")
                                        UserProgressRepository.incrementWordsMastered()
                                    } else {
                                        isCorrectAssembly = false
                                        audioEngine.playWrongSound()
                                        audioEngine.speak("حَاوِلْ مَرَّةً أُخْرَى يَا بَطَل")
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Bottom Navigation Row (Previous, Reset, Next)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (currentIndex > 0) currentIndex--
                    },
                    enabled = currentIndex > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = KidsSecondary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "السابق"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("السَّابِق", fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = {
                        // Reset current
                        userPlacedSyllables.clear()
                        repeat(currentModel.correctSyllables.size) {
                            userPlacedSyllables.add(null)
                        }
                        availableSyllables.clear()
                        val all = (currentModel.correctSyllables + currentModel.distractorSyllables).shuffled()
                        availableSyllables.addAll(all)
                        isCorrectAssembly = null
                        audioEngine.playPopSound()
                    },
                    modifier = Modifier
                        .background(KidsCardPink, CircleShape)
                        .border(2.dp, KidsCardPinkBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "إعادة",
                        tint = KidsCardPinkBorder
                    )
                }

                Button(
                    onClick = {
                        if (currentIndex < ArabicLearningData.syllableWords.size - 1) {
                            currentIndex++
                        } else {
                            currentIndex = 0 // loop around
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KidsPrimary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("التَّالِي", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "التالي"
                    )
                }
            }
        }
    }

    CelebrationOverlay(
        isVisible = showSuccessEffect,
        onDismiss = { showSuccessEffect = false }
    )
}
