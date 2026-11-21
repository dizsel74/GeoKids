package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FeedbackManager
import com.example.ui.theme.GeoMint
import com.example.ui.theme.GeoMintBorder
import com.example.ui.theme.GeoMintCard
import com.example.ui.theme.GeoMintSurface
import com.example.ui.theme.GeoNavy
import com.example.ui.theme.GeoNavyMuted
import com.example.ui.theme.GeoOrange

data class QuizQuestion(
    val id: Int,
    val textEs: String,
    val textEn: String,
    val optionsEs: List<String>,
    val optionsEn: List<String>,
    val correctIndex: Int,
    val explanationEs: String,
    val explanationEn: String
)

@Composable
fun QuizScreen(
    isSpanish: Boolean,
    modifier: Modifier = Modifier
) {
    val questions = remember {
        listOf(
            QuizQuestion(
                id = 1,
                textEs = "¿Cuántos vértices (esquinas) tiene un cubo?",
                textEn = "How many vertices (corners) does a cube have?",
                optionsEs = listOf("4 vértices", "6 vértices", "8 vértices"),
                optionsEn = listOf("4 vertices", "6 vertices", "8 vertices"),
                correctIndex = 2,
                explanationEs = "¡Un cubo tiene 8 esquinas en total (4 arriba y 4 abajo)!",
                explanationEn = "A cube has 8 corners in total (4 on top and 4 at the bottom)!"
            ),
            QuizQuestion(
                id = 2,
                textEs = "¿Cuál es la figura plana (2D) que se estira para formar un cilindro?",
                textEn = "Which flat shape (2D) is stretched to make a cylinder?",
                optionsEs = listOf("Un círculo", "Un cuadrado", "Un triángulo"),
                optionsEn = listOf("A circle", "A square", "A triangle"),
                correctIndex = 0,
                explanationEs = "¡Un círculo plano al tomar volumen se convierte en cilindro redondo!",
                explanationEn = "A flat circle gains volume to become a round cylinder!"
            ),
            QuizQuestion(
                id = 3,
                textEs = "¿Cuántas caras (superficies planas) tiene un prisma rectangular?",
                textEn = "How many faces (flat surfaces) does a rectangular prism have?",
                optionsEs = listOf("4 caras", "6 caras", "8 caras"),
                optionsEn = listOf("4 faces", "6 faces", "8 faces"),
                correctIndex = 1,
                explanationEs = "¡Tiene 6 caras, igual que una caja de zapatos o un cubo largo!",
                explanationEn = "It has 6 faces, just like a shoe box or a long cube!"
            ),
            QuizQuestion(
                id = 4,
                textEs = "Si juntas 1 base cuadrada abajo y 4 caras triangulares en los lados, ¿qué figura construyes?",
                textEn = "If you join 1 square base at the bottom and 4 triangular faces on the sides, what shape do you build?",
                optionsEs = listOf("Un cubo", "Una pirámide", "Un prisma rectangular"),
                optionsEn = listOf("A cube", "A pyramid", "A rectangular prism"),
                correctIndex = 1,
                explanationEs = "¡Muy bien! Se forma una pirámide de base cuadrada que termina en punta arriba.",
                explanationEn = "Correct! It forms a square-based pyramid that points up at the top."
            ),
            QuizQuestion(
                id = 5,
                textEs = "¿Qué es una arista en un cuerpo geométrico?",
                textEn = "What is an edge in a 3D geometric solid?",
                optionsEs = listOf(
                    "Una de las superficies planas",
                    "La esquina donde se juntan los puntos",
                    "La línea donde se unen dos caras"
                ),
                optionsEn = listOf(
                    "One of the flat surfaces",
                    "The corner where points meet",
                    "The line where two faces meet"
                ),
                correctIndex = 2,
                explanationEs = "¡Una arista es la línea de borde donde se conectan dos caras!",
                explanationEn = "An edge is the border line where two faces connect!"
            ),
            QuizQuestion(
                id = 6,
                textEs = "¿Cuántas caras tiene un prisma triangular en total?",
                textEn = "How many faces does a triangular prism have in total?",
                optionsEs = listOf("3 caras", "5 caras", "6 caras"),
                optionsEn = listOf("3 faces", "5 faces", "6 faces"),
                correctIndex = 1,
                explanationEs = "¡Tiene 5 caras en total (2 bases triangulares y 3 caras rectangulares a los lados)!",
                explanationEn = "It has 5 faces in total (2 triangular bases and 3 rectangular side faces)!"
            ),
            QuizQuestion(
                id = 7,
                textEs = "¿Cuántos vértices tiene una pirámide de base triangular (tetraedro)?",
                textEn = "How many vertices does a triangular pyramid (tetrahedron) have?",
                optionsEs = listOf("4 vértices", "5 vértices", "6 vértices"),
                optionsEn = listOf("4 vertices", "5 vertices", "6 vertices"),
                correctIndex = 0,
                explanationEs = "¡Tiene 4 vértices en total (3 en la base triangular y 1 arriba en la punta)!",
                explanationEn = "It has 4 vertices in total (3 at the triangular base and 1 at the top apex)!"
            ),
            QuizQuestion(
                id = 8,
                textEs = "¿Cómo se le llama a un triángulo con exactamente dos lados iguales y uno diferente?",
                textEn = "What is a triangle with exactly two equal sides and one different side called?",
                optionsEs = listOf("Equilátero", "Isósceles", "Escaleno"),
                optionsEn = listOf("Equilateral", "Isosceles", "Scalene"),
                correctIndex = 1,
                explanationEs = "¡Un triángulo con dos lados iguales es un triángulo isósceles!",
                explanationEn = "A triangle with two equal sides is an isosceles triangle!"
            ),
            QuizQuestion(
                id = 9,
                textEs = "¿Cómo se le llama al polígono regular que tiene exactamente 5 lados iguales?",
                textEn = "What is a regular polygon with exactly 5 equal sides called?",
                optionsEs = listOf("Pentágono", "Hexágono", "Octágono"),
                optionsEn = listOf("Pentagon", "Hexagon", "Octagon"),
                correctIndex = 0,
                explanationEs = "¡Un polígono de 5 lados se llama pentágono!",
                explanationEn = "A 5-sided polygon is called a pentagon!"
            ),
            QuizQuestion(
                id = 10,
                textEs = "¿Cuál cuadrilátero tiene 4 lados iguales pero sus ángulos interiores no son rectos (diamante)?",
                textEn = "Which quadrilateral has 4 equal sides but its interior angles are not right angles (diamond shape)?",
                optionsEs = listOf("Rectángulo", "Rombo", "Trapecio"),
                optionsEn = listOf("Rectangle", "Rhombus", "Trapezoid"),
                correctIndex = 1,
                explanationEs = "¡Es el rombo! Tiene todos sus lados de la misma medida, como un diamante.",
                explanationEn = "It's the rhombus! It has equal sides, like a diamond."
            )
        )
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var quizCompleted by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val backgroundColor = GeoMintSurface

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!quizCompleted) {
            val question = questions[currentQuestionIndex]

            // Question Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isSpanish) "Prueba Rápida" else "Exit Ticket",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    ),
                    color = GeoNavy
                )

                Text(
                    text = "${currentQuestionIndex + 1} / ${questions.size}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = GeoNavy,
                    modifier = Modifier
                        .background(GeoMint, RoundedCornerShape(12.dp))
                        .border(1.dp, GeoMintBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = { (currentQuestionIndex + 1).toFloat() / questions.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .border(1.dp, GeoMintBorder, RoundedCornerShape(4.dp)),
                color = GeoOrange,
                trackColor = GeoMintCard,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )

            // Question Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GeoMintBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isSpanish) "Pregunta ${currentQuestionIndex + 1}" else "Question ${currentQuestionIndex + 1}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.5.sp
                        ),
                        color = GeoOrange
                    )

                    Text(
                        text = if (isSpanish) question.textEs else question.textEn,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 19.sp,
                            lineHeight = 25.sp
                        ),
                        color = GeoNavy,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Options List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val options = if (isSpanish) question.optionsEs else question.optionsEn
                options.forEachIndexed { index, optionText ->
                    val isSelected = selectedAnswerIndex == index
                    val isCorrect = index == question.correctIndex

                    val cardContainerColor = when {
                        isAnswerSubmitted && isCorrect -> Color(0xFFECFDF5) // Correct highlight (green)
                        isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFFEF2F2) // Wrong highlight (red)
                        isSelected -> GeoMintCard // Selected highlight (soft mint)
                        else -> Color.White
                    }

                    val cardBorderColor = when {
                        isAnswerSubmitted && isCorrect -> Color(0xFF10B981) // Green border
                        isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFEF4444) // Red border
                        isSelected -> GeoNavy // Navy border
                        else -> GeoMintBorder // Mint border
                    }

                    val textColor = when {
                        isAnswerSubmitted && isCorrect -> Color(0xFF047857)
                        isAnswerSubmitted && isSelected && !isCorrect -> Color(0xFFB91C1C)
                        isSelected -> GeoNavy
                        else -> GeoNavy
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isAnswerSubmitted) {
                                selectedAnswerIndex = index
                            }
                            .border(
                                width = if (isSelected || (isAnswerSubmitted && isCorrect)) 2.dp else 1.dp,
                                color = cardBorderColor,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .testTag("quiz_option_$index"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardContainerColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 17.sp
                                ),
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )

                            // Status Icons
                            if (isAnswerSubmitted) {
                                if (isCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = Color(0xFF10B981)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Incorrect",
                                        tint = Color(0xFFEF4444)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom explanation or Action Button
            AnimatedVisibility(
                visible = isAnswerSubmitted,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, GeoMint, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GeoMintCard)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val isUserCorrect = selectedAnswerIndex == question.correctIndex
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isUserCorrect) Icons.Default.CheckCircle else Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = if (isUserCorrect) Color(0xFF047857) else GeoOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isUserCorrect) {
                                    if (isSpanish) "¡Excelente!" else "Excellent!"
                                } else {
                                    if (isSpanish) "Recuerda:" else "Tip:"
                                },
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = if (isUserCorrect) Color(0xFF047857) else GeoOrange
                            )
                        }
                        Text(
                            text = if (isSpanish) question.explanationEs else question.explanationEn,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                            color = GeoNavy
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Confirm / Next button
            val view = LocalView.current
            Button(
                onClick = {
                    if (!isAnswerSubmitted) {
                        // Submit answer
                        if (selectedAnswerIndex != null) {
                            if (selectedAnswerIndex == question.correctIndex) {
                                score++
                            }
                            isAnswerSubmitted = true
                        }
                    } else {
                        // Go to next question or complete
                        FeedbackManager.playClick(view)
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            selectedAnswerIndex = null
                            isAnswerSubmitted = false
                        } else {
                            quizCompleted = true
                            com.example.ui.AchievementManager.submitQuizScore(score)
                        }
                    }
                },
                enabled = selectedAnswerIndex != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("quiz_action_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GeoOrange,
                    disabledContainerColor = GeoMintCard
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                val actionText = if (!isAnswerSubmitted) {
                    if (isSpanish) "Comprobar Respuesta" else "Check Answer"
                } else if (currentQuestionIndex < questions.size - 1) {
                    if (isSpanish) "Siguiente Pregunta" else "Next Question"
                } else {
                    if (isSpanish) "Ver Resultados" else "View Results"
                }
                val actionTint = if (selectedAnswerIndex != null) Color.White else GeoNavyMuted
                Text(
                    text = actionText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = actionTint
                )
                if (isAnswerSubmitted) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (currentQuestionIndex < questions.size - 1) {
                            Icons.AutoMirrored.Filled.ArrowForward
                        } else {
                            Icons.Outlined.EmojiEvents
                        },
                        contentDescription = null,
                        tint = actionTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

        } else {
            // RESULTS SCREEN
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .border(1.dp, GeoMintBorder, RoundedCornerShape(24.dp))
                    .testTag("results_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Star decoration drawing
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Celebrate drawing background circles
                            drawCircle(
                                color = GeoMint,
                                radius = size.minDimension / 2
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = GeoOrange,
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    // Score Title
                    Text(
                        text = if (isSpanish) "¡Gran trabajo!" else "Great job!",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 30.sp
                        ),
                        color = GeoNavy,
                        textAlign = TextAlign.Center
                    )

                    // Numerical fraction
                    Text(
                        text = "$score / ${questions.size}",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 54.sp
                        ),
                        color = GeoOrange,
                        textAlign = TextAlign.Center
                    )

                    // Fun response message
                    Text(
                        text = when {
                            score == 10 -> if (isSpanish) "¡Eres un pro de la geometría! ¡Te sacaste un diez!" else "You are a geometry expert! Excellent!"
                            score >= 8 -> if (isSpanish) "¡Casi perfecto! ¡Vas con todo, súper bien!" else "Almost perfect! Keep going, you're doing great!"
                            score >= 6 -> if (isSpanish) "¡Súper bien hecho! ¡Aprendiste un montón hoy!" else "Well done! You've learned a lot today!"
                            else -> if (isSpanish) "¡Sigue practicando! ¡A la próxima te sale de diez!" else "Keep practicing! With practice you will be the best!"
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        ),
                        color = GeoNavy,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Session Reward Badges
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.EmojiEvents,
                            contentDescription = null,
                            tint = GeoOrange,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = if (isSpanish) "Tus Insignias de la Sesión" else "Your Session Badges",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = GeoNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Badge 1: 3D Explorer
                        val isExp = com.example.ui.AchievementManager.hasExploredAll()
                        ResultsBadgeItem(
                            icon = Icons.Default.Explore,
                            title = if (isSpanish) "Explorador" else "Explorer",
                            isUnlocked = isExp,
                            color = Color(0xFFCD7F32) // Bronze
                        )

                        // Badge 2: Card Wise
                        val isFlip = com.example.ui.AchievementManager.hasFlippedAll()
                        ResultsBadgeItem(
                            icon = Icons.Default.Style,
                            title = if (isSpanish) "Sabio" else "Card Wise",
                            isUnlocked = isFlip,
                            color = Color(0xFF64748B) // Silver
                        )

                        // Badge 3: Super Star (on this quiz attempt)
                        val isStar = score >= 8
                        ResultsBadgeItem(
                            icon = Icons.Default.Star,
                            title = if (isSpanish) "Estrella" else "Super Star",
                            isUnlocked = isStar,
                            color = Color(0xFFF59E0B) // Gold
                        )

                        // Badge 4: Perfect score
                        val isPerfect = score == 10
                        ResultsBadgeItem(
                            icon = Icons.Default.WorkspacePremium,
                            title = if (isSpanish) "Mago" else "Wizard",
                            isUnlocked = isPerfect,
                            color = Color(0xFF0EA5E9) // Diamond Blue
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Restart button
                    Button(
                        onClick = {
                            currentQuestionIndex = 0
                            selectedAnswerIndex = null
                            isAnswerSubmitted = false
                            score = 0
                            quizCompleted = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("restart_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GeoOrange,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reiniciar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSpanish) "Volver a Intentar" else "Try Again",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultsBadgeItem(
    icon: ImageVector,
    title: String,
    isUnlocked: Boolean,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    if (isUnlocked) color.copy(alpha = 0.18f) else GeoMintSurface,
                    RoundedCornerShape(30.dp)
                )
                .border(
                    width = if (isUnlocked) 2.dp else 1.dp,
                    color = if (isUnlocked) color else GeoMintBorder,
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isUnlocked) color else GeoNavyMuted.copy(alpha = 0.6f),
                modifier = Modifier.size(28.dp)
            )
            if (!isUnlocked) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color.White, RoundedCornerShape(9.dp))
                        .border(1.dp, GeoNavyMuted, RoundedCornerShape(9.dp))
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = GeoNavyMuted,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = if (isUnlocked) GeoNavy else GeoNavyMuted
        )
    }
}
