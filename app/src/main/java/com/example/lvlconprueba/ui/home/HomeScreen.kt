package com.example.lvlconprueba.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.lvlconprueba.R
import com.example.lvlconprueba.domain.model.Project
import com.example.lvlconprueba.ui.createproject.LvlDropdownField
import com.example.lvlconprueba.ui.createproject.LvlTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userName by viewModel.user.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val comboData by viewModel.comboData.collectAsState()
    val searchFilters by viewModel.searchFilters.collectAsState()

    var showCreateProjectDialog by remember { mutableStateOf(false) }
    var showSearchSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    if (showCreateProjectDialog) {
        com.example.lvlconprueba.ui.createproject.CreateProjectScreen(
            onDismiss = { showCreateProjectDialog = false },
            onProjectCreated = {
                showCreateProjectDialog = false
                viewModel.retryLoadData()
            }
        )
    }

    if (showSearchSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSearchSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = null
        ) {
            SearchBottomSheetContent(
                filters = searchFilters,
                comboData = comboData,
                onFiltersChange = viewModel::onSearchFiltersChange,
                onSearch = {
                    viewModel.applySearch()
                    showSearchSheet = false
                },
                onClear = {
                    viewModel.clearFilters()
                    showSearchSheet = false
                },
                onClose = { showSearchSheet = false }
            )
        }
    }

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateProjectDialog = true },
                containerColor = Color(0xFF2D7DFF),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 6.dp, end = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Project", modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is HomeUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF2D7DFF))
                    }
                }
                is HomeUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = (uiState as HomeUiState.Error).message,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(
                                onClick = { viewModel.retryLoadData() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D7DFF))
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                is HomeUiState.Success -> {
                    val projects = (uiState as HomeUiState.Success).projects
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item(span = { GridItemSpan(2) }) {
                            TopBarSection(
                                userName = userName,
                                userRole = userRole,
                                onProfileClick = onNavigateToProfile
                            )
                        }

                        item(span = { GridItemSpan(2) }) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        item(span = { GridItemSpan(2) }) {
                            SearchSection(onClick = { showSearchSheet = true })
                        }

                        item(span = { GridItemSpan(2) }) {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = "Tableros",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                ),
                                color = Color(0xFF1A1A1A)
                            )
                        }

                        items(projects) { project ->
                            ProjectCard(project)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBottomSheetContent(
    filters: SearchFilters,
    comboData: com.example.lvlconprueba.domain.model.ProjectComboData?,
    onFiltersChange: (SearchFilters) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Búsqueda avanzada",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = Color(0xFF1A1A1A)
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color(0xFF2D7DFF))
            }
        }

        LvlTextField(
            value = filters.codigo,
            onValueChange = { onFiltersChange(filters.copy(codigo = it)) },
            label = "Código del proyecto",
            placeholder = "ATA-2"
        )

        LvlTextField(
            value = filters.nombre,
            onValueChange = { onFiltersChange(filters.copy(nombre = it)) },
            label = "Nombre",
            placeholder = "Nombre"
        )

        LvlDropdownField(
            label = "Estado",
            options = comboData?.estados?.map { it.nombre ?: "" } ?: emptyList(),
            selectedOption = comboData?.estados?.find { it.codigo == filters.estadoCodigo }?.nombre,
            onOptionSelected = { selected ->
                val estado = comboData?.estados?.find { it.nombre == selected }
                onFiltersChange(filters.copy(estadoCodigo = estado?.codigo))
            }
        )

        LvlDropdownField(
            label = "Categoría",
            options = comboData?.categorias?.map { it.nombre ?: "" } ?: emptyList(),
            selectedOption = comboData?.categorias?.find { it.id == filters.categoriaId }?.nombre,
            onOptionSelected = { selected ->
                val categoria = comboData?.categorias?.find { it.nombre == selected }
                onFiltersChange(filters.copy(categoriaId = categoria?.id))
            }
        )

        LvlDropdownField(
            label = "Icono del proyecto",
            options = comboData?.iconos?.map { it.nombre ?: "" } ?: emptyList(),
            selectedOption = comboData?.iconos?.find { it.codigo == filters.iconoCodigo }?.nombre,
            onOptionSelected = { selected ->
                val icono = comboData?.iconos?.find { it.nombre == selected }
                onFiltersChange(filters.copy(iconoCodigo = icono?.codigo))
            }
        )

        LvlTextField(
            value = filters.fechaInicio ?: "",
            onValueChange = { onFiltersChange(filters.copy(fechaInicio = it)) },
            label = "Fecha de inicio",
            placeholder = "Fecha de inicio"
        )

        LvlTextField(
            value = filters.fechaFin ?: "",
            onValueChange = { onFiltersChange(filters.copy(fechaFin = it)) },
            label = "Fecha de finalización",
            placeholder = "Fecha de finalización"
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D7DFF))
            ) {
                Text(
                    "Buscar",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            TextButton(onClick = onClear) {
                Text(
                    "Limpiar",
                    color = Color(0xFF2D7DFF),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun TopBarSection(userName: String?, userRole: String?, onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onProfileClick)
        ) {
            AsyncImage(
                model = R.drawable.profile_ic,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = userName ?: "Miguel Liberato",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    ),
                    color = Color(0xFF232529)
                )
                Text(
                    text = userRole ?: "CEO LVL Consulting",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    ),
                    color = Color(0xFF8C8C8C)
                )
            }
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .border(1.dp, Color(0xFFE0E0E0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notifications",
                    tint = Color(0xFF2D7DFF)
                )
            }
        }
    }
}

@Composable
fun SearchSection(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF2D7DFF),
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = "Buscar en tableros",
                color = Color(0xFF9E9E9E),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                tint = Color(0xFF2D7DFF),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ProjectCard(project: Project) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.95f)
            .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 16.dp, 2.dp, 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFF5F9FF), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(getIconRes(project.iconoCodigo)),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = shortText(project.nombre),
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = project.codigo ?: "ATA-1",
                    fontSize = 14.sp,
                    color = Color(0xFF9E9E9E)
                )
            }

            StatusChip(
                text = project.estadoNombre ?: "PLANIFICACIÓN",
                background = getStatusColor(project.estadoCodigo)
            )
        }
    }
}

@Composable
fun StatusChip(text: String, background: Color) {
    Surface(
        color = background,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            color = Color(0xFF1A1A1A).copy(alpha = 0.8f)
        )
    }
}

fun shortText(text: String?): String {
    if (text.isNullOrBlank()) return "Proyecto"
    return if (text.length > 9) {
        text.take(9) + "..."
    } else {
        text
    }
}


fun getIconRes(codigo: String?): Int {
    return when (codigo) {
        "IC001" -> R.drawable.project_ic
        "IC002" -> R.drawable.suitcase_ic
        "IC003" -> R.drawable.phone_ic
        else -> R.drawable.project_ic
    }
}


fun getStatusColor(codigo: String?): Color {
    return when (codigo) {
        "ESPR001" -> Color(0xFFFEFCCE)
        "ESPR002" -> Color(0xFFE9E9E9)
        "ESPR003" -> Color(0xFFD7FFC4)
        "ESPR004" -> Color(0xFFCEE3FE)
        else -> Color.Transparent
    }
}
