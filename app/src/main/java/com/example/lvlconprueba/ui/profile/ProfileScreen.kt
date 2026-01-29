package com.example.lvlconprueba.ui.profile

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.lvlconprueba.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onNavigateToEditPhoto: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is ProfileUiState.Updated -> {
                Toast.makeText(context, (uiState as ProfileUiState.Updated).message, Toast.LENGTH_SHORT).show()
                viewModel.dismissMessage()
            }
            is ProfileUiState.Error -> {
                Toast.makeText(context, (uiState as ProfileUiState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.dismissMessage()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi perfil",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        color = Color(0xFF1A1A1A)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color(0xFF2D7DFF)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState is ProfileUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF2D7DFF)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { onNavigateToEditPhoto() }
                    ) {
                        AsyncImage(
                            model = if (formState.url.isNotEmpty()) formState.url else R.drawable.profile_ic,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(2.dp, Color(0xFFF0F0F0), CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                                .offset(x = (-4).dp, y = (-4).dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 4.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar foto",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF2D7DFF)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    ProfileTextField(
                        label = "Nombres",
                        value = formState.nombre,
                        onValueChange = { viewModel.onFieldChange(nombre = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileTextField(
                        label = "Apellidos",
                        value = formState.apellido,
                        onValueChange = { viewModel.onFieldChange(apellido = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileTextField(
                        label = "Nombre de empresa",
                        value = formState.empresa,
                        onValueChange = { viewModel.onFieldChange(empresa = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileTextField(
                        label = "Cargo en la empresa (Código)",
                        value = formState.cargoCodigo,
                        onValueChange = { viewModel.onFieldChange(cargoCodigo = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileTextField(
                        label = "Teléfono",
                        value = formState.telefono,
                        onValueChange = { viewModel.onFieldChange(telefono = it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileTextField(
                        label = "Correo electrónico",
                        value = formState.correo,
                        onValueChange = { viewModel.onFieldChange(correo = it) }
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = { viewModel.updateProfile() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = uiState !is ProfileUiState.Updating,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D7DFF))
                    ) {
                        if (uiState is ProfileUiState.Updating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                "Guardar",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFF2D7DFF),
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color(0xFF2D7DFF),
                unfocusedBorderColor = Color(0xFFF0F0F0),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )
    }
}
