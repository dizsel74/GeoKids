package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class ProgressionData(
    val id: String,
    val flatEs: String,
    val flatEn: String,
    val solidEs: String,
    val solidEn: String,
    val explanationEs: String,
    val explanationEn: String,
    val color2D: Color,
    val color3D: Color
) {
    object SquareToCube : ProgressionData(
        id = "square_to_cube",
        flatEs = "Cuadrado",
        flatEn = "Square",
        solidEs = "Cubo",
        solidEn = "Cube",
        explanationEs = "Si juntas 6 cuadrados planos como si fueran paredes, ¡creas un cubo en 3D!",
        explanationEn = "If you put 6 flat squares together like walls, you create a 3D cube!",
        color2D = Color(0xFFFF8A65), // Pastel orange
        color3D = Color(0xFFEF4444)  // Bold red-orange
    )

    object RectangleToPrism : ProgressionData(
        id = "rect_to_prism",
        flatEs = "Rectángulo",
        flatEn = "Rectangle",
        solidEs = "Prisma Rectangular",
        solidEn = "Rectangular Prism",
        explanationEs = "¡Usa rectángulos planos para hacer una caja larga en 3D con esquinas rectas!",
        explanationEn = "Use flat rectangles to build a long 3D box with straight corners!",
        color2D = Color(0xFF4FC3F7), // Pastel blue
        color3D = Color(0xFF2563EB)  // Bold blue
    )

    object TriangleToPrism : ProgressionData(
        id = "tri_to_prism",
        flatEs = "Triángulo",
        flatEn = "Triangle",
        solidEs = "Prisma Triangular",
        solidEn = "Triangular Prism",
        explanationEs = "Dos bases triangulares y lados rectangulares forman un prisma. ¡Como un tobogán!",
        explanationEn = "Two triangular bases and rectangular sides make this prism. Like a playground slide!",
        color2D = Color(0xFF81C784), // Pastel green
        color3D = Color(0xFF10B981)  // Bold green
    )

    object CircleToCylinder : ProgressionData(
        id = "circle_to_cylinder",
        flatEs = "Círculo",
        flatEn = "Circle",
        solidEs = "Cilindro",
        solidEn = "Cylinder",
        explanationEs = "Estira un círculo hacia arriba y obtendrás un cilindro redondo. ¡Como una lata de refresco!",
        explanationEn = "Stretch a flat circle upwards to make a round cylinder. Just like a soda can!",
        color2D = Color(0xFFFFD54F), // Pastel yellow
        color3D = Color(0xFFF59E0B)  // Bold amber
    )
}

@Composable
fun DictionaryScreen(
    isSpanish: Boolean,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        ProgressionData.SquareToCube,
        ProgressionData.RectangleToPrism,
        ProgressionData.TriangleToPrism,
        ProgressionData.CircleToCylinder
    )

    // Solid high-contrast Slate-light background
    val backgroundColor = Color(0xFFF7F9FC)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (isSpanish) "De 2D a 3D" else "Flat to Solid",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp
                ),
                color = Color(0xFF1A1C1E)
            )
            Text(
                text = if (isSpanish) "¡Mira cómo las figuras planas (2D) crecen y se convierten en cuerpos con volumen (3D)!" 
                       else "See how flat shapes (2D) stretch to become solid bodies (3D)!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                ),
                color = Color(0xFF74777F)
            )
        }

        // LazyColumn for lists
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .testTag("dictionary_list"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(items) { item ->
                DictionaryCard(item = item, isSpanish = isSpanish)
            }
        }
    }
}

@Composable
fun DictionaryCard(
    item: ProgressionData,
    isSpanish: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .testTag("dictionary_card_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Visual comparison row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(item.color3D.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 2D Part
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "2D Plano / Flat",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF74777F)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ShapeCanvas(shapeId = item.id, is3D = false, color = item.color2D)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isSpanish) item.flatEs else item.flatEn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = item.color3D
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                // Growth Arrow
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Grows to",
                        tint = item.color3D,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (isSpanish) "¡Crece!" else "Grows!",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = item.color3D
                        )
                    )
                }

                // 3D Part
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "3D Sólido / Solid",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF74777F)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ShapeCanvas(shapeId = item.id, is3D = true, color = item.color3D)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isSpanish) item.solidEs else item.solidEn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = item.color3D
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Explanatory Bilingual text box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(14.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = if (isSpanish) "💡 ¿Cómo se forma?" else "💡 How is it made?",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1C1E)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isSpanish) item.explanationEs else item.explanationEn,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp
                    ),
                    color = Color(0xFF374151)
                )
            }
        }
    }
}

