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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.ui.screens.DictionaryScreen
import com.example.ui.screens.Explorer3DScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.ui.AchievementManager.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppLayout()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppLayout() {
    // Persistent global states across config changes
    var isSpanish by rememberSaveable { mutableStateOf(true) }
    var selectedTab by rememberSaveable { mutableStateOf("explorer") }
    var showAchievementsDialog by rememberSaveable { mutableStateOf(false) }

    if (showAchievementsDialog) {
        AchievementsDialog(
            isSpanish = isSpanish,
            onDismiss = { showAchievementsDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "📐",
                            fontSize = 24.sp
                        )
                        Column {
                            Text(
                                text = "GeoKids 3D",
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp,
                                color = Color(0xFF1A1C1E)
                            )
                            Text(
                                text = if (isSpanish) "¡Aprende Geometría!" else "Learn Geometry!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF74777F)
                            )
                        }
                    }
                },
                actions = {
                    // Fluent Kid-Friendly Language Toggle Button
                    Button(
                        onClick = { isSpanish = !isSpanish },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .testTag("language_toggle"),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (isSpanish) "🇲🇽" else "🇺🇸",
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isSpanish) "MX" else "US",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = Color(0xFF2563EB)
                            )
                        }
                    }

                    // Achievements Trophy Button
                    IconButton(
                        onClick = { showAchievementsDialog = true },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(36.dp)
                            .testTag("achievements_button")
                    ) {
                        Text(
                            text = "🏆",
                            fontSize = 22.sp
                        )
                    }

                    // Kid-friendly age badge "8y" matching Design HTML
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(36.dp)
                            .background(Color(0xFFDBEAFE), RoundedCornerShape(18.dp))
                            .border(2.dp, Color(0xFF2563EB), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "8y",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = Color(0xFF2563EB)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.border(width = 1.dp, color = Color(0xFFE2E8F0))
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF3F4F9),
                tonalElevation = 8.dp,
                modifier = Modifier
                    .border(width = 1.dp, color = Color(0xFFE2E8F0))
                    .testTag("bottom_nav_bar")
            ) {
                // Tab 1: 3D Explorer
                NavigationBarItem(
                    selected = selectedTab == "explorer",
                    onClick = { selectedTab = "explorer" },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == "explorer") Icons.Filled.Explore else Icons.Outlined.Explore,
                            contentDescription = "Explorer"
                        )
                    },
                    label = {
                        Text(
                            text = if (isSpanish) "Explorador" else "Explorer",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF2563EB),
                        selectedTextColor = Color(0xFF2563EB),
                        indicatorColor = Color(0xFFDBEAFE),
                        unselectedIconColor = Color(0xFF74777F),
                        unselectedTextColor = Color(0xFF74777F)
                    ),
                    modifier = Modifier.testTag("tab_explorer")
                )

                // Tab 2: Dictionary
                NavigationBarItem(
                    selected = selectedTab == "dictionary",
                    onClick = { selectedTab = "dictionary" },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == "dictionary") Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                            contentDescription = "Dictionary"
                        )
                    },
                    label = {
                        Text(
                            text = if (isSpanish) "De 2D a 3D" else "Flat to Solid",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF059669),
                        selectedTextColor = Color(0xFF059669),
                        indicatorColor = Color(0xFFD1FAE5),
                        unselectedIconColor = Color(0xFF74777F),
                        unselectedTextColor = Color(0xFF74777F)
                    ),
                    modifier = Modifier.testTag("tab_dictionary")
                )

                // Tab 3: Flashcards
                NavigationBarItem(
                    selected = selectedTab == "flashcards",
                    onClick = { selectedTab = "flashcards" },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == "flashcards") Icons.Filled.Style else Icons.Outlined.Style,
                            contentDescription = "Flashcards"
                        )
                    },
                    label = {
                        Text(
                            text = if (isSpanish) "Tarjetas" else "Flashcards",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFD97706),
                        selectedTextColor = Color(0xFFD97706),
                        indicatorColor = Color(0xFFFEF3C7),
                        unselectedIconColor = Color(0xFF74777F),
                        unselectedTextColor = Color(0xFF74777F)
                    ),
                    modifier = Modifier.testTag("tab_flashcards")
                )

                // Tab 4: Quiz
                NavigationBarItem(
                    selected = selectedTab == "quiz",
                    onClick = { selectedTab = "quiz" },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == "quiz") Icons.Filled.Assignment else Icons.Outlined.Assignment,
                            contentDescription = "Quiz"
                        )
                    },
                    label = {
                        Text(
                            text = if (isSpanish) "Prueba" else "Exit Ticket",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF7C3AED),
                        selectedTextColor = Color(0xFF7C3AED),
                        indicatorColor = Color(0xFFEDE9FE),
                        unselectedIconColor = Color(0xFF74777F),
                        unselectedTextColor = Color(0xFF74777F)
                    ),
                    modifier = Modifier.testTag("tab_quiz")
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color(0xFFF7F9FC)
        ) {
            when (selectedTab) {
                "explorer" -> Explorer3DScreen(isSpanish = isSpanish)
                "dictionary" -> DictionaryScreen(isSpanish = isSpanish)
                "flashcards" -> FlashcardsScreen(isSpanish = isSpanish)
                "quiz" -> QuizScreen(isSpanish = isSpanish)
            }
        }
    }
}

