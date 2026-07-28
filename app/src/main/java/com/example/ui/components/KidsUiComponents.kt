package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import kotlin.random.Random

@Composable
fun TopBarKidsHeader(
    title: String,
    starsCount: Int,
    onAudioClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = KidsSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Right side: Stars pill
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = KidsCardYellow,
                modifier = Modifier.border(2.dp, StarGold, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stars",
                        tint = StarGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$starsCount",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextDark,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = KidsPrimary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            // Audio button optional
            if (onAudioClick != null) {
                IconButton(
                    onClick = onAudioClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(KidsCardBlue, CircleShape)
                        .border(1.5.dp, KidsSecondary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speak",
                        tint = KidsSecondary
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(40.dp))
            }
        }
    }
}

@Composable
fun MascotBanner(
    message: String,
    onMascotTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot_bounce")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = KidsCardBlue)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text speech bubble
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = KidsSurface),
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, KidsCardBlueBorder, RoundedCornerShape(20.dp))
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(14.dp),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Mascot Image
            Box(
                modifier = Modifier
                    .offset(y = bounceOffset.dp)
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(KidsCardYellow)
                    .border(3.dp, StarGold, CircleShape)
                    .clickable { onMascotTap() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_mascot_1785232364951),
                    contentDescription = "سمسم المساعد",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun TactileBlock(
    text: String,
    backgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "blockScale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .height(68.dp)
            .shadow(if (isSelected) 10.dp else 6.dp, RoundedCornerShape(20.dp))
            .clickable {
                isPressed = true
                onClick()
                isPressed = false
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) KidsCardYellow else backgroundColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = if (isSelected) 3.5.dp else 2.5.dp,
                    color = if (isSelected) StarGold else borderColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CelebrationOverlay(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2200)
        onDismiss()
    }

    val particles = remember {
        List(25) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                color = listOf(KidsPrimary, KidsSecondary, StarGold, KidsTertiary, KidsCardPinkBorder).random(),
                radius = Random.nextFloat() * 18f + 10f
            )
        }
    }

    val animProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1800, easing = LinearEasing),
        label = "celebration"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            particles.forEach { p ->
                val currentY = (p.y + animProgress * 0.8f) % 1.0f
                drawCircle(
                    color = p.color,
                    radius = p.radius,
                    center = Offset(p.x * width, currentY * height)
                )
            }
        }

        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = KidsSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉 أحْسَنْتَ يا بَطَل! 🎉",
                    style = MaterialTheme.typography.displayMedium,
                    color = KidsPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "إِجَابَةٌ صَحِيحَةٌ وُمَمْتَازَة! ⭐⭐⭐",
                    style = MaterialTheme.typography.titleLarge,
                    color = SuccessGreen,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val color: Color,
    val radius: Float
)
