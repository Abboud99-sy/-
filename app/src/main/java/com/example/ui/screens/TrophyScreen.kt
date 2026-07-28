package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.ui.components.TopBarKidsHeader
import com.example.ui.theme.*

@Composable
fun TrophyScreen(
    progress: UserProgress,
    audioEngine: AudioEngine,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allBadges = listOf(
        BadgeItem("بَطَل الحُرُوف", "⭐", "تَعَلَّمْتَ الحُرُوفَ العَرَبِيَّة", true),
        BadgeItem("مُكْتَشِف الكَلِمَات", "📚", "كَنَزْتَ الكَلِمَاتِ الصَّحِيحَة", true),
        BadgeItem("فَارِس المَقَاطِع", "🧩", "رَكَّبْتَ المَقَاطِعَ الصَّوْتِيَّة", progress.wordsMastered >= 3),
        BadgeItem("عَبْقَرِي اللُّغَة", "🏆", "فِزْتَ فِي مُسَابَقَةِ الأَبْطَال", progress.quizzesCompleted >= 1),
        BadgeItem("نَجْمُ الأُسْبُوع", "🌟", "حَافَظْتَ عَلَى النَّشَاطِ اليَوْمِي", progress.totalStars >= 20),
        BadgeItem("أُسْطُورَة الكَلِمَات", "👑", "أَتْمَمْتَ كُلَّ التَّحَدِّيَات", progress.wordsMastered >= 10)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KidsBackground)
    ) {
        TopBarKidsHeader(
            title = "غُرْفَةُ الجَوَائِز 🏆",
            starsCount = progress.totalStars,
            onAudioClick = {
                audioEngine.speak("غُرْفَةُ الجَوَائِز! شَاهِدْ إِنْجَازَاتِكَ وَأَوْسِمَتَكَ العَظِيمَة!")
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "النُّجُوم",
                    value = "${progress.totalStars}",
                    icon = Icons.Default.Star,
                    iconTint = StarGold,
                    bgColor = KidsCardYellow,
                    borderColor = KidsCardYellowBorder,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "الكَلِمَات",
                    value = "${progress.wordsMastered}",
                    icon = Icons.Default.Extension,
                    iconTint = KidsPrimary,
                    bgColor = KidsCardOrange,
                    borderColor = KidsCardOrangeBorder,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "المُسَابَقَات",
                    value = "${progress.quizzesCompleted}",
                    icon = Icons.Default.EmojiEvents,
                    iconTint = KidsSecondary,
                    bgColor = KidsCardBlue,
                    borderColor = KidsCardBlueBorder,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "أَوْسِمَتُكَ المُمَيَّزَة: 🏅",
                style = MaterialTheme.typography.titleLarge,
                color = TextDark,
                fontWeight = FontWeight.Bold
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allBadges) { badge ->
                    BadgeCard(
                        badge = badge,
                        onClick = {
                            if (badge.isUnlocked) {
                                audioEngine.playVictorySound()
                                audioEngine.speak("وِسَامُ ${badge.title}! ${badge.description}")
                            } else {
                                audioEngine.playPopSound()
                                audioEngine.speak("أَكْمِلِ التَّحَدِّيَاتِ لِفَتْحِ هَذَا الوِسَام!")
                            }
                        }
                    )
                }
            }
        }
    }
}

private data class BadgeItem(
    val title: String,
    val iconEmoji: String,
    val description: String,
    val isUnlocked: Boolean
)

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    bgColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier
                .border(2.dp, borderColor, RoundedCornerShape(20.dp))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun BadgeCard(
    badge: BadgeItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (badge.isUnlocked) 6.dp else 2.dp, RoundedCornerShape(22.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked) KidsSurface else KidsBackground
        )
    ) {
        Column(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = if (badge.isUnlocked) StarGold else Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (badge.isUnlocked) badge.iconEmoji else "🔒",
                fontSize = 44.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = badge.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (badge.isUnlocked) TextDark else Color.Gray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = badge.description,
                style = MaterialTheme.typography.bodyMedium,
                color = if (badge.isUnlocked) TextDark.copy(alpha = 0.75f) else Color.Gray.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                fontSize = 11.sp
            )
        }
    }
}