@Composable
fun ShapeCanvas(
    shapeId: String,
    is3D: Boolean,
    color: Color
) {
    Canvas(
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp)
    ) {
        val w = size.width
        val h = size.height

        if (!is3D) {
            // Draw 2D Shapes on Canvas
            when (shapeId) {
                "square_to_cube" -> {
                    // Square
                    val sizeSquare = w * 0.7f
                    val x = (w - sizeSquare) / 2
                    val y = (h - sizeSquare) / 2
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(x, y),
                        size = Size(sizeSquare, sizeSquare),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                    )
                    drawRoundRect(
                        color = color.darken(),
                        topLeft = Offset(x, y),
                        size = Size(sizeSquare, sizeSquare),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
                "rect_to_prism" -> {
                    // Rectangle
                    val rw = w * 0.85f
                    val rh = h * 0.5f
                    val rx = (w - rw) / 2
                    val ry = (h - rh) / 2
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(rx, ry),
                        size = Size(rw, rh),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                    )
                    drawRoundRect(
                        color = color.darken(),
                        topLeft = Offset(rx, ry),
                        size = Size(rw, rh),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx()),
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
                "tri_to_prism" -> {
                    // Triangle
                    val path = Path().apply {
                        moveTo(w / 2, h * 0.15f)
                        lineTo(w * 0.15f, h * 0.85f)
                        lineTo(w * 0.85f, h * 0.85f)
                        close()
                    }
                    drawPath(path = path, color = color)
                    drawPath(path = path, color = color.darken(), style = Stroke(width = 3.dp.toPx()))
                }
                "circle_to_cylinder" -> {
                    // Circle
                    val r = w * 0.35f
                    drawCircle(color = color, radius = r, center = Offset(w / 2, h / 2))
                    drawCircle(color = color.darken(), radius = r, center = Offset(w / 2, h / 2), style = Stroke(width = 3.dp.toPx()))
                }
            }
        } else {
            // Draw 3D Isometric Projection Drawings on Canvas
            when (shapeId) {
                "square_to_cube" -> {
                    // 3D Cube isometric sketch
                    // Front face, top face, right face
                    val p = Path()
                    // Front Face
                    val fx = w * 0.15f
                    val fy = h * 0.45f
                    val cw = w * 0.5f
                    
                    // Let's sketch it manually
                    // Front Face: (fx, fy) -> (fx+cw, fy) -> (fx+cw, fy+cw) -> (fx, fy+cw)
                    p.moveTo(fx, fy)
                    p.lineTo(fx + cw, fy)
                    p.lineTo(fx + cw, fy + cw)
                    p.lineTo(fx, fy + cw)
                    p.close()
                    drawPath(path = p, color = color)

                    // Top Face: (fx, fy) -> (fx+cw, fy) -> (fx+cw+20, fy-20) -> (fx+20, fy-20)
                    val ox = 18.dp.toPx()
                    val oy = 18.dp.toPx()
                    val pTop = Path().apply {
                        moveTo(fx, fy)
                        lineTo(fx + cw, fy)
                        lineTo(fx + cw + ox, fy - oy)
                        lineTo(fx + ox, fy - oy)
                        close()
                    }
                    drawPath(path = pTop, color = color.lighten(0.15f))

                    // Right Face: (fx+cw, fy) -> (fx+cw+ox, fy-oy) -> (fx+cw+ox, fy-oy+cw) -> (fx+cw, fy+cw)
                    val pRight = Path().apply {
                        moveTo(fx + cw, fy)
                        lineTo(fx + cw + ox, fy - oy)
                        lineTo(fx + cw + ox, fy - oy + cw)
                        lineTo(fx + cw, fy + cw)
                        close()
                    }
                    drawPath(path = pRight, color = color.darken(0.15f))

                    // Draw Edges
                    drawPath(p, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))
                    drawPath(pTop, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))
                    drawPath(pRight, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))
                }
                "rect_to_prism" -> {
                    // Rectangular Prism isometric sketch
                    val fx = w * 0.1f
                    val fy = h * 0.45f
                    val pw = w * 0.55f
                    val ph = h * 0.35f
                    val ox = 18.dp.toPx()
                    val oy = 15.dp.toPx()

                    // Front
                    val pFront = Path().apply {
                        moveTo(fx, fy)
                        lineTo(fx + pw, fy)
                        lineTo(fx + pw, fy + ph)
                        lineTo(fx, fy + ph)
                        close()
                    }
                    drawPath(pFront, color)

                    // Top
                    val pTop = Path().apply {
                        moveTo(fx, fy)
                        lineTo(fx + pw, fy)
                        lineTo(fx + pw + ox, fy - oy)
                        lineTo(fx + ox, fy - oy)
                        close()
                    }
                    drawPath(pTop, color.lighten(0.15f))

                    // Right
                    val pRight = Path().apply {
                        moveTo(fx + pw, fy)
                        lineTo(fx + pw + ox, fy - oy)
                        lineTo(fx + pw + ox, fy - oy + ph)
                        lineTo(fx + pw, fy + ph)
                        close()
                    }
                    drawPath(pRight, color.darken(0.15f))

                    drawPath(pFront, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))
                    drawPath(pTop, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))
                    drawPath(pRight, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))
                }
                "tri_to_prism" -> {
                    // Triangular prism isometric sketch
                    // Base triangle at front, base triangle at back, side rectangles
                    val fx = w * 0.15f
                    val fy = h * 0.8f
                    val pw = w * 0.45f
                    val ph = h * 0.45f
                    val ox = 20.dp.toPx()
                    val oy = 18.dp.toPx()

                    // Front Triangle (fx, fy) -> (fx+pw, fy) -> (fx+pw/2, fy-ph)
                    val pFront = Path().apply {
                        moveTo(fx, fy)
                        lineTo(fx + pw, fy)
                        lineTo(fx + pw / 2, fy - ph)
                        close()
                    }
                    drawPath(pFront, color)

                    // Side Slanted Face (fx+pw/2, fy-ph) -> (fx+pw/2+ox, fy-ph-oy) -> (fx+pw+ox, fy-oy) -> (fx+pw, fy)
                    val pSlanted = Path().apply {
                        moveTo(fx + pw / 2, fy - ph)
                        lineTo(fx + pw / 2 + ox, fy - ph - oy)
                        lineTo(fx + pw + ox, fy - oy)
                        lineTo(fx + pw, fy)
                        close()
                    }
                    drawPath(pSlanted, color.lighten(0.15f))

                    // Side Bottom Face (fx, fy) -> (fx+pw, fy) -> (fx+pw+ox, fy-oy) -> (fx+ox, fy-oy) [hidden, so just draw slanted & front]
                    
                    // Draw edges
                    drawPath(pFront, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))
                    drawPath(pSlanted, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))
                    
                    // Extra back-side triangle edges to make it look truly 3D
                    val pBackLines = Path().apply {
                        moveTo(fx + pw / 2 + ox, fy - ph - oy)
                        lineTo(fx + ox, fy - oy)
                        lineTo(fx + pw + ox, fy - oy)
                        moveTo(fx, fy)
                        lineTo(fx + ox, fy - oy)
                    }
                    drawPath(pBackLines, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))
                }
                "circle_to_cylinder" -> {
                    // Cylinder isometric sketch
                    // Top ellipse, bottom semi-ellipse, vertical side lines
                    val cx = w * 0.5f
                    val cyTop = h * 0.3f
                    val cyBot = h * 0.7f
                    val rx = w * 0.35f
                    val ry = h * 0.12f

                    // Bottom face (filled cylinder body)
                    // Draw main body rect
                    drawRect(
                        color = color,
                        topLeft = Offset(cx - rx, cyTop),
                        size = Size(rx * 2, cyBot - cyTop)
                    )
                    
                    // Bottom cap
                    drawOval(
                        color = color,
                        topLeft = Offset(cx - rx, cyBot - ry),
                        size = Size(rx * 2, ry * 2)
                    )

                    // Top cap (lighter shade)
                    drawOval(
                        color = color.lighten(0.15f),
                        topLeft = Offset(cx - rx, cyTop - ry),
                        size = Size(rx * 2, ry * 2)
                    )

                    // Outline top
                    drawOval(
                        color = color.darken(0.4f),
                        topLeft = Offset(cx - rx, cyTop - ry),
                        size = Size(rx * 2, ry * 2),
                        style = Stroke(width = 2.5.dp.toPx())
                    )

                    // Outline bottom half
                    val pathBottomHalf = Path().apply {
                        moveTo(cx - rx, cyBot)
                        // quadratic to approximate semi circle
                        quadraticTo(cx, cyBot + ry, cx + rx, cyBot)
                    }
                    drawPath(pathBottomHalf, color.darken(0.4f), style = Stroke(width = 2.5.dp.toPx()))

                    // Vertical lines
                    drawLine(
                        color = color.darken(0.4f),
                        start = Offset(cx - rx, cyTop),
                        end = Offset(cx - rx, cyBot),
                        strokeWidth = 2.5.dp.toPx()
                    )
                    drawLine(
                        color = color.darken(0.4f),
                        start = Offset(cx + rx, cyTop),
                        end = Offset(cx + rx, cyBot),
                        strokeWidth = 2.5.dp.toPx()
                    )
                }
            }
        }
    }
}

// Helper Extensions for colors
private fun Color.darken(factor: Float = 0.2f): Color {
    return Color(
        red = (this.red * (1f - factor)).coerceIn(0f, 1f),
        green = (this.green * (1f - factor)).coerceIn(0f, 1f),
        blue = (this.blue * (1f - factor)).coerceIn(0f, 1f),
        alpha = this.alpha
    )
}

private fun Color.lighten(factor: Float = 0.2f): Color {
    return Color(
        red = (this.red + (1f - this.red) * factor).coerceIn(0f, 1f),
        green = (this.green + (1f - this.green) * factor).coerceIn(0f, 1f),
        blue = (this.blue + (1f - this.blue) * factor).coerceIn(0f, 1f),
        alpha = this.alpha
    )
}
