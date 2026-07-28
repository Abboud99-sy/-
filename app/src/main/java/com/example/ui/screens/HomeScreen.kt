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
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.AudioEngine
import com.example.data.UserProgress
import com.example.ui.components.MascotBanner
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    progress: UserProgress,
    audioEngine: AudioEngine,
    onNavigateToLetters: () -> Unit,
    onNavigateToSyllables: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToTrophy: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        audioEngine.speak("مَرْحَبًا بِكُمْ فِي تَطْبِيقِ حُرُوفٍ وَكَلِمَات! هَيَّا نَتَعَلَّمْ وَنَلْعَبْ مَعًا!")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KidsBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Header & Stats
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = KidsSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "تَطْبِيقُ حُرُوفٍ وَكَلِمَات 🎈",
                            style = MaterialTheme.typography.titleLarge,
                            color = KidsPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "تَعَلُّمُ العَرَبِيَّةِ بِمُتْعَةٍ وَتَفَاعُل!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextDark.copy(alpha = 0.8f)
                        )
                    }

                    // Total Stars Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = KidsCardYellow,
                        modifier = Modifier
                            .border(2.dp, StarGold, RoundedCornerShape(20.dp))
                            .clickable { onNavigateToTrophy() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Stars",
                                tint = StarGold,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${progress.totalStars}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                        }
                    }
                }
            }
        }

        // Hero Banner Art
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .shadow(8.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner_1785232348004),
                    contentDescription = "Hero Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Mascot Greetings
        item {
            MascotBanner(
                message = "أَهْلًا يَا أَبْطَال! اخْتَارُوا قِسْمًا وَهَيَّا نَبْدَأُ الرِّحْلَةَ!",
                onMascotTap = {
                    audioEngine.speak("أَنَا سِمْسِمُ المُمَيَّزُ! هَيَّا نَتَعَلَّمِ الحُرُوفَ وَالكَلِمَات!")
                    audioEngine.playPopSound()
                }
            )
        }

        // 4 Main Feature Navigation Cards
        item {
            Text(
                text = "اخْتَر نَشَاطَكَ المُفَضَّل: ⭐",
                style = MaterialTheme.typography.titleLarge,
                color = TextDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        item {
            FeatureMenuCard(
                title = "تَعَلُّمُ الحُرُوفِ العَرَبِيَّة",
                subtitle = "28 حَرْفًا مَعَ الحَرَكَاتِ وَالأَشْكَالِ وَالأَصْوَات",
                icon = Icons.Default.MenuBook,
                backgroundColor = KidsCardBlue,
                borderColor = KidsCardBlueBorder,
                iconTint = KidsSecondary,
                onClick = {
                    audioEngine.playPopSound()
                    onNavigateToLetters()
                }
            )
        }

        item {
            FeatureMenuCard(
                title = "تَرْكِيبُ المَقَاطِعِ وَالكَلِمَات",
                subtitle = "دَمْجُ المَقَاطِعِ الصَّوْتِيَّةِ لِتَكْوِينِ كَلِمَاتٍ مُصَوَّرَة",
                icon = Icons.Default.Extension,
                backgroundColor = KidsCardOrange,
                borderColor = KidsCardOrangeBorder,
                iconTint = KidsPrimary,
                onClick = {
                    audioEngine.playPopSound()
                    onNavigateToSyllables()
                }
            )
        }

        item {
            FeatureMenuCard(
                title = "مُسَابَقَةُ الأَبْطَال",
                subtitle = "تَحَدِّيَاتٌ مُمْتِعَةٌ لِاخْتِبَارِ مَهَارَاتِك",
                icon = Icons.Default.Quiz,
                backgroundColor = KidsCardGreen,
                borderColor = KidsCardGreenBorder,
                iconTint = KidsTertiary,
                onClick = {
                    audioEngine.playPopSound()
                    onNavigateToQuiz()
                }
            )
        }

        item {
            FeatureMenuCard(
                title = "غُرْفَةُ الجَوَائِزِ وَالأَوْسِمَة",
                subtitle = "شَاهِدْ نُجُومَكَ وَأَوْسِمَتَكَ التي جَمَعْتَهَا!",
                icon = Icons.Default.EmojiEvents,
                backgroundColor = KidsCardYellow,
                borderColor = KidsCardYellowBorder,
                iconTint = StarGold,
                onClick = {
                    audioEngine.playPopSound()
                    onNavigateToTrophy()
                }
            )
        }
    }
}

@Composable
private fun FeatureMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    borderColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.5.dp, borderColor, RoundedCornerShape(24.dp))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(KidsSurface)
                    .border(2.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextDark,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDark.copy(alpha = 0.75f)
                )
            }
        }
    }
}
