package org.jct.iedbs1.screens.report

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jct.iedbs1.screens.home.HomeViewModel
import org.jct.iedbs1.ui.theme.AppDimens
import org.jetbrains.compose.resources.Font
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Black
import votacion_iedbs1.composeapp.generated.resources.Montserrat_Medium
import votacion_iedbs1.composeapp.generated.resources.Montserrat_SemiBold
import votacion_iedbs1.composeapp.generated.resources.Res
import kotlin.random.Random

@Composable
fun ReportScreen(viewModel: HomeViewModel, onNavigateBack: () -> Unit) {
    val uiState by viewModel.reportUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadReportData()
    }

    Scaffold(
        topBar = {
            StyledHeader(title = "REPORTES GENERALES", onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = paddingValues
            ) {
                item {
                    Spacer(Modifier.height(8.dp))
                    ElectionTitle()
                }
                item {
                    CustomPieChartCard(
                        title = "Sociedad con mayor cantidad de cargos electos",
                        data = uiState.chartData.ganadoresPorGrupo,
                        dataLabel = "Candidatos"
                    )
                }
                item {
                    CustomPieChartCard(
                        title = "Género con mayor cantidad de cargos electos",
                        data = uiState.chartData.ganadoresPorGenero,
                        dataLabel = "Candidatos"
                    )
                }
                item {
                    CustomBarChartCard(
                        title = "Participación de votantes por cargo electo",
                        data = uiState.chartData.participacionPorCargo,
                        //dataLabel = "Votos"
                          dataLabel = ""
                    )
                }
                item {
                    CustomBarChartCard(
                        title = "Número de candidatos por cargo",
                        data = uiState.chartData.postulantesPorCargo,
                        //dataLabel = "Candidatos",
                        dataLabel = ""

                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StyledHeader(title: String, onNavigateBack: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            ),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 8.dp,
        tonalElevation = 3.dp
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = AppDimens.title,
                    fontFamily = FontFamily(Font(Res.font.Montserrat_Black))
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ElectionTitle() {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1000))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ELECCIONES DE LIDERES 2026-2027",
                fontWeight = FontWeight.Bold,
                fontSize = AppDimens.title,
                textAlign = TextAlign.Center,
                lineHeight = AppDimens.headline,
                fontFamily = FontFamily(Font(Res.font.Montserrat_Black))
            )
            Text(
                text = "Iglesia Evangelica de Dios Boliviana - Santiago I",
                fontSize = AppDimens.subtitle,
                textAlign = TextAlign.Center,
                lineHeight = AppDimens.caption,
                fontFamily = FontFamily(Font(Res.font.Montserrat_SemiBold))
            )
            Text(
                text = "IEDBS-I ©2025 Area AudioVisual",
                fontSize = AppDimens.tiny,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(Res.font.Montserrat_Medium))
            )
        }
    }
}

@Composable
private fun CustomPieChartCard(title: String, data: Map<String, Int>, dataLabel: String) {
    val total = data.values.sum().toFloat()
    if (total == 0f) return

    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    val animationProgress by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(1000, 400)
    )

    val colors = remember { generateColors(data.size) }
    val slices = data.entries.mapIndexed { index, entry ->
        val percentage = (entry.value / total)
        Slice(entry.key, percentage, colors[index], entry.value)
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, fontSize = AppDimens.headline)
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier.size(150.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        var startAngle = -90f
                        val totalAnimatedAngle = 360 * animationProgress
                        var drawnAngle = 0f

                        slices.forEach { slice ->
                            val sweep = slice.percentage * 360f
                            val sweepToDraw = if (drawnAngle + sweep > totalAnimatedAngle) {
                                totalAnimatedAngle - drawnAngle
                            } else {
                                sweep
                            }

                            if (sweepToDraw > 0) {
                                drawArc(
                                    color = slice.color,
                                    startAngle = startAngle,
                                    sweepAngle = sweepToDraw,
                                    useCenter = true
                                )
                            }
                            drawnAngle += sweep
                            startAngle += sweep
                        }
                    }
                }
                Legend(slices = slices, dataLabel = dataLabel)
            }
        }
    }
}