@Composable
fun AchievementsDialog(
    isSpanish: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dismiss_achievements_button")
            ) {
                Text(
                    text = if (isSpanish) "¡Excelente! 🚀" else "Awesome! 🚀",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color(0xFF2563EB)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    com.example.ui.AchievementManager.resetAchievements()
                },
                modifier = Modifier.testTag("reset_achievements_button")
            ) {
                Text(
                    text = if (isSpanish) "Reiniciar 🔄" else "Reset 🔄",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFFEF4444)
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🏆", fontSize = 28.sp)
                Text(
                    text = if (isSpanish) "Mis Logros" else "My Achievements",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp
                    ),
                    color = Color(0xFF1A1C1E)
                )
            }
        },
        text = {
            val exploredAll = com.example.ui.AchievementManager.hasExploredAll()
            val flippedAll = com.example.ui.AchievementManager.hasFlippedAll()
            val hasHighQuiz = com.example.ui.AchievementManager.hasHighQuizScore()
            val hasPerfectQuiz = com.example.ui.AchievementManager.hasPerfectQuizScore()

            val unlockedCount = (if (exploredAll) 1 else 0) +
                               (if (flippedAll) 1 else 0) +
                               (if (hasHighQuiz) 1 else 0) +
                               (if (hasPerfectQuiz) 1 else 0)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isSpanish) "¡Llevas $unlockedCount de 4 insignias!" else "You have $unlockedCount of 4 badges!",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Black),
                            color = Color(0xFF2563EB)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { unlockedCount.toFloat() / 4f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp)),
                            color = Color(0xFF10B981),
                            trackColor = Color(0xFFE2E8F0),
                            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    }
                }

                AchievementCard(
                    emoji = "🧭",
                    title = if (isSpanish) "Mente Curiosa" else "Curious Mind",
                    req = if (isSpanish) "Explora todas las figuras en el Explorador 3D." else "Explore all shapes in the 3D Explorer.",
                    desc = if (isSpanish) "¡Has observado de cerca todas las caras y vértices!" else "You have closely inspected all shapes!",
                    isUnlocked = exploredAll,
                    activeColor = Color(0xFFCD7F32)
                )

                AchievementCard(
                    emoji = "📚",
                    title = if (isSpanish) "Sabio de Tarjetas" else "Card Wise",
                    req = if (isSpanish) "Voltea las 5 tarjetas en Tarjetas Pregunta." else "Flip all 5 flashcards to study.",
                    desc = if (isSpanish) "¡Leíste y aprendiste todas las preguntas secretas!" else "You read and learned all the secret questions!",
                    isUnlocked = flippedAll,
                    activeColor = Color(0xFFC0C0C0)
                )

                AchievementCard(
                    emoji = "🌟",
                    title = if (isSpanish) "Súper Estrella" else "Super Star",
                    req = if (isSpanish) "Saca 4 de 5 o más en la Prueba Rápida." else "Score 4 out of 5 or more in the Exit Ticket.",
                    desc = if (isSpanish) "¡Demostraste un gran conocimiento geométrico!" else "You demonstrated great geometric knowledge!",
                    isUnlocked = hasHighQuiz,
                    activeColor = Color(0xFFFFD700)
                )

                AchievementCard(
                    emoji = "👑",
                    title = if (isSpanish) "Mago Geométrico" else "Geometry Wizard",
                    req = if (isSpanish) "Puntuación perfecta de 5 de 5 en la Prueba Rápida." else "Get a perfect score of 5 out of 5 in the Exit Ticket.",
                    desc = if (isSpanish) "¡Eres un experto total con visión espacial perfecta!" else "You are an expert with perfect spatial vision!",
                    isUnlocked = hasPerfectQuiz,
                    activeColor = Color(0xFFB9F2FF)
                )
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Composable
fun AchievementCard(
    emoji: String,
    title: String,
    req: String,
    desc: String,
    isUnlocked: Boolean,
    activeColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) activeColor.copy(alpha = 0.08f) else Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isUnlocked) 2.dp else 1.dp,
            color = if (isUnlocked) activeColor else Color(0xFFE2E8F0)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        if (isUnlocked) activeColor.copy(alpha = 0.25f) else Color(0xFFF1F5F9),
                        RoundedCornerShape(27.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isUnlocked) activeColor else Color(0xFFE2E8F0),
                        shape = RoundedCornerShape(27.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 24.sp,
                    modifier = Modifier.graphicsLayer(alpha = if (isUnlocked) 1f else 0.35f)
                )
                if (!isUnlocked) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(Color.White, RoundedCornerShape(7.dp))
                            .border(1.dp, Color(0xFF94A3B8), RoundedCornerShape(7.dp))
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔒", fontSize = 8.sp)
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                    color = if (isUnlocked) Color(0xFF1A1C1E) else Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isUnlocked) desc else req,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = if (isUnlocked) Color(0xFF374151) else Color(0xFF94A3B8)
                )
            }
        }
    }
}
