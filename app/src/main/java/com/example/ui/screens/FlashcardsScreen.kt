package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Flashcard(
    val id: Int,
    val questionEs: String,
    val questionEn: String,
    val answerEs: String,
    val answerEn: String,
    val color: Color
)

@Composable
fun FlashcardsScreen(
    isSpanish: Boolean,
    modifier: Modifier = Modifier
) {
    val flashcards = remember {
        listOf(
            Flashcard(
                id = 1,
                questionEs = "Figura 3D con 6 caras cuadradas iguales.\n\n🎲 ¡Es como un dado!",
                questionEn = "3D shape with 6 equal square faces.\n\n🎲 It's like a playing die!",
                answerEs = "🟩\n\nEl Cubo",
                answerEn = "🟩\n\nThe Cube",
                color = Color(0xFFEF4444) // Bright Coral Red
            ),
            Flashcard(
                id = 2,
                questionEs = "Figura 3D con 8 vértices (esquinas) y 6 caras rectangulares.\n\n📦 ¡Es como una caja de zapatos!",
                questionEn = "3D shape with 8 vertices (corners) and 6 rectangular faces.\n\n📦 It's like a shoebox!",
                answerEs = "📦\n\nEl Prisma Rectangular",
                answerEn = "📦\n\nThe Rectangular Prism",
                color = Color(0xFF2563EB) // Bright Blue
            ),
            Flashcard(
                id = 3,
                questionEs = "Tiene una base cuadrada plana abajo y sus lados terminan en una sola punta arriba.",
                questionEn = "It has a flat square base at the bottom, and all its sides meet at a single point at the top.",
                answerEs = "🔺\n\nLa Pirámide",
                answerEn = "🔺\n\nThe Pyramid",
                color = Color(0xFF10B981) // Green
            ),
            Flashcard(
                id = 4,
                questionEs = "Cuerpo redondo con dos bases circulares iguales y que puede rodar.\n\n🥤 ¡Como una lata de jugo!",
                questionEn = "Round solid with two equal circular bases that can roll.\n\n🥤 Just like a can of juice!",
                answerEs = "🥤\n\nEl Cilindro",
                answerEn = "🥤\n\nThe Cylinder",
                color = Color(0xFFF59E0B) // Amber Yellow
            ),
            Flashcard(
                id = 5,
                questionEs = "¿Cómo se llaman las líneas formadas cuando dos caras de un cuerpo 3D se juntan?",
                questionEn = "What do we call the lines formed when two faces of a 3D solid meet?",
                answerEs = "📏\n\nLas Aristas (o líneas de borde)",
                answerEn = "📏\n\nThe Edges (or border lines)",
                color = Color(0xFF8B5CF6) // Purple
            )
        )
    }

    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    // Solid high-contrast Slate-light background
    val backgroundColor = Color(0xFFF7F9FC)

    // Current flashcard
    val card = flashcards[currentIndex]

    // Reset rotation state when switching cards
    LaunchedEffect(currentIndex) {
        isFlipped = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Info
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = if (isSpanish) "Tarjetas Pregunta" else "Flashcards",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                ),
                color = Color(0xFF1A1C1E)
            )
            Text(
                text = if (isSpanish) "¡Toca la tarjeta para ver la respuesta secreta!" else "Tap the card to reveal the secret answer!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                ),
                color = Color(0xFF74777F)
            )
        }

        // Card Counter Bar
        Text(
            text = "${currentIndex + 1} / ${flashcards.size}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2563EB)
            ),
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        )

        // The Flippable Card Container
        FlippableCard(
            isFlipped = isFlipped,
            onFlip = { 
                isFlipped = !isFlipped
                if (isFlipped) {
                    com.example.ui.AchievementManager.flipCard(card.id)
                }
            },
            frontContent = {
                CardSideContent(
                    title = if (isSpanish) "PREGUNTA / QUESTION" else "QUESTION",
                    text = if (isSpanish) card.questionEs else card.questionEn,
                    cardColor = card.color,
                    isSpanish = isSpanish,
                    isBack = false
                )
            },
            backContent = {
                CardSideContent(
                    title = if (isSpanish) "¡RESPUESTA / ANSWER!" else "ANSWER",
                    text = if (isSpanish) card.answerEs else card.answerEn,
                    cardColor = card.color,
                    isSpanish = isSpanish,
                    isBack = true
                )
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("flashcard_box")
        )

        // Bottom Controls Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous Button
            Button(
                onClick = {
                    if (currentIndex > 0) {
                        currentIndex--
                    }
                },
                enabled = currentIndex > 0,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .border(
                        width = 1.dp,
                        color = if (currentIndex > 0) Color(0xFFE2E8F0) else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("prev_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color(0xFFF3F4F6)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous",
                    tint = if (currentIndex > 0) Color(0xFF2563EB) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isSpanish) "Anterior" else "Back",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = if (currentIndex > 0) Color(0xFF2563EB) else Color.Gray
                )
            }

            // Next Button
            Button(
                onClick = {
                    if (currentIndex < flashcards.size - 1) {
                        currentIndex++
                    }
                },
                enabled = currentIndex < flashcards.size - 1,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .testTag("next_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB),
                    disabledContainerColor = Color(0xFFF3F4F6)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = if (isSpanish) "Siguiente" else "Next",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = if (currentIndex < flashcards.size - 1) Color.White else Color.Gray
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next",
                    tint = if (currentIndex < flashcards.size - 1) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun FlippableCard(
    isFlipped: Boolean,
    onFlip: () -> Unit,
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    // Rotation value state
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    Card(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12 * density
            }
            .clickable { onFlip() },
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (rotation <= 90f) {
                // Front Side view
                frontContent()
            } else {
                // Back Side view, must flip the content horizontally to draw correctly
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        }
                ) {
                    backContent()
                }
            }
        }
    }
}

@Composable
fun CardSideContent(
    title: String,
    text: String,
    cardColor: Color,
    isSpanish: Boolean,
    isBack: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .border(
                width = 5.dp,
                color = if (isBack) Color(0xFF10B981) else cardColor,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Label / Title of side
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                ),
                color = if (isBack) Color(0xFF047857) else cardColor.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Text Content (Big and friendly)
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = if (isBack) 32.sp else 21.sp,
                    lineHeight = if (isBack) 38.sp else 28.sp
                ),
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tap prompt helper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .background(
                        color = if (isBack) Color(0xFFE6F4EA) else cardColor.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Loop,
                    contentDescription = "Girar",
                    tint = if (isBack) Color(0xFF059669) else cardColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = if (isSpanish) "¡Voltear!" else "Flip over!",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = if (isBack) Color(0xFF059669) else cardColor
                )
            }
        }
    }
}
