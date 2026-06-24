package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.OmniPlayViewModel
import com.example.ui.theme.*

@Composable
fun BookingsScreen(
    viewModel: OmniPlayViewModel,
    onNavigateToExplore: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val unreadNotifCount by viewModel.unreadNotificationCount.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(0) } // 0 = Upcoming, 1 = Completed, 2 = Cancelled
    var activeQrBooking by remember { mutableStateOf<com.example.data.Booking?>(null) }

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
                        text = "SESSION HISTORY",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariantText,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "My Bookings",
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

            // Tabs Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf("Upcoming", "Completed", "Cancelled").forEachIndexed { index, label ->
                    val isActive = selectedTab == index
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = index },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isActive) SecondaryCyan else OnSurfaceVariantText,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(if (isActive) SecondaryCyan else Color.Transparent)
                        )
                    }
                }
            }

            // Filter Tabs logic
            val upcomingBookings = bookings.filter { !it.isCancelled }
            val cancelledBookings = bookings.filter { it.isCancelled }
            val completedBookings = emptyList<com.example.data.Booking>() // Treated as completed in past logs

            var bookingToCancel by remember { mutableStateOf<com.example.data.Booking?>(null) }
            var bookingToEdit by remember { mutableStateOf<com.example.data.Booking?>(null) }

            val currentList = when (selectedTab) {
                0 -> upcomingBookings
                1 -> completedBookings
                else -> cancelledBookings
            }

            if (selectedTab == 0) {
                if (currentList.isEmpty()) {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = OutlineColor,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Active Sessions",
                            style = MaterialTheme.typography.headlineMedium,
                            color = OnSurfaceText,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Find high-performance rigs and initialize your gaming session now.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnSurfaceVariantText,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { onNavigateToExplore() },
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryCyanContainer)
                        ) {
                            Text("EXPLORE VENUES", color = DarkBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(currentList) { booking ->
                            BookingCard(
                                booking = booking,
                                onViewQr = { activeQrBooking = booking },
                                onEdit = { bookingToEdit = booking },
                                onCancel = { bookingToCancel = booking }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(96.dp))
                        }
                    }
                }
            } else if (selectedTab == 2) {
                if (currentList.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No Cancelled Sessions",
                            style = MaterialTheme.typography.headlineMedium,
                            color = OnSurfaceVariantText,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Once you cancel a booking, it will appear here for your history logs.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OutlineColor,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(currentList) { booking ->
                            CancelledBookingCard(booking = booking)
                        }
                        item {
                            Spacer(modifier = Modifier.height(96.dp))
                        }
                    }
                }
            } else {
                // Completed static placeholders
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No Completed Sessions",
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnSurfaceVariantText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Once you finish sessions, they'll appear here for your history logs.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OutlineColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Reschedule Dialog Flow
            bookingToEdit?.let { booking ->
                var selectedDateOption by remember { mutableStateOf("Oct 25, 2026") }
                var selectedTimeOption by remember { mutableStateOf("12:00 PM - 02:00 PM") }
                val dateOptions = listOf("Oct 25, 2026", "Oct 26, 2026", "Oct 27, 2026")
                val timeOptions = listOf("10:00 AM - 12:00 PM", "12:00 PM - 02:00 PM", "02:00 PM - 04:00 PM", "06:00 PM - 08:00 PM")

                Dialog(onDismissRequest = { bookingToEdit = null }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(1.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "RESCHEDULE SESSION",
                                style = MaterialTheme.typography.labelMedium,
                                color = SecondaryCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Reschedule your rig at ${booking.studioName} (${booking.stationName})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceText
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Text("SELECT DATE", style = MaterialTheme.typography.labelSmall, color = OutlineColor)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                dateOptions.forEach { d ->
                                    val isSel = selectedDateOption == d
                                    Card(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedDateOption = d }
                                            .border(1.dp, if (isSel) SecondaryCyan else OutlineVariantColor, RoundedCornerShape(8.dp)),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSel) SecondaryCyan.copy(alpha = 0.1f) else DarkSurface
                                        )
                                    ) {
                                        Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                                            Text(d.substringBefore(","), style = MaterialTheme.typography.labelMedium, color = if (isSel) SecondaryCyan else OnSurfaceVariantText, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text("SELECT TIME", style = MaterialTheme.typography.labelSmall, color = OutlineColor)
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                timeOptions.forEach { t ->
                                    val isSel = selectedTimeOption == t
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedTimeOption = t }
                                            .border(1.dp, if (isSel) SecondaryCyan else OutlineVariantColor, RoundedCornerShape(8.dp)),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSel) SecondaryCyan.copy(alpha = 0.1f) else DarkSurface
                                        )
                                    ) {
                                        Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.CenterStart) {
                                            Text(t, style = MaterialTheme.typography.bodyMedium, color = if (isSel) SecondaryCyan else OnSurfaceText, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { bookingToEdit = null },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = OnSurfaceVariantText)
                                ) {
                                    Text("CANCEL")
                                }
                                Button(
                                    onClick = {
                                        viewModel.rescheduleBooking(booking.id, selectedDateOption, selectedTimeOption)
                                        bookingToEdit = null
                                    },
                                    modifier = Modifier.weight(1.5f),
                                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryCyanContainer)
                                ) {
                                    Text("CONFIRM", color = DarkBackground, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Cancellation Confirm Dialog Flow
            bookingToCancel?.let { booking ->
                Dialog(onDismissRequest = { bookingToCancel = null }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(1.dp, TertiaryRose.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(TertiaryRose.copy(alpha = 0.1f), CircleShape)
                                    .border(1.dp, TertiaryRose, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = TertiaryRose, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "CANCEL SESSION?",
                                style = MaterialTheme.typography.headlineMedium,
                                color = OnSurfaceText,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Are you sure you want to cancel your high-performance booking at ${booking.studioName}?\n\nA full refund of RM${booking.totalAmount.toInt()} will be credited back to your payment method.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariantText,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { bookingToCancel = null },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = OnSurfaceVariantText)
                                ) {
                                    Text("KEEP BOOKING")
                                }
                                Button(
                                    onClick = {
                                        viewModel.cancelBooking(booking.id)
                                        bookingToCancel = null
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = TertiaryRose)
                                ) {
                                    Text("YES, CANCEL", color = OnSurfaceText, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // QR Code Dialog popup
    activeQrBooking?.let { booking ->
        Dialog(onDismissRequest = { activeQrBooking = null }) {
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
                        text = "SCAN AT ENTRY",
                        style = MaterialTheme.typography.labelLarge,
                        color = SecondaryCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // QR representation
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCuIJsKi-drfGVtpJnr333dZDW7-11CeMo_KimtQGExz9syxqcyJLAYmxSe4SPogGxkAfshXVf0hTOKSZTSYmkxd2r5PkAWtmmRGr-YBb-kcqstjFmgyAum8LdjgGNXhNWc4nmp0JI6riLhbHhDNeTE-YPSEK7zBkH4iB94AV0QpkJUrv15rqISv8kSZS95ah-LhT-G1lQlK62OW8SyHvLUIOZ8BnLvd1kEhut-bMb0i4Gg25fBqiQmsT6MuGCn6nP1r3a0fermiQ",
                            contentDescription = "QR Code",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = booking.qrCodePayload,
                        style = MaterialTheme.typography.labelLarge,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${booking.studioName}\nStation ${booking.stationName}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceText,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = booking.timeText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariantText
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { activeQrBooking = null },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurpleContainer)
                    ) {
                        Text("DISMISS", color = OnSurfaceText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun BookingCard(
    booking: com.example.data.Booking,
    onViewQr: () -> Unit,
    onEdit: () -> Unit,
    onCancel: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, OutlineVariantColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.8f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Status bar indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(PrimaryPurple, SecondaryCyan)
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = booking.studioName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = OutlineColor, modifier = Modifier.size(14.dp))
                            Text(
                                text = booking.studioLocation.substringBefore(","),
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariantText
                            )
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .background(SecondaryCyan.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                            .border(0.5.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(SecondaryCyan, CircleShape)
                            )
                            Text(
                                text = "CONFIRMED",
                                color = SecondaryCyan,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date & Time box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceLowest, RoundedCornerShape(8.dp))
                        .border(0.5.dp, OutlineVariantColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("DATE", style = MaterialTheme.typography.labelSmall, color = OutlineColor, fontSize = 9.sp)
                        Text(booking.dateText, style = MaterialTheme.typography.labelLarge, color = OnSurfaceText, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("TIME", style = MaterialTheme.typography.labelSmall, color = OutlineColor, fontSize = 9.sp)
                        Text(booking.timeText, style = MaterialTheme.typography.labelLarge, color = OnSurfaceText, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onViewQr,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DarkSurfaceLowest, modifier = Modifier.size(18.dp))
                                Text(
                                    text = "View QR",
                                    color = DarkSurfaceLowest,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    Box {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(DarkSurfaceLowest, RoundedCornerShape(8.dp))
                                .border(1.dp, OutlineVariantColor, RoundedCornerShape(8.dp))
                                .clickable { showMenu = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = OnSurfaceText
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(DarkSurfaceLowest)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Reschedule", color = OnSurfaceText) },
                                onClick = {
                                    showMenu = false
                                    onEdit()
                                },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = SecondaryCyan) }
                            )
                            DropdownMenuItem(
                                text = { Text("Cancel Booking", color = TertiaryRoseDim) },
                                onClick = {
                                    showMenu = false
                                    onCancel()
                                },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = TertiaryRose) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CancelledBookingCard(
    booking: com.example.data.Booking
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, OutlineVariantColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(TertiaryRose.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = booking.studioName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariantText
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = OutlineColor, modifier = Modifier.size(14.dp))
                            Text(
                                text = booking.studioLocation.substringBefore(","),
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariantText
                            )
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .background(TertiaryRose.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                            .border(0.5.dp, TertiaryRose.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "CANCELLED",
                            color = TertiaryRose,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceLowest.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("DATE", style = MaterialTheme.typography.labelSmall, color = OutlineColor, fontSize = 9.sp)
                        Text(booking.dateText, style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariantText, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("TIME", style = MaterialTheme.typography.labelSmall, color = OutlineColor, fontSize = 9.sp)
                        Text(booking.timeText, style = MaterialTheme.typography.labelLarge, color = OnSurfaceVariantText, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
