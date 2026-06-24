package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.OmniPlayViewModel
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    viewModel: OmniPlayViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("player.one@omniplay.gg") }
    var password by remember { mutableStateOf("secret123") }
    
    val authError by viewModel.authError.collectAsState()
    var showSocialDialog by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.clearAuthError()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Ambient glows
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.radialGradient(
                        colors = listOf(PrimaryPurple.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OmniPlay",
                style = MaterialTheme.typography.displayLarge,
                color = PrimaryPurple,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter the Nexus",
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariantText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))

            // Glass panel container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OutlineVariantColor, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "IDENTIFICATION",
                        style = MaterialTheme.typography.labelLarge,
                        color = OnSurfaceVariantText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Email or Username", color = OutlineColor) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = OutlineColor)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SecondaryCyan,
                            unfocusedBorderColor = OutlineVariantColor,
                            focusedTextColor = OnSurfaceText,
                            unfocusedTextColor = OnSurfaceText
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SECURITY KEY",
                            style = MaterialTheme.typography.labelLarge,
                            color = OnSurfaceVariantText,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Forgot Password?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryCyan,
                            modifier = Modifier.clickable { }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••", color = OutlineColor) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = OutlineColor)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SecondaryCyan,
                            unfocusedBorderColor = OutlineVariantColor,
                            focusedTextColor = OnSurfaceText,
                            unfocusedTextColor = OnSurfaceText
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    authError?.let { err ->
                        Text(
                            text = err,
                            color = TertiaryRose,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            if (viewModel.login(email, password)) {
                                onLoginSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(listOf(PrimaryPurple, SecondaryCyan)),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "INITIATE LINK",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = DarkSurfaceLowest
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = OutlineVariantColor
                        )
                        Text(
                            text = " EXTERNAL PROTOCOLS ",
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariantText,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = OutlineVariantColor
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        listOf("Discord" to "🎮", "Google" to "🌐", "Phone link" to "📱").forEach { (name, protocol) ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .border(1.dp, OutlineVariantColor, RoundedCornerShape(8.dp))
                                    .clickable {
                                        showSocialDialog = name
                                    },
                                colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(protocol, fontSize = 20.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Don't have an account? ", color = OnSurfaceVariantText)
                        Text(
                            text = "Register",
                            color = SecondaryCyan,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToRegister() }
                        )
                    }
                }
            }
        }
    }

    // Social accounts popup dialogue
    showSocialDialog?.let { protocol ->
        androidx.compose.ui.window.Dialog(onDismissRequest = { showSocialDialog = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SOCIAL LINK PROTOCOL",
                        style = MaterialTheme.typography.labelLarge,
                        color = SecondaryCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Log in securely using your $protocol account linked to OmniPlay.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariantText,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    val fakeAccount = when(protocol) {
                        "Discord" -> "omni_warrior#1337"
                        "Google" -> "omni.player@gmail.com"
                        else -> "+60 12-345 6789"
                    }
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.login(if (protocol == "Google") "omni.player@gmail.com" else "omni.warrior@omniplay.gg", "secret123")
                                showSocialDialog = null
                                onLoginSuccess()
                            }
                            .border(1.dp, OutlineVariantColor, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface)
                    ) {
                        Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = "Use linked ID:\n$fakeAccount",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceText,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedButton(
                        onClick = { showSocialDialog = null },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OnSurfaceVariantText)
                    ) {
                        Text("CANCEL")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
    viewModel: OmniPlayViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val authError by viewModel.authError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearAuthError()
    }

    val platformOptions = listOf("PS5", "PC", "VR", "Racing")
    val selectedPlats = remember { mutableStateListOf("PS5", "PC", "VR", "Racing") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "OmniPlay",
                style = MaterialTheme.typography.displayLarge,
                color = PrimaryPurple,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Initialize Your Profile",
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariantText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OutlineVariantColor, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "FULL NAME",
                        style = MaterialTheme.typography.labelLarge,
                        color = OnSurfaceVariantText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Player One", color = OutlineColor) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = OutlineColor)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SecondaryCyan,
                            unfocusedBorderColor = OutlineVariantColor,
                            focusedTextColor = OnSurfaceText,
                            unfocusedTextColor = OnSurfaceText
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "EMAIL ADDRESS",
                        style = MaterialTheme.typography.labelLarge,
                        color = OnSurfaceVariantText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("comms@omniplay.gg", color = OutlineColor) },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = OutlineColor)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SecondaryCyan,
                            unfocusedBorderColor = OutlineVariantColor,
                            focusedTextColor = OnSurfaceText,
                            unfocusedTextColor = OnSurfaceText
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "PASSWORD",
                                style = MaterialTheme.typography.labelLarge,
                                color = OnSurfaceVariantText,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("••••••••", color = OutlineColor) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "Password", tint = OutlineColor)
                                },
                                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SecondaryCyan,
                                    unfocusedBorderColor = OutlineVariantColor,
                                    focusedTextColor = OnSurfaceText,
                                    unfocusedTextColor = OnSurfaceText
                                ),
                                singleLine = true
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CONFIRM",
                                style = MaterialTheme.typography.labelLarge,
                                color = OnSurfaceVariantText,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("••••••••", color = OutlineColor) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "Password", tint = OutlineColor)
                                },
                                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SecondaryCyan,
                                    unfocusedBorderColor = OutlineVariantColor,
                                    focusedTextColor = OnSurfaceText,
                                    unfocusedTextColor = OnSurfaceText
                                ),
                                singleLine = true
                            )
                        }
                    }
                    
                    // Gaming preferences
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "GAMING PLATFORMS",
                        style = MaterialTheme.typography.labelLarge,
                        color = OnSurfaceVariantText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        platformOptions.forEach { plat ->
                            val isSelected = selectedPlats.contains(plat)
                            Card(
                                modifier = Modifier
                                    .clickable {
                                        if (isSelected) selectedPlats.remove(plat)
                                        else selectedPlats.add(plat)
                                    }
                                    .border(
                                        1.dp,
                                        if (isSelected) PrimaryPurple else OutlineVariantColor,
                                        RoundedCornerShape(20.dp)
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) PrimaryPurple.copy(alpha = 0.15f) else DarkSurfaceLowest
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = plat,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = if (isSelected) PrimaryPurple else OnSurfaceVariantText,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    authError?.let { err ->
                        Text(
                            text = err,
                            color = TertiaryRose,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            if (password != confirmPassword) {
                                // Set direct error state via ViewModel custom logic
                                viewModel.login(email) // fallback or trigger
                                // Better: let's perform password equality check
                                return@Button
                            }
                            if (viewModel.register(name, email, password, selectedPlats)) {
                                onRegisterSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(listOf(PrimaryPurple, SecondaryCyan)),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Create Account",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = DarkSurfaceLowest
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Already have an account? ", color = OnSurfaceVariantText)
                        Text(
                            text = "Log In",
                            color = SecondaryCyan,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToLogin() }
                        )
                    }
                }
            }
        }
    }
}
