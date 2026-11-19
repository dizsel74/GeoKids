package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GeometryWebView

sealed class GeometricShape(
    val id: String,
    val nameEs: String,
    val nameEn: String,
    val vertices: Int,
    val edges: Int,
    val faces: Int,
    val descriptionEs: String,
    val descriptionEn: String
) {
    object Cube : GeometricShape(
        id = "cube",
        nameEs = "Cubo",
        nameEn = "Cube",
        vertices = 8,
        edges = 12,
        faces = 6,
        descriptionEs = "¡Tiene 6 caras que son cuadrados perfectos iguales! Es como un dado de juego.",
        descriptionEn = "It has 6 faces that are identical perfect squares! It's just like a playing die."
    )

    object RectangularPrism : GeometricShape(
        id = "prism",
        nameEs = "Prisma rectangular / Paralelepípedo",
        nameEn = "Rectangular prism",
        vertices = 8,
        edges = 12,
        faces = 6,
        descriptionEs = "Tiene caras rectangulares. ¡Piensa en una caja de cereal o en tu libro favorito!",
        descriptionEn = "It has rectangular faces. Think of a cereal box or your favorite book!"
    )

    object TriangularPrism : GeometricShape(
        id = "triangular_prism",
        nameEs = "Prisma triangular",
        nameEn = "Triangular prism",
        vertices = 6,
        edges = 9,
        faces = 5,
        descriptionEs = "¡Tiene 2 bases triangulares iguales y 3 caras laterales rectangulares! Como una tienda de campaña.",
        descriptionEn = "It has 2 identical triangular bases and 3 rectangular side faces! Just like a camping tent."
    )

    object Pyramid : GeometricShape(
        id = "pyramid",
        nameEs = "Pirámide de base cuadrada",
        nameEn = "Square-based pyramid",
        vertices = 5,
        edges = 8,
        faces = 5,
        descriptionEs = "¡Tiene una base cuadrada abajo y sus lados terminan en una punta arriba (cúspide)!",
        descriptionEn = "It has a square base at the bottom, and all its sides meet at a point (apex) at the top!"
    )

    object TriangularPyramid : GeometricShape(
        id = "triangular_pyramid",
        nameEs = "Pirámide de base triangular",
        nameEn = "Triangular pyramid",
        vertices = 4,
        edges = 6,
        faces = 4,
        descriptionEs = "¡Tiene una base triangular abajo y 3 caras triangulares laterales! También se llama tetraedro.",
        descriptionEn = "It has a triangular base at the bottom and 3 triangular sides! It is also known as a tetrahedron."
    )

    object Cylinder : GeometricShape(
        id = "cylinder",
        nameEs = "Cilindro",
        nameEn = "Cylinder",
        vertices = 0,
        edges = 2,
        faces = 3,
        descriptionEs = "¡Tiene 2 caras planas circulares (bases) y 1 superficie curva a su alrededor! Como una lata de refresco.",
        descriptionEn = "It has 2 flat circular faces (bases) and 1 curved surface around it! Just like a soda can."
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Explorer3DScreen(
    isSpanish: Boolean,
    modifier: Modifier = Modifier
) {
    val shapes = listOf(
        GeometricShape.Cube,
        GeometricShape.RectangularPrism,
        GeometricShape.TriangularPrism,
        GeometricShape.Pyramid,
        GeometricShape.TriangularPyramid,
        GeometricShape.Cylinder
    )
    var selectedShape by remember { mutableStateOf<GeometricShape>(GeometricShape.Cube) }
    
    // Automatically report viewed shapes to the reward system & speak name in English
    LaunchedEffect(selectedShape) {
        com.example.ui.AchievementManager.exploreShape(selectedShape.id)
        com.example.ui.TtsManager.speak(selectedShape.nameEn)
    }
    
    var showVertices by remember { mutableStateOf(true) }
    var showEdges by remember { mutableStateOf(true) }
    var showFaces by remember { mutableStateOf(true) }
    
    var showHelpDialog by remember { mutableStateOf(false) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Solid high-contrast Slate-light background
    val backgroundColor = Color(0xFFF7F9FC)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Heading / Section title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isSpanish) "Explorador 3D" else "3D Explorer",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    ),
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    text = if (isSpanish) "¡Toca y gira las figuras con tu dedo!" else "Touch and spin the shapes with your finger!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    ),
                    color = Color(0xFF74777F)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { showHelpDialog = true },
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    .size(44.dp)
                    .testTag("help_button")
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Help",
                    tint = Color(0xFF2563EB)
                )
            }
        }

        // Dropdown Menu Selector Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dropdownExpanded = true }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("shape_selector_dropdown"),
                contentAlignment = Alignment.CenterStart
            ) {
                    val scope = rememberCoroutineScope()
                    var showTranslation by remember(selectedShape) { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = if (isSpanish) "Elige tu figura geométrica:" else "Choose your 3D Shape:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2563EB)
                                )
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (showTranslation) selectedShape.nameEs else selectedShape.nameEn,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF1A1C1E)
                                    )
                                )
                                Button(
                                    onClick = {
                                        scope.launch {
                                            showTranslation = true
                                            kotlinx.coroutines.delay(2000)
                                            showTranslation = false
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Text(
                                        text = "🇲🇽",
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select shape",
                        tint = Color(0xFF1A1C1E),
                        modifier = Modifier.size(30.dp)
                    )
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                ) {
                    shapes.forEach { shape ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (isSpanish) shape.nameEs else shape.nameEn,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (selectedShape == shape) Color(0xFF2563EB) else Color(0xFF1A1C1E)
                                )
                            },
                            onClick = {
                                selectedShape = shape
                                dropdownExpanded = false
                            },
                            modifier = Modifier.testTag("dropdown_item_${shape.id}")
                        )
                    }
                }
            }
        }

        // 3D Viewer Container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .border(2.dp, Color(0xFFEFF6FF), RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                GeometryWebView(
                    shape = selectedShape.id,
                    showVertices = showVertices,
                    showEdges = showEdges,
                    showFaces = showFaces,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .testTag("geometry_3d_view")
                )
            }
        }

        // Feature Toggles Row (Vertices, Edges, Faces)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (isSpanish) "Modos de Vista (¡Combínalos!)" else "View Options (Combine them!)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Vertices Toggle
                    ToggleButton(
                        text = if (isSpanish) "Esquinas\n(Vértices)" else "Corners\n(Vertices)",
                        isSelected = showVertices,
                        onSelectedChange = { showVertices = it },
                        activeColor = Color(0xFFEF4444), // Red for vertices
                        bgColor = Color(0xFFFEF2F2),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_vertices")
                    )

                    // Edges Toggle
                    ToggleButton(
                        text = if (isSpanish) "Líneas\n(Aristas)" else "Lines\n(Edges)",
                        isSelected = showEdges,
                        onSelectedChange = { showEdges = it },
                        activeColor = Color(0xFF2563EB), // Blue for edges
                        bgColor = Color(0xFFEFF6FF),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_edges")
                    )

                    // Faces Toggle
                    ToggleButton(
                        text = if (isSpanish) "Caras\n(Lados)" else "Sides\n(Faces)",
                        isSelected = showFaces,
                        onSelectedChange = { showFaces = it },
                        activeColor = Color(0xFF10B981), // Green for faces
                        bgColor = Color(0xFFECFDF5),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_faces")
                    )
                }
            }
        }

        // Live Count Statistics Card (Bilingual counts)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isSpanish) "Datos de la Figura" else "Shape Statistics",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1C1E)
                    )
                )

                // Stats Counters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Vertices Count
                    StatCounter(
                        count = selectedShape.vertices,
                        labelEs = "Vértices",
                        labelEn = "Vertices",
                        color = Color(0xFFEF4444),
                        isSpanish = isSpanish,
                        modifier = Modifier.testTag("stat_vertices")
                    )

                    // Divider
                    Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color(0xFFE2E8F0)))

                    // Edges Count
                    StatCounter(
                        count = selectedShape.edges,
                        labelEs = "Aristas",
                        labelEn = "Edges",
                        color = Color(0xFF2563EB),
                        isSpanish = isSpanish,
                        modifier = Modifier.testTag("stat_edges")
                    )

                    // Divider
                    Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color(0xFFE2E8F0)))

                    // Faces Count
                    StatCounter(
                        count = selectedShape.faces,
                        labelEs = "Caras",
                        labelEn = "Faces",
                        color = Color(0xFF10B981),
                        isSpanish = isSpanish,
                        modifier = Modifier.testTag("stat_faces")
                    )
                }

                // Friendly description box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (isSpanish) selectedShape.descriptionEs else selectedShape.descriptionEn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        ),
                        color = Color(0xFF374151)
                    )
                }
            }
        }
    }

    // Interactive Help Dialog for kids
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = {
                Text(
                    text = if (isSpanish) "🤔 ¿Cómo funciona?" else "🤔 How does it work?",
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1E3A8A)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isSpanish) 
                            "👋 ¡Hola, pequeño explorador! Aquí puedes aprender cómo están hechas las figuras 3D:" 
                            else 
                            "👋 Hello, little explorer! Here is how you can study these 3D shapes:",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151)
                    )
                    
                    BulletPoint(
                        emoji = "🔄",
                        textEs = "Gira con tu dedo: Arrastra tu dedo por el visor para girar el cuerpo en todas direcciones.",
                        textEn = "Spin with your finger: Drag across the screen to rotate the shape in all directions.",
                        isSpanish = isSpanish
                    )
                    BulletPoint(
                        emoji = "🔴",
                        textEs = "Vértices: Son las 'esquinas' donde se juntan las líneas. ¡Se iluminan con esferas rojas!",
                        textEn = "Vertices: These are the 'corners' where lines meet. They glow with red spheres!",
                        isSpanish = isSpanish
                    )
                    BulletPoint(
                        emoji = "➖",
                        textEs = "Aristas: Son las 'líneas' o bordes que conectan las esquinas. ¡Se vuelven de color azul fuerte!",
                        textEn = "Edges: These are the 'lines' or borders connecting corners. They turn solid deep blue!",
                        isSpanish = isSpanish
                    )
                    BulletPoint(
                        emoji = "🟩",
                        textEs = "Caras: Son las superficies 'planas' o lados de la figura. ¡Se pintan de color verde!",
                        textEn = "Faces: These are the flat side surfaces of the shape. They are painted green!",
                        isSpanish = isSpanish
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showHelpDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isSpanish) "¡Entendido!" else "Got it!",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun ToggleButton(
    text: String,
    isSelected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    activeColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(72.dp)
            .clickable { onSelectedChange(!isSelected) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) bgColor else Color.White
        ),
        border = borderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) activeColor else Color(0xFFE2E8F0)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = activeColor,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 2.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                    fontSize = 13.sp,
                    lineHeight = 16.sp
                ),
                color = if (isSelected) activeColor else Color(0xFF4B5563),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = 
    androidx.compose.foundation.BorderStroke(width, color)

@Composable
fun StatCounter(
    count: Int,
    labelEs: String,
    labelEn: String,
    color: Color,
    isSpanish: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(color.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                .border(2.dp, color, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp
                ),
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isSpanish) labelEs else labelEn,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            color = Color(0xFF374151)
        )
    }
}

@Composable
fun BulletPoint(
    emoji: String,
    textEs: String,
    textEn: String,
    isSpanish: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$emoji ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 1.dp)
        )
        Text(
            text = if (isSpanish) textEs else textEn,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF4B5563)
        )
    }
}
