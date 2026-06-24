package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.OmniPlayViewModel
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: OmniPlayViewModel,
    onNavigateToStudio: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToExplore: () -> Unit
) {
    val context = LocalContext.current
    val studios by viewModel.studios.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val name by viewModel.userFullName.collectAsStateWithLifecycle()
    val unreadNotifCount by viewModel.unreadNotificationCount.collectAsStateWithLifecycle()

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
                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "NEXUS PLAY",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariantText,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "G’day, $name",
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
                        contentDescription = "Notifications",
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
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search studios, games, gear...", color = OutlineColor) },
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
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
        }

        // Upcoming Booking Snippet
        val upcomingBooking = bookings.firstOrNull()
        if (upcomingBooking != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, SecondaryCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .clickable { onNavigateToExplore() },
                colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(TertiaryRose, CircleShape)
                            )
                            Text(
                                text = "UPCOMING SESSION",
                                style = MaterialTheme.typography.labelSmall,
                                color = TertiaryRoseDim,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${upcomingBooking.studioName} - ${upcomingBooking.stationName}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText
                        )
                        Text(
                            text = "Today • ${upcomingBooking.timeText}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceVariantText
                        )
                    }

                    // Scan payload QR representation
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(DarkSurfaceContainer, RoundedCornerShape(8.dp))
                            .border(1.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Scan Code",
                            tint = SecondaryCyan
                        )
                    }
                }
            }
        }

        // Horizontal Categories
        val categories = listOf(
            CategoryItem("All", "🎮"),
            CategoryItem("PS5", "🎮"),
            CategoryItem("PC", "💻"),
            CategoryItem("VR", "🕶️"),
            CategoryItem("Racing", "🏎️")
        )
        
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat.name
                Card(
                    modifier = Modifier
                        .border(
                            1.dp,
                            if (isSelected) SecondaryCyan else OutlineVariantColor,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { viewModel.selectCategory(cat.name) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) SecondaryCyan.copy(alpha = 0.15f) else DarkSurface
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(cat.icon, fontSize = 14.sp)
                        Text(
                            text = cat.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) SecondaryCyan else OnSurfaceText
                        )
                    }
                }
            }
        }

        // Promotions Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .clickable {
                    viewModel.setPromoCode("OMNIPLAY20")
                    viewModel.applyPromo()
                    Toast.makeText(context, "Promo OMNIPLAY20 Saved & Applied!", Toast.LENGTH_SHORT).show()
                },
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PrimaryPurple, PrimaryPurpleContainer)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    // Styled live/promo badge matching the "Live Event" badge of the design
                    Box(
                        modifier = Modifier
                            .background(TertiaryRose, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "LIVE OFFER",
                            color = TertiaryRoseDim,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Get 20% off your first 4-hour session!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .background(DarkBackground, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "CLAIM OFFER",
                            color = OnSurfaceText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Nearby Studios Horizontal Scroll
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nearby Studios",
                style = MaterialTheme.typography.headlineMedium,
                color = PrimaryPurple,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryCyan,
                modifier = Modifier.clickable { onNavigateToExplore() }
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(studios) { studio ->
                Card(
                    modifier = Modifier
                        .width(260.dp)
                        .border(1.dp, OutlineVariantColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable {
                            viewModel.selectStudio(studio)
                            onNavigateToStudio()
                        },
                    colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.9f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                        ) {
                            AsyncImage(
                                model = studio.imageUrl,
                                contentDescription = studio.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Star Rating badge
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(DarkBackground.copy(alpha = 0.85f), RoundedCornerShape(4.dp))
                                    .border(0.5.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = SecondaryCyan,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = studio.rating.toString(),
                                        color = OnSurfaceText,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = studio.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = OutlineColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = studio.distanceText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceVariantText
                                )
                            }
                        }
                    }
                }
            }
        }

        // Featured Equipment (Bento Style Grid)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Featured Equipment",
            style = MaterialTheme.typography.headlineMedium,
            color = PrimaryPurple,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .border(1.dp, OutlineVariantColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .clickable {
                        viewModel.selectCategory("PS5")
                        onNavigateToExplore()
                    },
                colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = SecondaryCyan,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopEnd)
                    )
                    Text(
                        text = "Next-Gen PS5\nStations",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceText,
                        modifier = Modifier.align(Alignment.BottomStart),
                        lineHeight = 20.sp
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .border(1.dp, OutlineVariantColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .clickable {
                        viewModel.selectCategory("PC")
                        onNavigateToExplore()
                    },
                colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopEnd)
                    )
                    Text(
                        text = "RTX 4090\nPC Rigs",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceText,
                        modifier = Modifier.align(Alignment.BottomStart),
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(96.dp)) // padding for bottom nav
    }
}

data class CategoryItem(val name: String, val icon: String)