@Composable
private fun CustomBarChartCardold(title: String, data: List<Pair<String, Int>>, dataLabel: String) {
    if (data.isEmpty()) return

    val maxValue = data.maxOfOrNull { it.second }?.toFloat() ?: 0f
    if (maxValue == 0f) return

    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    val colors = remember { generateColors(data.size) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, fontSize = AppDimens.headline)
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                data.forEachIndexed { index, pair ->
                    val animatedHeightFactor by animateFloatAsState(
                        targetValue = if (animationPlayed) pair.second / maxValue else 0f,
                        animationSpec = tween(durationMillis = 1000, delayMillis = 200 + index * 100)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("${pair.second} $dataLabel", fontSize = AppDimens.caption, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height((animatedHeightFactor * 150).dp)
                                .background(colors[index])
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = pair.first,
                            fontSize = AppDimens.tiny,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            lineHeight = AppDimens.caption
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomBarChartCard1(
    title: String,
    data: List<Pair<String, Int>>,
    dataLabel: String
) {
    if (data.isEmpty()) return

    val maxValue = data.maxOfOrNull { it.second }?.toFloat() ?: 0f
    if (maxValue == 0f) return

    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animationPlayed = true }

    val colors = remember { generateColors(data.size) }

    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = AppDimens.headline
            )

            Spacer(Modifier.height(16.dp))

            Column {

                // --- SCROLL HORIZONTAL DEL GRÁFICO ---
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .height(220.dp)
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {

                    data.forEachIndexed { index, pair ->

                        val animatedHeightFactor by animateFloatAsState(
                            targetValue = if (animationPlayed) pair.second / maxValue else 0f,
                            animationSpec = tween(
                                durationMillis = 1000,
                                delayMillis = 200 + index * 100
                            )
                        )

                        Column(
                            modifier = Modifier
                                .width(70.dp)
                                .padding(horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            // Valor
                            Text(
                                "${pair.second} $dataLabel",
                                fontSize = AppDimens.caption,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.height(4.dp))

                            // Barra
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((animatedHeightFactor * 150).dp)
                                    .background(colors[index])
                            )

                            Spacer(Modifier.height(4.dp))

                            // Etiqueta ROTADA (vertical)
                            Box(
                                modifier = Modifier.height(60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = pair.first,
                                    fontSize = AppDimens.tiny,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    lineHeight = AppDimens.caption,
                                    modifier = Modifier.rotate(-90f) // ← ROTACIÓN VERTICAL
                                )
                            }
                        }
                    }
                }

                // --- SCROLLBAR MULTIPLATAFORMA ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color.Gray.copy(alpha = 0.2f))
                ) {
                    val scrollPercent =
                        scrollState.value.toFloat() / scrollState.maxValue.coerceAtLeast(1)

                    val indicatorWidth = 80.dp // Tamaño visible del scrollbar

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset(x = (scrollPercent * (scrollState.maxValue)).dp)
                            .width(indicatorWidth)
                            .height(6.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomBarChartCard(
    title: String,
    data: List<Pair<String, Int>>,
    dataLabel: String
) {
    if (data.isEmpty()) return

    val maxValue = data.maxOfOrNull { it.second }?.toFloat() ?: 0f
    if (maxValue == 0f) return

    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animationPlayed = true }

    val colors = remember { generateColors(data.size) }
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = AppDimens.headline
            )

            Spacer(Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {

                // --- Fade izquierdo ---
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .width(24.dp)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface,
                                    Color.Transparent
                                )
                            )
                        )
                )

                // --- Fade derecho ---
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .width(24.dp)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                )

                Column {

                    // --- SCROLL HORIZONTAL real del gráfico ---
                    Row(
                        modifier = Modifier
                            .horizontalScroll(scrollState)
                            .height(220.dp)
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {

                        data.forEachIndexed { index, pair ->

                            val animatedHeightFactor by animateFloatAsState(
                                targetValue = if (animationPlayed) pair.second / maxValue else 0f,
                                animationSpec = tween(
                                    durationMillis = 1000,
                                    delayMillis = 200 + index * 100
                                )
                            )

                            Column(
                                modifier = Modifier
                                    .width(70.dp)
                                    .padding(horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    "${pair.second} $dataLabel",
                                    fontSize = AppDimens.caption,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(Modifier.height(4.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height((animatedHeightFactor * 150).dp)
                                        .background(colors[index])
                                )

                                Spacer(Modifier.height(4.dp))

                                // Etiqueta vertical
                                Box(
                                    modifier = Modifier.height(60.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = pair.first,
                                        fontSize = AppDimens.tiny,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        lineHeight = AppDimens.caption,
                                        modifier = Modifier.rotate(-90f)
                                    )
                                }
                            }
                        }
                    }

                    // --- Scrollbar multiplataforma ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .background(Color.Gray.copy(alpha = 0.2f))
                    ) {
                        val scrollPercent =
                            scrollState.value.toFloat() / scrollState.maxValue.coerceAtLeast(1)

                        val indicatorWidth = 80.dp

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .offset(x = (scrollPercent * (scrollState.maxValue)).dp)
                                .width(indicatorWidth)
                                .height(6.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(3.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun Legend(slices: List<Slice>, dataLabel: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        slices.forEach { slice ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(12.dp).background(slice.color, CircleShape))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${slice.label} (${slice.value} $dataLabel)",
                    fontSize = AppDimens.caption
                )
            }
        }
    }
}

private data class Slice(val label: String, val percentage: Float, val color: Color, val value: Int)

private fun generateColors(count: Int): List<Color> {
    val baseColors = listOf(
        Color(0xFFD81159), Color(0xFFFBB13C), Color(0xFF8F2D56), Color(0xFF218380),
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF673AB7),
        Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4), Color(0xFF00BCD4),
        Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
        Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800), Color(0xFF795548)
    )
    if (count <= baseColors.size) {
        return baseColors.take(count)
    }
    val random = Random(123) // Seed for consistent "random" colors
    return baseColors + (1..(count - baseColors.size)).map {
        Color(
            red = random.nextFloat(),
            green = random.nextFloat(),
            blue = random.nextFloat(),
            alpha = 1.0f
        )
    }
}
