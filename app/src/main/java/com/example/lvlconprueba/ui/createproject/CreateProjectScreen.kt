package com.example.lvlconprueba.ui.createproject

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lvlconprueba.R
import com.example.lvlconprueba.ui.home.getIconRes
import java.text.SimpleDateFormat
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    onDismiss: () -> Unit,
    onProjectCreated: () -> Unit,
    viewModel: CreateProjectViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color(0xFF2D7DFF)
                        )
                    }
                    Text(
                        text = "Nuevo proyecto",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    when (uiState) {
                        is CreateProjectUiState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color(0xFF2D7DFF))
                            }
                        }
                        is CreateProjectUiState.Error -> {
                            val errorState = uiState as CreateProjectUiState.Error
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = errorState.message, color = MaterialTheme.colorScheme.error)
                                Button(onClick = { viewModel.dismissError() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                        else -> {
                            val comboData = (uiState as? CreateProjectUiState.Success)?.comboData
                            
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "Icono del proyecto",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFF1A1A1A)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(72.dp)
                                                .background(Color(0xFFF5F9FF), RoundedCornerShape(16.dp))
                                                .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(16.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                painter = painterResource(getIconRes(formState.iconoCodigo)),
                                                contentDescription = null,
                                                tint = Color.Unspecified,
                                                modifier = Modifier.size(36.dp)
                                            )
                                        }
                                        
                                        Row(
                                            modifier = Modifier.clickable {
                                                comboData?.iconos?.let { icons ->
                                                    if (icons.isNotEmpty()) {
                                                        val currentIndex = icons.indexOfFirst { it.codigo == formState.iconoCodigo }
                                                        val nextIndex = (currentIndex + 1) % icons.size
                                                        viewModel.onIconoChange(icons[nextIndex])
                                                    }
                                                }
                                            },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "Cambiar icono aleatorio",
                                                color = Color(0xFF9E9E9E),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = null,
                                                tint = Color(0xFF2D7DFF),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                                LvlTextField(
                                    value = formState.nombre,
                                    onValueChange = { viewModel.onNombreChange(it) },
                                    label = "Nombre del proyecto",
                                    placeholder = "Evento de aniversario"
                                )

                                LvlTextField(
                                    value = formState.descripcion,
                                    onValueChange = { viewModel.onDescripcionChange(it) },
                                    label = "Descripción",
                                    placeholder = "Descripción"
                                )

                                LvlDropdownField(
                                    label = "Categoría del proyecto",
                                    options = comboData?.categorias?.map { it.nombre ?: "" } ?: emptyList(),
                                    selectedOption = comboData?.categorias?.find { it.id == formState.categoriaId }?.nombre,
                                    onOptionSelected = { selected ->
                                        val categoria = comboData?.categorias?.find { it.nombre == selected }
                                        viewModel.onCategoriaChange(categoria)
                                    }
                                )

                                LvlDropdownField(
                                    label = "Estado del proyecto",
                                    options = comboData?.estados?.map { it.nombre ?: "" } ?: emptyList(),
                                    selectedOption = comboData?.estados?.find { it.codigo == formState.estadoCodigo }?.nombre,
                                    onOptionSelected = { selected ->
                                        val estado = comboData?.estados?.find { it.nombre == selected }
                                        viewModel.onEstadoChange(estado)
                                    }
                                )

                                DatePickerField(
                                    value = formState.fechaInicio,
                                    onValueChange = { viewModel.onFechaInicioChange(it) },
                                    label = "Fecha de inicio",
                                    placeholder = "Fecha de inicio"
                                )

                                DatePickerField(
                                    value = formState.fechaFin,
                                    onValueChange = { viewModel.onFechaFinChange(it) },
                                    label = "Fecha de finalización",
                                    placeholder = "Fecha de finalización"
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "¿Compartir con otros miembros?",
                                        color = Color(0xFF757575),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Switch(
                                        checked = formState.compartir,
                                        onCheckedChange = { viewModel.onCompartirChange(it) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF2D7DFF),
                                            uncheckedThumbColor = Color.White,
                                            uncheckedTrackColor = Color(0xFFE0E0E0),
                                            uncheckedBorderColor = Color.Transparent
                                        )
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
                }
                Button(
                    onClick = { viewModel.createProject(onProjectCreated) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D7DFF),
                        contentColor = Color.White
                    ),
                    enabled = formState.nombre.isNotBlank() &&
                              formState.categoriaId != null &&
                              formState.estadoCodigo != null &&
                              formState.iconoCodigo != null &&
                              uiState !is CreateProjectUiState.Creating
                ) {
                    if (uiState is CreateProjectUiState.Creating) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Crear proyecto",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LvlTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    trailingIcon: ImageVector? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder, color = Color(0xFF9E9E9E)) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedBorderColor = Color(0xFF2D7DFF),
            unfocusedBorderColor = Color(0xFFF0F0F0),
            focusedLabelColor = Color(0xFF2D7DFF),
            unfocusedLabelColor = Color(0xFF9E9E9E),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        trailingIcon = if (trailingIcon != null) {
            { Icon(trailingIcon, contentDescription = null, tint = Color(0xFF2D7DFF)) }
        } else null,
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LvlDropdownField(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text(label, color = Color(0xFF9E9E9E)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF2D7DFF)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color(0xFF2D7DFF),
                unfocusedBorderColor = Color(0xFFF0F0F0),
                focusedLabelColor = Color(0xFF2D7DFF),
                unfocusedLabelColor = Color(0xFF9E9E9E),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val selectedDate = remember(value) {
        if (value.isNotEmpty()) {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                sdf.parse(value)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    if (selectedDate != null) {
        datePickerState.selectedDateMillis = selectedDate.time
    }

    OutlinedTextField(
        value = if (value.isNotEmpty()) {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                val date = sdf.parse(value)
                val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                displayFormat.format(date ?: Date())
            } catch (e: Exception) {
                value
            }
        } else {
            value
        },
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        placeholder = { Text(placeholder, color = Color(0xFF9E9E9E)) },
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF2D7DFF)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedBorderColor = Color(0xFF2D7DFF),
            unfocusedBorderColor = Color(0xFFF0F0F0),
            focusedLabelColor = Color(0xFF2D7DFF),
            unfocusedLabelColor = Color(0xFF9E9E9E),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Date(millis)
                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                            sdf.timeZone = TimeZone.getTimeZone("UTC")
                            onValueChange(sdf.format(date))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
