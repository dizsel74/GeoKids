package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.ui.theme.GeoMint
import com.example.ui.theme.GeoMintBorder
import com.example.ui.theme.GeoMintCard
import com.example.ui.theme.GeoMintSurface
import com.example.ui.theme.GeoNavy
import com.example.ui.theme.GeoNavyMuted
import com.example.ui.theme.GeoOrange

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
        solidEs = "Prisma rectangular / Paralelepípedo",
        solidEn = "Rectangular prism",
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

data class Polygon2DData(
    val id: String,
    val categoryEs: String,
    val categoryEn: String,
    val nameEs: String,
    val nameEn: String,
    val descEs: String,
    val descEn: String,
    val color: Color
)

val polygons2DList = listOf(
    // Triangles
    Polygon2DData(
        id = "equilateral_tri",
        categoryEs = "Triángulos (Polígonos de 3 lados)",
        categoryEn = "Triangles (3-sided Polygons)",
        nameEs = "Triángulo Equilátero",
        nameEn = "Equilateral Triangle",
        descEs = "¡Tiene tres lados exactamente iguales! Es un polígono regular de simetría perfecta.",
        descEn = "It has three exactly equal sides! It's a regular polygon with perfect symmetry.",
        color = Color(0xFFEF4444)
    ),
    Polygon2DData(
        id = "isosceles_tri",
        categoryEs = "Triángulos (Polígonos de 3 lados)",
        categoryEn = "Triangles (3-sided Polygons)",
        nameEs = "Triángulo Isósceles",
        nameEn = "Isosceles Triangle",
        descEs = "¡Tiene dos lados iguales y uno diferente! Como la forma de una rebanada de pizza.",
        descEn = "It has two equal sides and one different side! Like the shape of a pizza slice.",
        color = Color(0xFFF59E0B)
    ),
    Polygon2DData(
        id = "scalene_tri",
        categoryEs = "Triángulos (Polígonos de 3 lados)",
        categoryEn = "Triangles (3-sided Polygons)",
        nameEs = "Triángulo Escaleno",
        nameEn = "Scalene Triangle",
        descEs = "¡Tiene sus tres lados completamente diferentes! Ninguno mide lo mismo.",
        descEn = "It has three completely different sides! None of them measure the same.",
        color = Color(0xFF10B981)
    ),
    // Quadrilaterals
    Polygon2DData(
        id = "square",
        categoryEs = "Cuadriláteros (Polígonos de 4 lados)",
        categoryEn = "Quadrilaterals (4-sided Polygons)",
        nameEs = "Cuadrado",
        nameEn = "Square",
        descEs = "¡Tiene cuatro lados iguales y cuatro ángulos rectos perfectos de 90 grados!",
        descEn = "It has four equal sides and four perfect 90-degree right angles!",
        color = Color(0xFF3B82F6)
    ),
    Polygon2DData(
        id = "rectangle",
        categoryEs = "Cuadriláteros (Polígonos de 4 lados)",
        categoryEn = "Quadrilaterals (4-sided Polygons)",
        nameEs = "Rectángulo",
        nameEn = "Rectangle",
        descEs = "Cuatro lados paralelos dos a dos, opuestos de la misma medida con esquinas rectas.",
        descEn = "Four sides parallel two-by-two, opposite sides are equal with straight corners.",
        color = Color(0xFF6366F1)
    ),
    Polygon2DData(
        id = "rhombus",
        categoryEs = "Cuadriláteros (Polígonos de 4 lados)",
        categoryEn = "Quadrilaterals (4-sided Polygons)",
        nameEs = "Rombo",
        nameEn = "Rhombus",
        descEs = "¡Tiene cuatro lados iguales pero esquinas inclinadas, como una cometa voladora!",
        descEn = "It has four equal sides but tilted corners, just like a flying kite!",
        color = Color(0xFF8B5CF6)
    ),
    Polygon2DData(
        id = "trapezoid",
        categoryEs = "Cuadriláteros (Polígonos de 4 lados)",
        categoryEn = "Quadrilaterals (4-sided Polygons)",
        nameEs = "Trapecio",
        nameEn = "Trapezoid",
        descEs = "Tiene solamente un par de lados paralelos. ¡Se parece a la forma de una falda!",
        descEn = "It has only one pair of parallel sides. It resembles the shape of a skirt!",
        color = Color(0xFFEC4899)
    ),
    Polygon2DData(
        id = "parallelogram",
        categoryEs = "Cuadriláteros (Polígonos de 4 lados)",
        categoryEn = "Quadrilaterals (4-sided Polygons)",
        nameEs = "Paralelogramo",
        nameEn = "Parallelogram",
        descEs = "Tiene dos pares de lados paralelos paralelos. Es como un rectángulo inclinado.",
        descEn = "It has two pairs of parallel sides. It is like a tilted rectangle.",
        color = Color(0xFF14B8A6)
    ),
    // Regular Polygons
    Polygon2DData(
        id = "pentagon",
        categoryEs = "Polígonos Regulares Superiores",
        categoryEn = "Higher Regular Polygons",
        nameEs = "Pentágono",
        nameEn = "Pentagon",
        descEs = "¡Un polígono regular de 5 lados iguales! Tiene la forma icónica de una casita.",
        descEn = "A regular polygon with 5 equal sides! It has the iconic shape of a small house.",
        color = Color(0xFF06B6D4)
    ),
    Polygon2DData(
        id = "hexagon",
        categoryEs = "Polígonos Regulares Superiores",
        categoryEn = "Higher Regular Polygons",
        nameEs = "Hexágono",
        nameEn = "Hexagon",
        descEs = "¡Un polígono regular de 6 lados iguales! Es la forma de un panal de abejas.",
        descEn = "A regular polygon with 6 equal sides! It is the shape of a honeycomb cell.",
        color = Color(0xFFF43F5E)
    ),
    Polygon2DData(
        id = "octagon",
        categoryEs = "Polígonos Regulares Superiores",
        categoryEn = "Higher Regular Polygons",
        nameEs = "Octágono",
        nameEn = "Octagon",
        descEs = "¡Un polígono regular de 8 lados iguales! Es la forma de una señal de Stop en la calle.",
        descEn = "A regular polygon with 8 equal sides! It is the shape of a street Stop sign.",
        color = Color(0xFF84CC16)
    ),
    // Curves
    Polygon2DData(
        id = "circle",
        categoryEs = "Figuras con Curvas (No Polígonos)",
        categoryEn = "Curved Shapes (Non-Polygons)",
        nameEs = "Círculo",
        nameEn = "Circle",
        descEs = "¡Perfectamente redondo! Todos sus puntos están a la misma distancia del centro.",
        descEn = "Perfectly round! All of its points are at the same distance from the center.",
        color = Color(0xFF10B981)
    ),
    Polygon2DData(
        id = "oval_ellipse",
        categoryEs = "Figuras con Curvas (No Polígonos)",
        categoryEn = "Curved Shapes (Non-Polygons)",
        nameEs = "Óvalo / Elipse",
        nameEn = "Oval / Ellipse",
        descEs = "¡Es como un círculo estirado! Tiene la forma de un huevo o de un balón de fútbol.",
        descEn = "It's like a stretched circle! It has the shape of an egg or a football.",
        color = Color(0xFFEAB308)
    )
)

