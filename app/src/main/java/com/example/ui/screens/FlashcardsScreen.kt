package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Pentagon
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GeoMint
import com.example.ui.theme.GeoMintBorder
import com.example.ui.theme.GeoMintCard
import com.example.ui.theme.GeoMintSurface
import com.example.ui.theme.GeoNavy
import com.example.ui.theme.GeoNavyMuted
import com.example.ui.theme.GeoOrange

data class Flashcard(
    val id: Int,
    val questionEs: String,
    val questionEn: String,
    val answerEs: String,
    val answerEn: String,
    val icon: ImageVector,
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
                questionEs = "Figura 3D con 6 caras cuadradas iguales.\n\n¡Es como un dado!",
                questionEn = "3D shape with 6 equal square faces.\n\nIt's like a playing die!",
                answerEs = "El Cubo",
                answerEn = "The Cube",
                icon = Icons.Default.ViewInAr,
                color = Color(0xFFEF4444)
            ),
            Flashcard(
                id = 2,
                questionEs = "Figura 3D con 8 vértices (esquinas) y 6 caras rectangulares.\n\n¡Es como una caja de zapatos!",
                questionEn = "3D shape with 8 vertices (corners) and 6 rectangular faces.\n\nIt's like a shoebox!",
                answerEs = "El Prisma Rectangular / Paralelepípedo",
                answerEn = "The Rectangular Prism",
                icon = Icons.Default.Inventory2,
                color = Color(0xFF2563EB)
            ),
            Flashcard(
                id = 3,
                questionEs = "Tiene una base cuadrada plana abajo y sus lados terminan en una sola punta arriba.",
                questionEn = "It has a flat square base at the bottom, and all its sides meet at a single point at the top.",
                answerEs = "La Pirámide de base cuadrada",
                answerEn = "The Square-based pyramid",
                icon = Icons.Default.ChangeHistory,
                color = Color(0xFF10B981)
            ),
            Flashcard(
                id = 4,
                questionEs = "Cuerpo redondo con dos bases circulares iguales y que puede rodar.\n\n¡Como una lata de jugo!",
                questionEn = "Round solid with two equal circular bases that can roll.\n\nJust like a can of juice!",
                answerEs = "El Cilindro",
                answerEn = "The Cylinder",
                icon = Icons.Default.LocalDrink,
                color = Color(0xFFF59E0B)
            ),
            Flashcard(
                id = 5,
                questionEs = "¿Cómo se llaman las líneas formadas cuando dos caras de un cuerpo 3D se juntan?",
                questionEn = "What do we call the lines formed when two faces of a 3D solid meet?",
                answerEs = "Las Aristas",
                answerEn = "The Edges",
                icon = Icons.Default.Straighten,
                color = Color(0xFF8B5CF6)
            ),
            Flashcard(
                id = 6,
                questionEs = "Polígono de 3 lados que tiene todos sus lados y ángulos iguales.",
                questionEn = "A 3-sided polygon that has all of its sides and angles equal.",
                answerEs = "El Triángulo Equilátero",
                answerEn = "The Equilateral Triangle",
                icon = Icons.Default.ChangeHistory,
                color = Color(0xFFEC4899)
            ),
            Flashcard(
                id = 7,
                questionEs = "Triángulo que tiene exactamente dos lados iguales y uno diferente.",
                questionEn = "A triangle that has exactly two equal sides and one different side.",
                answerEs = "El Triángulo Isósceles",
                answerEn = "The Isosceles Triangle",
                icon = Icons.Default.Architecture,
                color = Color(0xFF06B6D4)
            ),
            Flashcard(
                id = 8,
                questionEs = "Triángulo con tres lados de diferentes medidas.",
                questionEn = "A triangle with three sides of completely different lengths.",
                answerEs = "El Triángulo Escaleno",
                answerEn = "The Scalene Triangle",
                icon = Icons.Default.SquareFoot,
                color = Color(0xFF14B8A6)
            ),
            Flashcard(
                id = 9,
                questionEs = "Cuadrilátero con 4 lados iguales, pero sus ángulos no son rectos.\n\n¡Parece un diamante!",
                questionEn = "A quadrilateral with 4 equal sides, but its angles are not right angles.\n\nIt looks like a diamond!",
                answerEs = "El Rombo",
                answerEn = "The Rhombus",
                icon = Icons.Default.Diamond,
                color = Color(0xFFF43F5E)
            ),
            Flashcard(
                id = 10,
                questionEs = "Cuadrilátero con un solo par de lados paralelos.",
                questionEn = "A quadrilateral with exactly one pair of parallel sides.",
                answerEs = "El Trapecio",
                answerEn = "The Trapezoid",
                icon = Icons.Default.Category,
                color = Color(0xFF84CC16)
            ),
            Flashcard(
                id = 11,
                questionEs = "Polígono regular con exactamente 5 lados y 5 ángulos iguales.",
                questionEn = "A regular polygon with exactly 5 equal sides and 5 equal angles.",
                answerEs = "El Pentágono",
                answerEn = "The Pentagon",
                icon = Icons.Default.Pentagon,
                color = Color(0xFF6366F1)
            ),
            Flashcard(
                id = 12,
                questionEs = "Cuerpo 3D con 2 bases triangulares iguales y 3 caras laterales rectangulares.\n\n¡Como una tienda de campaña!",
                questionEn = "A 3D solid with 2 identical triangular bases and 3 rectangular side faces.\n\nJust like a camping tent!",
                answerEs = "El Prisma Triangular",
                answerEn = "The Triangular Prism",
                icon = Icons.Default.Terrain,
                color = Color(0xFF10B981)
            ),
            Flashcard(
                id = 13,
                questionEs = "Cuerpo 3D con 4 caras triangulares en total, 4 vértices y 6 aristas.\n\n¡También se le llama tetraedro!",
                questionEn = "A 3D solid with 4 triangular faces in total, 4 vertices, and 6 edges.\n\nAlso known as a tetrahedron!",
                answerEs = "La Pirámide de base triangular",
                answerEn = "The Triangular Pyramid",
                icon = Icons.Default.ChangeHistory,
                color = Color(0xFFEF4444)
            )
        )
    }

    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val backgroundColor = GeoMintSurface

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
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                ),
                color = GeoNavy
            )
            Text(
                text = if (isSpanish) "¡Toca la tarjeta para ver la respuesta secreta!" else "Tap the card to reveal the secret answer!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                ),
                color = GeoNavyMuted
            )
        }

        // Card Counter Bar
        Text(
            text = "${currentIndex + 1} / ${flashcards.size}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = GeoNavy
            ),
            modifier = Modifier
                .background(GeoMint, RoundedCornerShape(12.dp))
                .border(1.dp, GeoMintBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        )

        val scope = rememberCoroutineScope()

        // The Flippable Card Container
        FlippableCard(
            isFlipped = isFlipped,
            onFlip = { 
                isFlipped = !isFlipped
                if (isFlipped) {
                    com.example.ui.AchievementManager.flipCard(card.id)
                    // Speak the answer in US English!
                    val cleanText = card.answerEn.replace(Regex("[^a-zA-Z0-9\\s()\\-]"), "").replace("\n", " ").trim()
                    com.example.ui.TtsManager.speak(cleanText)
                }
            },
            frontContent = {
                CardSideContent(
                    title = if (isSpanish) "PREGUNTA / QUESTION" else "QUESTION",
                    text = if (isSpanish) card.questionEs else card.questionEn,
                    icon = card.icon,
                    cardColor = card.color,
                    isSpanish = isSpanish,
                    isBack = false
                )
            },
            backContent = {
                var showTranslation by remember(card) { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxSize()) {
                    CardSideContent(
                        title = "ANSWER",
                        text = if (showTranslation) card.answerEs else card.answerEn,
                        icon = card.icon,
                        cardColor = card.color,
                        isSpanish = false,
                        isBack = true
                    )
                    
                    // Translation button with Material Icon
                    IconButton(
                        onClick = {
                            scope.launch {
                                showTranslation = true
                                kotlinx.coroutines.delay(2000)
                                showTranslation = false
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 12.dp, end = 12.dp)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = if (isSpanish) "Traducir" else "Translate",
                            tint = GeoOrange,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
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
                        width = 1.5.dp,
                        color = if (currentIndex > 0) GeoNavy else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("prev_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    disabledContainerColor = GeoMintCard
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous",
                    tint = if (currentIndex > 0) GeoNavy else GeoNavyMuted.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isSpanish) "Anterior" else "Back",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = if (currentIndex > 0) GeoNavy else GeoNavyMuted.copy(alpha = 0.5f)
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
                    containerColor = GeoOrange,
                    disabledContainerColor = GeoMintCard
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = if (isSpanish) "Siguiente" else "Next",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = if (currentIndex < flashcards.size - 1) Color.White else GeoNavyMuted.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next",
                    tint = if (currentIndex < flashcards.size - 1) Color.White else GeoNavyMuted.copy(alpha = 0.5f),
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
    icon: ImageVector,
    cardColor: Color,
    isSpanish: Boolean,
    isBack: Boolean
) {
    val accentColor = if (isBack) Color(0xFF059669) else cardColor

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
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                ),
                color = if (isBack) Color(0xFF047857) else cardColor.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Material Icon Badge
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Text Content
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = if (isBack) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = if (isBack) 30.sp else 20.sp,
                    lineHeight = if (isBack) 36.sp else 28.sp
                ),
                color = GeoNavy,
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
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = if (isSpanish) "¡Voltear!" else "Flip over!",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = accentColor
                )
            }
        }
    }
}
