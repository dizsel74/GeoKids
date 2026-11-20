package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    showHelpDialog: Boolean = false,
    onDismissHelpDialog: () -> Unit = {},
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
                                text = if (isSpanish) "Elige tu figura geométrica:" else "Choose 3D shape",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
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
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1A1C1E)
                                    )
                                )
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            showTranslation = true
                                            kotlinx.coroutines.delay(2000)
                                            showTranslation = false
                                        }
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Translate,
                                        contentDescription = if (isSpanish) "Traducir" else "Translate",
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(18.dp)
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
                                    fontWeight = if (selectedShape == shape) FontWeight.SemiBold else FontWeight.Medium,
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

        // Interactive Shape Statistics Card (Tap Vertices, Edges, or Faces to toggle)
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = if (isSpanish) "Datos de la Figura" else "Shape Statistics",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1A1C1E)
                        )
                    )
                    Text(
                        text = if (isSpanish) "Toca cada botón para mostrar u ocultar" else "Tap each button to show or hide",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        ),
                        color = Color(0xFF64748B)
                    )
                }

                // Interactive Stats Toggle Counters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Vertices Toggle Counter
                    StatCounter(
                        count = selectedShape.vertices,
                        labelEs = "Vértices",
                        labelEn = "Vertices",
                        subLabelEs = "Esquinas",
                        subLabelEn = "Corners",
                        isSelected = showVertices,
                        onToggle = { showVertices = it },
                        activeColor = Color(0xFFEF4444),
                        bgColor = Color(0xFFFEF2F2),
                        isSpanish = isSpanish,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("stat_vertices")
                    )

                    // Edges Toggle Counter
                    StatCounter(
                        count = selectedShape.edges,
                        labelEs = "Aristas",
                        labelEn = "Edges",
                        subLabelEs = "Líneas",
                        subLabelEn = "Lines",
                        isSelected = showEdges,
                        onToggle = { showEdges = it },
                        activeColor = Color(0xFF2563EB),
                        bgColor = Color(0xFFEFF6FF),
                        isSpanish = isSpanish,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("stat_edges")
                    )

                    // Faces Toggle Counter
                    StatCounter(
                        count = selectedShape.faces,
                        labelEs = "Caras",
                        labelEn = "Faces",
                        subLabelEs = "Lados",
                        subLabelEn = "Sides",
                        isSelected = showFaces,
                        onToggle = { showFaces = it },
                        activeColor = Color(0xFF10B981),
                        bgColor = Color(0xFFECFDF5),
                        isSpanish = isSpanish,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("stat_faces")
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
                            fontWeight = FontWeight.Normal,
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
            onDismissRequest = onDismissHelpDialog,
            containerColor = Color(0xFF1D1D1B),
            titleContentColor = Color(0xFFFFFFFF),
            textContentColor = Color(0xFFFFFFFF),
            title = {
                Text(
                    text = if (isSpanish) "¿Cómo funciona?" else "How does it work?",
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFFFFFF)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    BulletPoint(
                        icon = Icons.Default.Autorenew,
                        iconTint = Color(0xFF93C5FD),
                        textEs = "Gira con tu dedo: Arrastra tu dedo por el visor para girar el cuerpo en todas direcciones.",
                        textEn = "Spin with your finger: Drag across the screen to rotate the shape in all directions.",
                        isSpanish = isSpanish
                    )
                    BulletPoint(
                        icon = Icons.Default.Circle,
                        iconTint = Color(0xFFEF4444),
                        textEs = "Vértices: Son las 'esquinas' donde se juntan las líneas. ¡Se iluminan con esferas rojas!",
                        textEn = "Vertices: These are the 'corners' where lines meet. They glow with red spheres!",
                        isSpanish = isSpanish
                    )
                    BulletPoint(
                        icon = Icons.Default.HorizontalRule,
                        iconTint = Color(0xFF60A5FA),
                        textEs = "Aristas: Son las 'líneas' o bordes que conectan las esquinas. ¡Se vuelven de color azul fuerte!",
                        textEn = "Edges: These are the 'lines' or borders connecting corners. They turn solid deep blue!",
                        isSpanish = isSpanish
                    )
                    BulletPoint(
                        icon = Icons.Default.Square,
                        iconTint = Color(0xFF10B981),
                        textEs = "Caras: Son las superficies 'planas' o lados de la figura. ¡Se pintan de color verde!",
                        textEn = "Faces: These are the flat side surfaces of the shape. They are painted green!",
                        isSpanish = isSpanish
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismissHelpDialog,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        contentColor = Color(0xFFFFFFFF)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isSpanish) "¡Entendido!" else "Got it!",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) =
    androidx.compose.foundation.BorderStroke(width, color)

@Composable
fun StatCounter(
    count: Int,
    labelEs: String,
    labelEn: String,
    subLabelEs: String,
    subLabelEn: String,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit,
    activeColor: Color,
    bgColor: Color,
    isSpanish: Boolean,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) bgColor else Color(0xFFF1F5F9),
        label = "stat_container_color"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else Color(0xFF64748B),
        label = "stat_border_color"
    )
    val primaryContentColor by animateColorAsState(
        targetValue = if (isSelected) activeColor else Color(0xFF475569),
        label = "stat_primary_content_color"
    )
    val secondaryContentColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF374151) else Color(0xFF475569),
        label = "stat_secondary_content_color"
    )

    Card(
        onClick = { onToggle(!isSelected) },
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = borderStroke(
            width = 2.dp,
            color = borderColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            // Active / inactive status badge in top-right corner
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) activeColor else Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) activeColor else Color(0xFF64748B),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = if (isSpanish) "Activado" else "Enabled",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 28.sp,
                        lineHeight = 32.sp
                    ),
                    color = primaryContentColor
                )
                Text(
                    text = if (isSpanish) labelEs else labelEn,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    ),
                    color = secondaryContentColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "(${if (isSpanish) subLabelEs else subLabelEn})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    ),
                    color = secondaryContentColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun BulletPoint(
    icon: ImageVector,
    iconTint: Color,
    textEs: String,
    textEn: String,
    isSpanish: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(18.dp)
        )
        Text(
            text = if (isSpanish) textEs else textEn,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            color = Color(0xFFFFFFFF)
        )
    }
}