@Composable
fun DictionaryScreen(
    isSpanish: Boolean,
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableStateOf("2d_polygons") } // "2d_polygons" or "2d_to_3d"

    val items = listOf(
        ProgressionData.SquareToCube,
        ProgressionData.RectangleToPrism,
        ProgressionData.TriangleToPrism,
        ProgressionData.CircleToCylinder
    )

    LaunchedEffect(activeSubTab) {
        if (activeSubTab == "2d_to_3d") {
            kotlinx.coroutines.delay(200)
            com.example.ui.TtsManager.speak("Cube")
        } else {
            kotlinx.coroutines.delay(200)
            com.example.ui.TtsManager.speak("Equilateral Triangle")
        }
    }

    val backgroundColor = GeoMintSurface

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Header using the correct technical terminology "Polygons" / "Polígonos"
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (isSpanish) "Polígonos y Cuerpos" else "Polygons & Solids",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                ),
                color = GeoNavy
            )
            Text(
                text = if (isSpanish) "Explora la estructura técnica de los polígonos y la magia de los cuerpos 3D." 
                       else "Explore the technical structure of polygons and the magic of 3D solids.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                ),
                color = GeoNavyMuted
            )
        }

        // Pill Sub-tab selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GeoMintCard, RoundedCornerShape(16.dp))
                .border(1.dp, GeoMint, RoundedCornerShape(16.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { activeSubTab = "2d_polygons" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeSubTab == "2d_polygons") GeoOrange else Color.Transparent,
                    contentColor = if (activeSubTab == "2d_polygons") Color.White else GeoNavy
                ),
                elevation = if (activeSubTab == "2d_polygons") ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text(
                    text = if (isSpanish) "Polígonos 2D" else "2D Polygons",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.5.sp
                )
            }
            Button(
                onClick = { activeSubTab = "2d_to_3d" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeSubTab == "2d_to_3d") GeoOrange else Color.Transparent,
                    contentColor = if (activeSubTab == "2d_to_3d") Color.White else GeoNavy
                ),
                elevation = if (activeSubTab == "2d_to_3d") ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text(
                    text = if (isSpanish) "De 2D a 3D" else "Flat to Solid (3D)",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.5.sp
                )
            }
        }

        // LazyColumn for lists
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .testTag("dictionary_list"),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (activeSubTab == "2d_polygons") {
                // Group by categories and render them
                val grouped = polygons2DList.groupBy { if (isSpanish) it.categoryEs else it.categoryEn }
                grouped.forEach { (categoryName, shapeList) ->
                    item {
                        Text(
                            text = categoryName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = GeoNavy
                            ),
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }
                    items(shapeList) { shape ->
                        Polygon2DCard(shape = shape, isSpanish = isSpanish)
                    }
                }
            } else {
                items(items) { item ->
                    DictionaryCard(item = item, isSpanish = isSpanish)
                }
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
            .border(1.dp, GeoMintBorder, RoundedCornerShape(20.dp))
            .clickable {
                com.example.ui.TtsManager.speak(item.solidEn)
            }
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
                        text = if (isSpanish) "2D Plano" else "2D Flat",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = GeoNavyMuted
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ShapeCanvas(shapeId = item.id, is3D = false, color = item.color2D)
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isSpanish) item.flatEs else item.flatEn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = item.color3D
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (isSpanish) item.flatEn else item.flatEs,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Normal,
                                color = GeoNavyMuted
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Growth Arrow
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Grows to",
                        tint = item.color3D,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (isSpanish) "¡Crece!" else "Grows!",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
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
                        text = if (isSpanish) "3D Sólido" else "3D Solid",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = GeoNavyMuted
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ShapeCanvas(shapeId = item.id, is3D = true, color = item.color3D)
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isSpanish) item.solidEs else item.solidEn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = item.color3D
                            ),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (isSpanish) item.solidEn else item.solidEs,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Normal,
                                color = GeoNavyMuted
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Explanatory Bilingual text box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GeoMintCard, RoundedCornerShape(14.dp))
                    .border(1.dp, GeoMint, RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = GeoOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isSpanish) "¿Cómo se forma?" else "How is it made?",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = GeoNavy
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isSpanish) item.explanationEs else item.explanationEn,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        lineHeight = 18.sp
                    ),
                    color = GeoNavy
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

