package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.OmniPlayViewModel
import com.example.ui.theme.*

@Composable
fun StudioDetailsScreen(
    viewModel: OmniPlayViewModel,
    onNavigateToSchedule: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val studio by viewModel.selectedStudio.collectAsStateWithLifecycle()
    val stations by viewModel.stations.collectAsStateWithLifecycle()
    val selectedStation by viewModel.selectedStation.collectAsStateWithLifecycle()
    val filter by viewModel.selectedStationFilter.collectAsStateWithLifecycle()

    var isFavorite by remember { mutableStateOf(false) }

    if (studio == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select a studio first.", color = OnSurfaceText)
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                AsyncImage(
                    model = studio!!.imageUrl,
                    contentDescription = studio!!.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Navigation headers on image
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkBackground.copy(alpha = 0.6f))
                            .clickable { onNavigateBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = SecondaryCyan
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkBackground.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = SecondaryCyan
                        )
                    }
                }

                // Camera indicator
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd)
                        .background(DarkSurfaceLowest.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = OnSurfaceText,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "1 / 8",
                            color = OnSurfaceText,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title and heart fav button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = studio!!.name,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(SecondaryCyan.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .border(0.5.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(modifier = Modifier.size(6.dp).background(SecondaryCyan, CircleShape))
                                    Text("OPEN NOW", color = SecondaryCyan, style = MaterialTheme.typography.labelSmall)
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = TertiaryRose, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "${studio!!.rating} (1.2k reviews)",
                                    color = OnSurfaceVariantText,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceContainer)
                            .border(1.dp, if (isFavorite) TertiaryRose else OutlineVariantColor, CircleShape)
                            .clickable {
                                isFavorite = !isFavorite
                                Toast.makeText(context, if (isFavorite) "Added to Favorites!" else "Removed from Favorites", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) TertiaryRose else PrimaryPurple
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Address & Get Directions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = OnSurfaceVariantText,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "${studio!!.address} ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariantText
                    )
                    Text(
                        text = "GET DIRECTIONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryCyan,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { }
                    )
                }

                // Features Chips
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    studio!!.featuresList.forEach { feat ->
                        Box(
                            modifier = Modifier
                                .background(PrimaryPurple.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                                .border(0.5.dp, PrimaryPurple.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = feat.uppercase(),
                                color = PrimaryPurple,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                // Select Station Header & Filters
                Spacer(modifier = Modifier.height(28.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = null, tint = SecondaryCyan)
                        Text(
                            text = "SELECT STATION",
                            style = MaterialTheme.typography.headlineMedium,
                            color = OnSurfaceText,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("ALL", "PC", "CONSOLE").forEach { plat ->
                            val isSel = filter == plat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) PrimaryPurple.copy(alpha = 0.2f) else DarkSurface)
                                    .border(
                                        0.5.dp,
                                        if (isSel) PrimaryPurple else OutlineVariantColor,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.selectStationFilter(plat) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = plat,
                                    color = if (isSel) PrimaryPurple else OnSurfaceVariantText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                // Bento Grid of Stations
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val rows = stations.chunked(2)
                    rows.forEach { rowStations ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowStations.forEach { station ->
                                val isSelected = selectedStation?.id == station.id
                                val isBooked = station.status == "Booked"
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(130.dp)
                                        .border(
                                            1.dp,
                                            when {
                                                isSelected -> SecondaryCyan
                                                isBooked -> OutlineVariantColor.copy(alpha = 0.2f)
                                                else -> OutlineVariantColor.copy(alpha = 0.5f)
                                            },
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable(enabled = !isBooked) {
                                            viewModel.selectStation(station)
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = when {
                                            isSelected -> SecondaryCyan.copy(alpha = 0.05f)
                                            isBooked -> DarkSurfaceLowest.copy(alpha = 0.5f)
                                            else -> DarkSurfaceContainer
                                        }
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp)
                                    ) {
                                        // Header inside bento
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                if (station.type == "PC") Icons.Default.Settings else Icons.Default.PlayArrow,
                                                contentDescription = null,
                                                tint = if (isSelected) SecondaryCyan else OnSurfaceVariantText,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isBooked) TertiaryRose else SecondaryCyan)
                                            )
                                        }

                                        Column(
                                            modifier = Modifier.align(Alignment.BottomStart)
                                        ) {
                                            Text(
                                                text = station.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isBooked) OnSurfaceVariantText else OnSurfaceText
                                            )
                                            Text(
                                                text = if (isBooked) "Booked" else "Available",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (isBooked) TertiaryRoseDim else SecondaryCyan
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "RM${station.pricePerHour.toInt()} / hr",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = PrimaryPurple,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                            if (rowStations.size == 1) {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Reviews Section (Comms Log)
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "COMMS LOG",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceText
                    )
                    Text(
                        text = "View All Logs",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryCyan,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Review Card 1
                ReviewItem(
                    username = "@NeoStrike",
                    lvlText = "LVL 42 • 2 days ago",
                    stars = 5,
                    body = "Rigs are zero latency. Ping to local servers was solid 5ms. Aircon is freezing, just how I like it for a long grind session."
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Review Card 2
                ReviewItem(
                    username = "@ZeroFrame",
                    lvlText = "LVL 12 • 1 week ago",
                    stars = 4,
                    body = "Great atmosphere. The PS5 pods are spacious. Only issue is parking downstairs is a nightmare on weekends."
                )

                Spacer(modifier = Modifier.height(120.dp)) // room for sticky CTA
            }
        }

        // Sticky Bottom CTA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(DarkBackground.copy(alpha = 0.95f))
                .border(0.5.dp, OutlineVariantColor.copy(alpha = 0.3f))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (selectedStation != null) "Selected: ${selectedStation!!.name}" else "Choose Station",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariantText
                    )
                    Text(
                        text = if (selectedStation != null) "RM${selectedStation!!.pricePerHour.toInt()}/hr" else "--",
                        style = MaterialTheme.typography.headlineLarge,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        if (selectedStation != null) {
                            onNavigateToSchedule()
                        } else {
                            Toast.makeText(context, "Please select an available station pod!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .width(200.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(listOf(PrimaryPurple, SecondaryCyan)),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "INITIALIZE BOOKING",
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

@Composable
fun ReviewItem(
    username: String,
    lvlText: String,
    stars: Int,
    body: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, OutlineVariantColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceHighest)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = OnSurfaceText,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Column {
                        Text(username, fontWeight = FontWeight.Bold, color = OnSurfaceText, style = MaterialTheme.typography.bodyLarge)
                        Text(lvlText, color = OnSurfaceVariantText, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Row {
                    repeat(5) { i ->
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = if (i < stars) SecondaryCyan else OutlineVariantColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(body, color = OnSurfaceVariantText, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
