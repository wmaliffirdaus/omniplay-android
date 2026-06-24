package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.OmniPlayViewModel
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: OmniPlayViewModel,
    onLogout: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val name by viewModel.userFullName.collectAsStateWithLifecycle()
    val email by viewModel.userEmail.collectAsStateWithLifecycle()
    val level by viewModel.userLevel.collectAsStateWithLifecycle()
    val points by viewModel.userPoints.collectAsStateWithLifecycle()
    val hours by viewModel.userHours.collectAsStateWithLifecycle()
    val platforms by viewModel.selectedPlatforms.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecondaryCyan)
            }
            Text(
                text = "Neural Profile",
                style = MaterialTheme.typography.headlineLarge,
                color = OnSurfaceText,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onNavigateToNotifications) {
                Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = SecondaryCyan)
            }
        }

        // Avatar & Identification Details
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(2.dp, PrimaryPurple, CircleShape)
                    .background(DarkSurfaceLowest)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuD5gsuIq34JyHFb2nb-99LY0U5HaVc_vYwa5cbP2YefB-Rl5oT7310ztnhHnMBe0L1gtr537w1w1TsQvwXrECV7FcatoGyksxymwWh8Z8fkYzrRzNRNbHliIPOc-MgSM_rULlHLX-0o8FETIIm4UW3dTv_IwmV3Gc6pnrV4WGqFo8hP4Sm1it4j1VARBa1a1dZCwZHgpqaLep0LfzLikom3TKQZXWnVogEx-Kl1mZtP936s_lUspc6gzdjDkUOkSenMXM3AWkfwFw",
                    contentDescription = "Avatar picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceText
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariantText
            )

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(PrimaryPurple.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .border(0.5.dp, PrimaryPurple.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "LVL $level PRO MEMBER",
                    color = PrimaryPurple,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Stats Grid (Bento Boxes)
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OutlineVariantColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("LEVEL PROGRESS", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText, fontWeight = FontWeight.Bold)
                        Text("12,450 / 15,000 EXP", style = MaterialTheme.typography.labelLarge, color = SecondaryCyan, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { 0.83f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = SecondaryCyan,
                        trackColor = DarkBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .border(1.dp, OutlineVariantColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.8f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                         Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("GAMING HOURS", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText, fontSize = 9.sp)
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(16.dp))
                        }
                        Text("$hours hrs", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .border(1.dp, OutlineVariantColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.8f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("LOYALTY POINTS", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText, fontSize = 9.sp)
                            Icon(Icons.Default.Star, contentDescription = null, tint = TertiaryRose, modifier = Modifier.size(16.dp))
                        }
                        Text("$points pts", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                    }
                }
            }

            // Platform Preferences chips bento box
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OutlineVariantColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PREFERRED PLATFORMS", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        platforms.forEach { plat ->
                            Box(
                                modifier = Modifier
                                    .background(DarkBackground, RoundedCornerShape(20.dp))
                                    .border(0.5.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(plat, color = SecondaryCyan, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }

            // Settings options list
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "SETTINGS & COGNITIVE CONTROLS",
                style = MaterialTheme.typography.headlineMedium,
                color = PrimaryPurple,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    ProfileSettingsItem("Security Protocols", Icons.Default.Lock, "Manage access keys & MFA"),
                    ProfileSettingsItem("Account Details", Icons.Default.Person, "Update email, phone, avatar"),
                    ProfileSettingsItem("Billing Protocols", Icons.Default.DateRange, "Manage saved credit cards & wallets"),
                    ProfileSettingsItem("Language & Environment", Icons.Default.Info, "English (Global)")
                ).forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.5.dp, OutlineVariantColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .clickable {
                                Toast.makeText(context, "${item.title} settings loaded", Toast.LENGTH_SHORT).show()
                            },
                        colors = CardDefaults.cardColors(containerColor = DarkSurface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(DarkSurfaceLowest, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(item.icon, contentDescription = null, tint = SecondaryCyan)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                                Text(item.subtitle, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariantText)
                            }
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = OutlineColor)
                        }
                    }
                }

                // Active Node Status
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, SecondaryCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(SecondaryCyan, CircleShape)
                            )
                            Text("ACTIVE SYSTEM CONNECTION", style = MaterialTheme.typography.labelSmall, color = OnSurfaceText, fontWeight = FontWeight.Bold)
                        }

                        Text("LATENCY 4MS", style = MaterialTheme.typography.labelSmall, color = SecondaryCyan, fontWeight = FontWeight.Bold)
                    }
                }

                // Log out button
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(1.dp, TertiaryRose.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text("LOG OUT SYSTEM", color = TertiaryRoseDim, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}

data class ProfileSettingsItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val subtitle: String
)
