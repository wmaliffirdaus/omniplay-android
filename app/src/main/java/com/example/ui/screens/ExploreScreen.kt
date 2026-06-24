package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.OmniPlayViewModel
import com.example.ui.theme.*

@Composable
fun ExploreScreen(
    viewModel: OmniPlayViewModel,
    onNavigateToStudio: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val studios by viewModel.studios.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val unreadNotifCount by viewModel.unreadNotificationCount.collectAsStateWithLifecycle()

    var showMapOverlay by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "FIND ARENAS",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariantText,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Explore Venues",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceText
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Notification Bell
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(DarkSurface)
                            .border(1.dp, OutlineVariantColor, CircleShape)
                            .clickable { onNavigateToNotifications() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Alert Center",
                            tint = SecondaryCyan
                        )
                        if (unreadNotifCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(TertiaryRose, CircleShape)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-4).dp, y = 4.dp)
                            )
                        }
                    }

                    // Profile Avatar Clickable
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, OutlineColor, CircleShape)
                            .clickable { onNavigateToProfile() }
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuD5gsuIq34JyHFb2nb-99LY0U5HaVc_vYwa5cbP2YefB-Rl5oT7310ztnhHnMBe0L1gtr537w1w1TsQvwXrECV7FcatoGyksxymwWh8Z8fkYzrRzNRNbHliIPOc-MgSM_rULlHLX-0o8FETIIm4UW3dTv_IwmV3Gc6pnrV4WGqFo8hP4Sm1it4j1VARBa1a1dZCwZHgpqaLep0LfzLikom3TKQZXWnVogEx-Kl1mZtP936s_lUspc6gzdjDkUOkSenMXM3AWkfwFw",
                            contentDescription = "Profile Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Search input with neon effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search venues, gear, or games...", color = OutlineColor) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = OutlineColor)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SecondaryCyan,
                        unfocusedBorderColor = OutlineVariantColor,
                        focusedTextColor = OnSurfaceText,
                        unfocusedTextColor = OnSurfaceText,
                        focusedContainerColor = DarkSurfaceContainer,
                        unfocusedContainerColor = DarkSurfaceContainer
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // Filter chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    AssistChip(
                        onClick = { },
                        label = { Text("ALL FILTERS", color = SecondaryCyan) },
                        leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null, tint = SecondaryCyan) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = SecondaryCyan.copy(alpha = 0.1f))
                    )
                }
                items(listOf("Price", "Distance", "Rating", "Availability")) { filter ->
                    AssistChip(
                        onClick = { },
                        label = { Text(filter, color = OnSurfaceVariantText) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = DarkSurface)
                    )
                }
            }

            // Studios List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(studios) { studio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, OutlineVariantColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.selectStudio(studio)
                                onNavigateToStudio()
                            },
                        colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.85f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column {
                            // Studio Image Header
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            ) {
                                AsyncImage(
                                    model = studio.imageUrl,
                                    contentDescription = studio.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Live slots open
                                Box(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .align(Alignment.TopEnd)
                                        .background(DarkBackground.copy(alpha = 0.85f), RoundedCornerShape(20.dp))
                                        .border(0.5.dp, TertiaryRose.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .background(TertiaryRose, CircleShape)
                                        )
                                        Text(
                                            text = "3 SLOTS OPEN",
                                            color = TertiaryRoseDim,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // Studio Content
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = studio.name,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurfaceText,
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier
                                            .background(SecondaryCyan.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = null,
                                            tint = SecondaryCyan,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = studio.rating.toString(),
                                            color = SecondaryCyan,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = studio.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceVariantText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                // Tags / Features
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    studio.featuresList.take(3).forEach { feature ->
                                        Box(
                                            modifier = Modifier
                                                .background(DarkSurfaceHighest.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                                                .border(0.5.dp, OutlineVariantColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = feature.uppercase(),
                                                color = OnSurfaceVariantText,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = OutlineVariantColor.copy(alpha = 0.4f))
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "STARTING FROM",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = OnSurfaceVariantText,
                                            fontSize = 9.sp
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "RM${studio.startingPrice.toInt()}",
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = PrimaryPurple,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "/hr",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = OnSurfaceVariantText
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.selectStudio(studio)
                                            onNavigateToStudio()
                                        },
                                        modifier = Modifier.height(40.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        contentPadding = PaddingValues()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    Brush.linearGradient(listOf(PrimaryPurple, SecondaryCyan)),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(horizontal = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (studio.category == "VR") "View" else "Book Now",
                                                color = DarkSurfaceLowest,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(96.dp))
        }
    }

    // Floating Map view toggle
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 96.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .border(1.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(30.dp))
                .clickable { showMapOverlay = !showMapOverlay },
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceHigh.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(30.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = SecondaryCyan
                )
                Text(
                    text = "Map View",
                    style = MaterialTheme.typography.labelLarge,
                    color = SecondaryCyan,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Interactive Cyber Map overlay
    AnimatedVisibility(
        visible = showMapOverlay,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground.copy(alpha = 0.95f))
                .clickable { showMapOverlay = false },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = SecondaryCyan,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "OMNIPLAY NEURAL MAP",
                    style = MaterialTheme.typography.headlineMedium,
                    color = PrimaryPurple,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tracking nearby high-latency nodes & pro-pods in real-time.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariantText,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                // Show radar locations
                studios.forEach { studio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .border(1.dp, SecondaryCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest.copy(alpha = 0.8f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("🎯", fontSize = 16.sp)
                                Column {
                                    Text(studio.name, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                                    Text(studio.distanceText, color = OnSurfaceVariantText, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            Text("ACTIVE", color = SecondaryCyan, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text("Tap anywhere to close", color = OutlineColor, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