@Composable
fun Polygon2DCard(
    shape: Polygon2DData,
    isSpanish: Boolean
) {
    val scope = rememberCoroutineScope()
    var showTranslation by remember(shape) { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GeoMintBorder, RoundedCornerShape(20.dp))
            .clickable {
                com.example.ui.TtsManager.speak(shape.nameEn)
            }
            .testTag("polygon_card_${shape.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Visual display (left)
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(shape.color.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                PolygonCanvas(id = shape.id, color = shape.color)
            }

            // Description and title (right)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (showTranslation) shape.nameEs else shape.nameEn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = GeoNavy
                        )
                    )
                    
                    // Translation button
                    IconButton(
                        onClick = {
                            scope.launch {
                                showTranslation = true
                                delay(2000)
                                showTranslation = false
                            }
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = if (isSpanish) "Traducir" else "Translate",
                            tint = GeoOrange,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = if (isSpanish) shape.descEs else shape.descEn,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        lineHeight = 17.sp
                    ),
                    color = GeoNavyMuted
                )
            }
        }
    }
}

@Composable
fun PolygonCanvas(
    id: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .size(80.dp)
            .padding(4.dp)
    ) {
        val w = size.width
        val h = size.height

        val strokeWidth = 3.dp.toPx()
        val outlineColor = color.darken()

        when (id) {
            "equilateral_tri" -> {
                val path = Path().apply {
                    moveTo(w / 2, h * 0.15f)
                    lineTo(w * 0.15f, h * 0.85f)
                    lineTo(w * 0.85f, h * 0.85f)
                    close()
                }
                drawPath(path = path, color = color)
                drawPath(path = path, color = outlineColor, style = Stroke(width = strokeWidth))
            }
            "isosceles_tri" -> {
                val path = Path().apply {
                    moveTo(w / 2, h * 0.1f)
                    lineTo(w * 0.3f, h * 0.9f)
                    lineTo(w * 0.7f, h * 0.9f)
                    close()
                }
                drawPath(path = path, color = color)
                drawPath(path = path, color = outlineColor, style = Stroke(width = strokeWidth))
            }
            "scalene_tri" -> {
                val path = Path().apply {
                    moveTo(w * 0.45f, h * 0.15f)
                    lineTo(w * 0.15f, h * 0.85f)
                    lineTo(w * 0.9f, h * 0.7f)
                    close()
                }
                drawPath(path = path, color = color)
                drawPath(path = path, color = outlineColor, style = Stroke(width = strokeWidth))
            }
            "square" -> {
                val sizeSquare = w * 0.7f
                val x = (w - sizeSquare) / 2
                val y = (h - sizeSquare) / 2
                drawRoundRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(sizeSquare, sizeSquare),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                )
                drawRoundRect(
                    color = outlineColor,
                    topLeft = Offset(x, y),
                    size = Size(sizeSquare, sizeSquare),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
            }
            "rectangle" -> {
                val rw = w * 0.85f
                val rh = h * 0.5f
                val rx = (w - rw) / 2
                val ry = (h - rh) / 2
                drawRoundRect(
                    color = color,
                    topLeft = Offset(rx, ry),
                    size = Size(rw, rh),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                )
                drawRoundRect(
                    color = outlineColor,
                    topLeft = Offset(rx, ry),
                    size = Size(rw, rh),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
            }
            "rhombus" -> {
                val path = Path().apply {
                    moveTo(w / 2, h * 0.15f)
                    lineTo(w * 0.85f, h / 2)
                    lineTo(w / 2, h * 0.85f)
                    lineTo(w * 0.15f, h / 2)
                    close()
                }
                drawPath(path = path, color = color)
                drawPath(path = path, color = outlineColor, style = Stroke(width = strokeWidth))
            }
            "trapezoid" -> {
                val path = Path().apply {
                    moveTo(w * 0.35f, h * 0.25f)
                    lineTo(w * 0.65f, h * 0.25f)
                    lineTo(w * 0.85f, h * 0.75f)
                    lineTo(w * 0.15f, h * 0.75f)
                    close()
                }
                drawPath(path = path, color = color)
                drawPath(path = path, color = outlineColor, style = Stroke(width = strokeWidth))
            }
            "parallelogram" -> {
                val path = Path().apply {
                    moveTo(w * 0.35f, h * 0.25f)
                    lineTo(w * 0.85f, h * 0.25f)
                    lineTo(w * 0.65f, h * 0.75f)
                    lineTo(w * 0.15f, h * 0.75f)
                    close()
                }
                drawPath(path = path, color = color)
                drawPath(path = path, color = outlineColor, style = Stroke(width = strokeWidth))
            }
            "pentagon" -> {
                val path = Path()
                val cx = w / 2
                val cy = h / 2
                val r = w * 0.38f
                for (i in 0 until 5) {
                    val angle = -Math.PI / 2 + (i * 2 * Math.PI / 5)
                    val px = cx + r * Math.cos(angle).toFloat()
                    val py = cy + r * Math.sin(angle).toFloat()
                    if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
                }
                path.close()
                drawPath(path = path, color = color)
                drawPath(path = path, color = outlineColor, style = Stroke(width = strokeWidth))
            }
            "hexagon" -> {
                val path = Path()
                val cx = w / 2
                val cy = h / 2
                val r = w * 0.38f
                for (i in 0 until 6) {
                    val angle = -Math.PI / 2 + (i * 2 * Math.PI / 6)
                    val px = cx + r * Math.cos(angle).toFloat()
                    val py = cy + r * Math.sin(angle).toFloat()
                    if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
                }
                path.close()
                drawPath(path = path, color = color)
                drawPath(path = path, color = outlineColor, style = Stroke(width = strokeWidth))
            }
            "octagon" -> {
                val path = Path()
                val cx = w / 2
                val cy = h / 2
                val r = w * 0.38f
                for (i in 0 until 8) {
                    val angle = -Math.PI / 2 + Math.PI / 8 + (i * 2 * Math.PI / 8)
                    val px = cx + r * Math.cos(angle).toFloat()
                    val py = cy + r * Math.sin(angle).toFloat()
                    if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
                }
                path.close()
                drawPath(path = path, color = color)
                drawPath(path = path, color = outlineColor, style = Stroke(width = strokeWidth))
            }
            "circle" -> {
                val r = w * 0.35f
                drawCircle(color = color, radius = r, center = Offset(w / 2, h / 2))
                drawCircle(color = outlineColor, radius = r, center = Offset(w / 2, h / 2), style = Stroke(width = strokeWidth))
            }
            "oval_ellipse" -> {
                drawOval(
                    color = color,
                    topLeft = Offset(w * 0.1f, h * 0.25f),
                    size = Size(w * 0.8f, h * 0.5f)
                )
                drawOval(
                    color = outlineColor,
                    topLeft = Offset(w * 0.1f, h * 0.25f),
                    size = Size(w * 0.8f, h * 0.5f),
                    style = Stroke(width = strokeWidth)
                )
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
