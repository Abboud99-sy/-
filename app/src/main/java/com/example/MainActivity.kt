package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.AudioEngine
import com.example.data.UserProgressRepository
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LettersScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SyllablesScreen
import com.example.ui.screens.TrophyScreen
import com.example.ui.theme.*

enum class NavScreen(val title: String, val icon: ImageVector) {
    HOME("الرَّئِيسِيَّة", Icons.Default.Home),
    LETTERS("الحُرُوف", Icons.Default.MenuBook),
    SYLLABLES("المَقَاطِع", Icons.Default.Extension),
    QUIZ("المُسَابَقَة", Icons.Default.Quiz),
    TROPHY("الجوَائِز", Icons.Default.EmojiEvents)
}

class MainActivity : ComponentActivity() {

    private lateinit var audioEngine: AudioEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        audioEngine = AudioEngine(this)

        setContent {
            ArabicKidsAppTheme {
                val userProgress by UserProgressRepository.progress.collectAsStateWithLifecycle()
                var currentNavScreen by remember { mutableStateOf(NavScreen.HOME) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        KidsNavigationBar(
                            currentScreen = currentNavScreen,
                            onScreenSelected = { screen ->
                                audioEngine.playPopSound()
                                currentNavScreen = screen
                            }
                        )
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentNavScreen) {
                            NavScreen.HOME -> HomeScreen(
                                progress = userProgress,
                                audioEngine = audioEngine,
                                onNavigateToLetters = { currentNavScreen = NavScreen.LETTERS },
                                onNavigateToSyllables = { currentNavScreen = NavScreen.SYLLABLES },
                                onNavigateToQuiz = { currentNavScreen = NavScreen.QUIZ },
                                onNavigateToTrophy = { currentNavScreen = NavScreen.TROPHY }
                            )

                            NavScreen.LETTERS -> LettersScreen(
                                starsCount = userProgress.totalStars,
                                audioEngine = audioEngine,
                                onBack = { currentNavScreen = NavScreen.HOME }
                            )

                            NavScreen.SYLLABLES -> SyllablesScreen(
                                starsCount = userProgress.totalStars,
                                audioEngine = audioEngine,
                                onBack = { currentNavScreen = NavScreen.HOME }
                            )

                            NavScreen.QUIZ -> QuizScreen(
                                starsCount = userProgress.totalStars,
                                audioEngine = audioEngine,
                                onBack = { currentNavScreen = NavScreen.HOME }
                            )

                            NavScreen.TROPHY -> TrophyScreen(
                                progress = userProgress,
                                audioEngine = audioEngine,
                                onBack = { currentNavScreen = NavScreen.HOME }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioEngine.shutdown()
    }
}

@Composable
private fun KidsNavigationBar(
    currentScreen: NavScreen,
    onScreenSelected: (NavScreen) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .shadow(12.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = KidsSurface)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        ) {
            NavScreen.entries.forEach { screen ->
                val isSelected = currentScreen == screen
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onScreenSelected(screen) },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            tint = if (isSelected) KidsPrimary else TextDark.copy(alpha = 0.6f),
                            modifier = Modifier.size(if (isSelected) 28.dp else 24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = screen.title,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            fontSize = 11.sp,
                            color = if (isSelected) KidsPrimary else TextDark.copy(alpha = 0.6f)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = KidsCardOrange
                    )
                )
            }
        }
    }
}
