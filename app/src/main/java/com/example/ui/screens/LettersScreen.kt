package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import com.example.audio.AudioEngine
import com.example.data.ArabicLearningData
import com.example.data.LetterModel
import com.example.ui.components.TopBarKidsHeader
import com.example.ui.theme.*

@Composable
fun LettersScreen(
    starsCount: Int,
    audioEngine: AudioEngine,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLetter by remember { mutableStateOf<LetterModel?>(null) }

    LaunchedEffect(Unit) {
        audioEngine.speak("قِسْمُ الحُرُوفِ العَرَبِيَّة! انْقُرْ عَلَى أَيِّ حَرْفٍ لِتَسْمَعَ صَوْتَهُ!")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidsBackground)
    ) {
        TopBarKidsHeader(
            title = "عَالَمُ الحُرُوف 🔤",
            starsCount = starsCount,
            onAudioClick = {
                audioEngine.speak("هَذِهِ هِيَ الحُرُوفُ العَرَبِيَّةُ الثَّمَانِيَةُ وَالعِشْرُون!")
            }
        )

        Text(
            text = "اضْغَطْ عَلَى الحَرْفِ لِتَتَعَلَّمَ نُطْقَهُ وَأَشْكَالَهُ: ⭐",
            style = MaterialTheme.typography.titleMedium,
            color = TextDark,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(ArabicLearningData.letters) { letter ->
                LetterGridTile(
                    letter = letter,
                    onClick = {
                        audioEngine.playPopSound()
                        audioEngine.speak("${letter.name} ... ${letter.fatha}")
                        selectedLetter = letter
                    }
                )
            }
        }
    }

    // Letter detail dialog
    selectedLetter?.let { letter ->
        LetterDetailDialog(
            letter = letter,
            audioEngine = audioEngine,
            onDismiss = { selectedLetter = null }
        )
    }
}

@Composable
private fun LetterGridTile(
    letter: LetterModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .shadow(6.dp, RoundedCornerShape(22.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(letter.colorHex))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, KidsPrimary.copy(alpha = 0.3f), RoundedCornerShape(22.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = letter.symbol,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    fontSize = 32.sp
                )
                Text(
                    text = letter.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun LetterDetailDialog(
    letter: LetterModel,
    audioEngine: AudioEngine,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = KidsSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "حَرْفُ (${letter.name})",
                        style = MaterialTheme.typography.titleLarge,
                        color = KidsPrimary,
                        fontWeight = FontWeight.ExtraBold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Big Letter Box
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(letter.colorHex))
                        .border(3.dp, KidsPrimary, CircleShape)
                        .clickable {
                            audioEngine.speak("${letter.name} ... ${letter.fatha}")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letter.symbol,
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 54.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextDark
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Diacritics Row (الحركات الثلاث)
                Text(
                    text = "الحَرَكَاتُ الثَّلاَث: 🔊",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DiacriticButton(
                        label = "فتحة",
                        sound = letter.fatha,
                        color = KidsCardOrange,
                        borderColor = KidsCardOrangeBorder,
                        onClick = { audioEngine.speak(letter.fatha) },
                        modifier = Modifier.weight(1f)
                    )
                    DiacriticButton(
                        label = "ضمة",
                        sound = letter.damma,
                        color = KidsCardYellow,
                        borderColor = KidsCardYellowBorder,
                        onClick = { audioEngine.speak(letter.damma) },
                        modifier = Modifier.weight(1f)
                    )
                    DiacriticButton(
                        label = "كسرة",
                        sound = letter.kasra,
                        color = KidsCardBlue,
                        borderColor = KidsCardBlueBorder,
                        onClick = { audioEngine.speak(letter.kasra) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Letter Forms (أشكال الحرف)
                Text(
                    text = "أَشْكَالُ الحَرْفِ فِي الكَلِمَة:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FormBox("أول الكلمة", letter.initialForm, Modifier.weight(1f))
                    FormBox("وسط الكلمة", letter.medialForm, Modifier.weight(1f))
                    FormBox("آخر الكلمة", letter.finalForm, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sample Word Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = KidsCardGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, KidsCardGreenBorder, RoundedCornerShape(20.dp))
                        .clickable {
                            audioEngine.speak("${letter.sampleWord} ... ${letter.sampleWordMeaning}")
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = letter.sampleWordEmoji,
                            fontSize = 36.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = letter.sampleWord,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                            Text(
                                text = letter.sampleWordMeaning,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextDark.copy(alpha = 0.75f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "استمع للكلمة",
                            tint = KidsTertiary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiacriticButton(
    label: String,
    sound: String,
    color: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = color
    ) {
        Column(
            modifier = Modifier
                .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = sound,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun FormBox(
    title: String,
    formText: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = KidsCardPurple
    ) {
        Column(
            modifier = Modifier
                .border(1.5.dp, KidsCardPurpleBorder, RoundedCornerShape(16.dp))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
        }
    }
}
