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
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Hexagon
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled._3dRotation
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Hexagon
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.material.icons.outlined._3dRotation
import androidx.compose.material3.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.R
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
        com.example.ui.TtsManager.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppLayout()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        com.example.ui.TtsManager.shutdown()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppLayout() {
    // Persistent global states across config changes
    var isSpanish by rememberSaveable { mutableStateOf(false) }
    var selectedTab by rememberSaveable { mutableStateOf("explorer") }
    var showAchievementsDialog by rememberSaveable { mutableStateOf(false) }
    var showExplorerHelpDialog by rememberSaveable { mutableStateOf(false) }

    if (showAchievementsDialog) {
        AchievementsDialog(
            isSpanish = isSpanish,
            onDismiss = { showAchievementsDialog = false }
        )
    }

    Scaffold(
        topBar = {
            val topBarTitle = when (selectedTab) {
                "explorer" -> if (isSpanish) "Figuras 3D" else "3D Shapes"
                "dictionary" -> if (isSpanish) "De 2D a 3D" else "Flat to Solid"
                "flashcards" -> if (isSpanish) "Tarjetas" else "Flashcards"
                "quiz" -> if (isSpanish) "Prueba Rápida" else "Exit Ticket"
                else -> "GeoKids"
            }
            val topBarSubtitle = when (selectedTab) {
                "explorer" -> if (isSpanish) "¡Toca y gira las figuras con tu dedo!" else "Touch and spin the shapes with your finger!"
                else -> null
            }

            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = topBarTitle,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = Color(0xFF1A1C1E)
                        )
                        if (topBarSubtitle != null) {
                            Text(
                                text = topBarSubtitle,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color(0xFF74777F)
                            )
                        }
                    }
                },
                actions = {
                    if (selectedTab == "explorer") {
                        IconButton(
                            onClick = { showExplorerHelpDialog = true },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("help_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.HelpOutline,
                                contentDescription = if (isSpanish) "Ayuda" else "Help",
                                tint = Color(0xFF1A1C1E),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    // Achievements Trophy Button
                    IconButton(
                        onClick = { showAchievementsDialog = true },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp)
                            .testTag("achievements_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.EmojiEvents,
                            contentDescription = if (isSpanish) "Mis Logros" else "Achievements",
                            tint = Color(0xFF1A1C1E),
                            modifier = Modifier.size(24.dp)
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
                // Tab 1: 3D Shapes
                NavigationBarItem(
                    selected = selectedTab == "explorer",
                    onClick = { selectedTab = "explorer" },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == "explorer") Icons.Filled._3dRotation else Icons.Outlined._3dRotation,
                            contentDescription = "3D Shapes"
                        )
                    },
                    label = {
                        Text(
                            text = if (isSpanish) "Figuras 3D" else "3D Shapes",
                            fontWeight = FontWeight.Medium,
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

                // Tab 2: Flat to Solid
                NavigationBarItem(
                    selected = selectedTab == "dictionary",
                    onClick = { selectedTab = "dictionary" },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == "dictionary") Icons.Filled.Hexagon else Icons.Outlined.Hexagon,
                            contentDescription = "Flat to Solid"
                        )
                    },
                    label = {
                        Text(
                            text = if (isSpanish) "De 2D a 3D" else "Flat to Solid",
                            fontWeight = FontWeight.Medium,
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
                            painter = painterResource(
                                id = if (selectedTab == "flashcards") {
                                    R.drawable.ic_cards_stack_filled
                                } else {
                                    R.drawable.ic_cards_stack_outlined
                                }
                            ),
                            contentDescription = "Flashcards"
                        )
                    },
                    label = {
                        Text(
                            text = if (isSpanish) "Tarjetas" else "Flashcards",
                            fontWeight = FontWeight.Medium,
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

                // Tab 4: Exit Ticket
                NavigationBarItem(
                    selected = selectedTab == "quiz",
                    onClick = { selectedTab = "quiz" },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == "quiz") Icons.Filled.LocalActivity else Icons.Outlined.LocalActivity,
                            contentDescription = "Exit Ticket"
                        )
                    },
                    label = {
                        Text(
                            text = if (isSpanish) "Prueba" else "Exit Ticket",
                            fontWeight = FontWeight.Medium,
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
                "explorer" -> Explorer3DScreen(
                    isSpanish = isSpanish,
                    showHelpDialog = showExplorerHelpDialog,
                    onDismissHelpDialog = { showExplorerHelpDialog = false }
                )
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
                    text = if (isSpanish) "¡Excelente!" else "Awesome!",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
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
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isSpanish) "Reiniciar" else "Reset",
                    fontWeight = FontWeight.Medium,
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
                Icon(
                    imageVector = Icons.Outlined.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFF1A1C1E),
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = if (isSpanish) "Mis Logros" else "My Achievements",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
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
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
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
                    icon = Icons.Default.Explore,
                    title = if (isSpanish) "Mente Curiosa" else "Curious Mind",
                    req = if (isSpanish) "Explora todas las figuras en el Explorador 3D." else "Explore all shapes in the 3D Explorer.",
                    desc = if (isSpanish) "¡Has observado de cerca todas las caras y vértices!" else "You have closely inspected all shapes!",
                    isUnlocked = exploredAll,
                    activeColor = Color(0xFFCD7F32)
                )

                AchievementCard(
                    icon = Icons.Default.Style,
                    title = if (isSpanish) "Sabio de Tarjetas" else "Card Wise",
                    req = if (isSpanish) "Voltea 12 tarjetas en Tarjetas Pregunta." else "Flip 12 flashcards to study.",
                    desc = if (isSpanish) "¡Leíste y aprendiste todas las preguntas secretas!" else "You read and learned all the secret questions!",
                    isUnlocked = flippedAll,
                    activeColor = Color(0xFF64748B)
                )

                AchievementCard(
                    icon = Icons.Default.Star,
                    title = if (isSpanish) "Súper Estrella" else "Super Star",
                    req = if (isSpanish) "Saca 8 de 10 o más en la Prueba Rápida." else "Score 8 out of 10 or more in the Exit Ticket.",
                    desc = if (isSpanish) "¡Demostraste un gran conocimiento geométrico!" else "You demonstrated great geometric knowledge!",
                    isUnlocked = hasHighQuiz,
                    activeColor = Color(0xFFF59E0B)
                )

                AchievementCard(
                    icon = Icons.Default.WorkspacePremium,
                    title = if (isSpanish) "Mago Geométrico" else "Geometry Wizard",
                    req = if (isSpanish) "Puntuación perfecta de 10 de 10 en la Prueba Rápida." else "Get a perfect score of 10 out of 10 in the Exit Ticket.",
                    desc = if (isSpanish) "¡Eres un experto total con visión espacial perfecta!" else "You are an expert with perfect spatial vision!",
                    isUnlocked = hasPerfectQuiz,
                    activeColor = Color(0xFF0EA5E9)
                )
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}

@Composable
fun AchievementCard(
    icon: ImageVector,
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
                        if (isUnlocked) activeColor.copy(alpha = 0.2f) else Color(0xFFF1F5F9),
                        RoundedCornerShape(27.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isUnlocked) activeColor else Color(0xFFE2E8F0),
                        shape = RoundedCornerShape(27.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isUnlocked) activeColor else Color(0xFF94A3B8),
                    modifier = Modifier.size(26.dp)
                )
                if (!isUnlocked) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF94A3B8), RoundedCornerShape(8.dp))
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isUnlocked) Color(0xFF1A1C1E) else Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isUnlocked) desc else req,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
                    color = if (isUnlocked) Color(0xFF374151) else Color(0xFF94A3B8)
                )
            }
        }
    }
}
