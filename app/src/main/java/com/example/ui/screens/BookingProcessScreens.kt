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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.OmniPlayViewModel
import com.example.ui.PaymentState
import com.example.ui.theme.*

// ==========================================
// 1. SELECT SCHEDULE SCREEN
// ==========================================
@Composable
fun SelectScheduleScreen(
    viewModel: OmniPlayViewModel,
    onNavigateToReview: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val studio by viewModel.selectedStudio.collectAsStateWithLifecycle()
    val station by viewModel.selectedStation.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedSlots by viewModel.selectedTimeSlots.collectAsStateWithLifecycle()

    if (studio == null || station == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Incomplete session context.", color = OnSurfaceText)
        }
        return
    }

    val availableDates = listOf("Oct 23", "Oct 24", "Oct 25", "Oct 26", "Oct 27", "Oct 28")
    val timeSlots = listOf(
        "08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM",
        "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM"
    )
    val bookedSlots = listOf("08:00 AM", "09:00 AM") // mock booked

    val duration = selectedSlots.size
    val rate = station!!.pricePerHour
    val totalEstimated = duration * rate

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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecondaryCyan)
                }
                Text(
                    text = "Select Schedule",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurfaceText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(48.dp)) // balance spacing
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                // Context Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, PrimaryPurple.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.8f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            AsyncImage(
                                model = studio!!.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Column {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(PrimaryPurple.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = station!!.type.uppercase(),
                                        color = PrimaryPurple,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 8.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(DarkSurfaceHighest, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "PRO TIER",
                                        color = OnSurfaceVariantText,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 8.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "OmniPlayer Elite Suite",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceText
                            )
                            Text(
                                text = "Sector 7G, Downtown Hub",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceVariantText
                            )
                        }
                    }
                }

                // Date strips
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "DATE",
                    style = MaterialTheme.typography.labelLarge,
                    color = OnSurfaceVariantText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(availableDates) { date ->
                        val isSel = selectedDate == date
                        val isPast = date == "Oct 23"
                        Card(
                            modifier = Modifier
                                .width(74.dp)
                                .border(
                                    1.dp,
                                    when {
                                        isSel -> PrimaryPurple
                                        isPast -> Color.Transparent
                                        else -> OutlineVariantColor.copy(alpha = 0.4f)
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                        .clickable(enabled = !isPast) { viewModel.selectDate(date) },
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isSel -> PrimaryPurple.copy(alpha = 0.15f)
                                    isPast -> DarkSurfaceLowest.copy(alpha = 0.4f)
                                    else -> DarkSurfaceContainer
                                }
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = date.substringBefore(" "),
                                    color = if (isPast) OutlineColor else if (isSel) PrimaryPurple else OnSurfaceVariantText,
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Text(
                                    text = date.substringAfter(" "),
                                    color = if (isPast) OutlineColor else if (isSel) PrimaryPurple else OnSurfaceText,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = if (isPast) TextDecoration.LineThrough else TextDecoration.None
                                )
                                Text(
                                    text = if (date == "Oct 24") "TUE" else "WED",
                                    color = if (isPast) OutlineColor else if (isSel) PrimaryPurple else OnSurfaceVariantText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                // Time slots
                Spacer(modifier = Modifier.height(28.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TIME SLOTS",
                        style = MaterialTheme.typography.labelLarge,
                        color = OnSurfaceVariantText,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .background(SecondaryCyan.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(0.5.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(6.dp).background(SecondaryCyan, CircleShape))
                            Text("LIVE REFRESH", color = SecondaryCyan, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Time grid (2 columns)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val rows = timeSlots.chunked(3)
                    rows.forEach { rowSlots ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowSlots.forEach { slot ->
                                val isBooked = bookedSlots.contains(slot)
                                val isSel = selectedSlots.contains(slot)
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp)
                                        .border(
                                            1.dp,
                                            when {
                                                isSel -> SecondaryCyan
                                                isBooked -> OutlineVariantColor.copy(alpha = 0.2f)
                                                else -> SecondaryCyan.copy(alpha = 0.6f)
                                            },
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable(enabled = !isBooked) { viewModel.toggleTimeSlot(slot) },
                                    colors = CardDefaults.cardColors(
                                        containerColor = when {
                                            isSel -> SecondaryCyan.copy(alpha = 0.15f)
                                            isBooked -> DarkSurfaceLowest.copy(alpha = 0.4f)
                                            else -> Color.Transparent
                                        }
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = slot,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isBooked) OutlineColor else if (isSel) SecondaryCyan else OnSurfaceText,
                                            textDecoration = if (isBooked) TextDecoration.LineThrough else TextDecoration.None
                                        )
                                    }
                                }
                            }
                            if (rowStationsSize(rowSlots = rowSlots.size)) {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Duration Indicator
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, PrimaryPurple.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(DarkSurface, RoundedCornerShape(8.dp))
                                    .border(0.5.dp, OutlineVariantColor, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = PrimaryPurple)
                            }
                            Column {
                                Text("DURATION", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText)
                                Text("Selected: $duration Hours", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("BASE RATE", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText)
                            Text("RM${rate.toInt()} / hr", style = MaterialTheme.typography.labelLarge, color = SecondaryCyan, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        // Sticky Footer
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
                    Text("TOTAL ESTIMATED", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText)
                    Text("RM ${totalEstimated.toInt()}", style = MaterialTheme.typography.headlineLarge, color = OnSurfaceText, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        if (selectedSlots.isNotEmpty()) {
                            onNavigateToReview()
                        } else {
                            Toast.makeText(context, "Please select at least 1 hourly time slot!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .width(180.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(listOf(PrimaryPurple, SecondaryCyan)),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("PROCEED", color = DarkSurfaceLowest, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = DarkSurfaceLowest, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

private fun rowStationsSize(rowSlots: Int): Boolean = rowSlots < 3

// ==========================================
// 2. REVIEW BOOKING SCREEN
// ==========================================
@Composable
fun ReviewBookingScreen(
    viewModel: OmniPlayViewModel,
    onNavigateToPayment: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val studio by viewModel.selectedStudio.collectAsStateWithLifecycle()
    val station by viewModel.selectedStation.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedSlots by viewModel.selectedTimeSlots.collectAsStateWithLifecycle()
    val promoCode by viewModel.promoCode.collectAsStateWithLifecycle()
    val discount by viewModel.discount.collectAsStateWithLifecycle()

    if (studio == null || station == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Review error: Missing details.", color = OnSurfaceText)
        }
        return
    }

    var promoInput by remember { mutableStateOf(promoCode) }

    val duration = selectedSlots.size
    val rate = station!!.pricePerHour
    val subtotal = duration * rate
    val fee = 5.00
    val total = (subtotal + fee - discount).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top app bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecondaryCyan)
            }
            Text(
                text = "Review Booking",
                style = MaterialTheme.typography.headlineLarge,
                color = PrimaryPurple,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Studio Details",
            style = MaterialTheme.typography.headlineMedium,
            color = SecondaryCyan,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Studio Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, PrimaryPurple.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.8f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        AsyncImage(
                            model = studio!!.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Column {
                        Text(studio!!.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = OutlineColor, modifier = Modifier.size(14.dp))
                            Text(station!!.name, color = OnSurfaceVariantText, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = OutlineVariantColor.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(16.dp))

                // Date & Time grid box
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(0.5.dp, OutlineVariantColor, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("DATE", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = SecondaryCyan, modifier = Modifier.size(16.dp))
                                Text("$selectedDate, 2026", color = OnSurfaceText, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(0.5.dp, OutlineVariantColor, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("TIME", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = SecondaryCyan, modifier = Modifier.size(16.dp))
                                Text(
                                    text = if (selectedSlots.isNotEmpty()) "${selectedSlots.first().substringBefore(" ")} - ${selectedSlots.last().substringBefore(" ")}" else "10:00 - 12:00",
                                    color = OnSurfaceText,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Features chips
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("4K DISPLAY", "ACOUSTIC TREATED").forEach { feat ->
                Box(
                    modifier = Modifier
                        .background(DarkSurfaceContainer, RoundedCornerShape(20.dp))
                        .border(0.5.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(feat, color = SecondaryCyan, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                }
            }
        }

        // Order Summary
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Order Summary",
            style = MaterialTheme.typography.headlineMedium,
            color = SecondaryCyan,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, PrimaryPurple.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.8f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Hourly Rate (${station!!.name})", color = OnSurfaceVariantText)
                    Text("RM${rate.toInt()}", style = MaterialTheme.typography.labelLarge, color = OnSurfaceText, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Duration", color = OnSurfaceVariantText)
                    Text("$duration Hours", style = MaterialTheme.typography.labelLarge, color = OnSurfaceText, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Service Fee", color = OnSurfaceVariantText)
                    Text("RM${fee.toInt()}", style = MaterialTheme.typography.labelLarge, color = OnSurfaceText, fontWeight = FontWeight.Bold)
                }

                if (discount > 0.0) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Discount applied", color = SecondaryCyan)
                        Text("-RM${discount.toInt()}", style = MaterialTheme.typography.labelLarge, color = SecondaryCyan, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = OutlineVariantColor.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total", style = MaterialTheme.typography.headlineMedium, color = OnSurfaceText)
                    Text("RM${total.toInt()}", style = MaterialTheme.typography.displayLarge, color = PrimaryPurple, fontWeight = FontWeight.Bold)
                }

                // Coupon code
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = promoInput,
                        onValueChange = { promoInput = it },
                        modifier = Modifier.weight(1.5f),
                        placeholder = { Text("Enter Promo Code", color = OutlineColor) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SecondaryCyan,
                            unfocusedBorderColor = OutlineVariantColor,
                            focusedTextColor = OnSurfaceText,
                            unfocusedTextColor = OnSurfaceText
                        ),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            viewModel.setPromoCode(promoInput)
                            viewModel.applyPromo()
                            Toast.makeText(context, "Promo code checked!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f).height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceContainer),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("APPLY", color = SecondaryCyan, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onNavigateToPayment,
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
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("Proceed to Payment", color = DarkSurfaceLowest, fontWeight = FontWeight.Bold)
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = DarkSurfaceLowest, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. SECURE PAYMENT (CHECKOUT) SCREEN
// ==========================================
@Composable
fun PaymentScreen(
    viewModel: OmniPlayViewModel,
    onPaymentSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val studio by viewModel.selectedStudio.collectAsStateWithLifecycle()
    val station by viewModel.selectedStation.collectAsStateWithLifecycle()
    val selectedSlots by viewModel.selectedTimeSlots.collectAsStateWithLifecycle()
    val discount by viewModel.discount.collectAsStateWithLifecycle()
    val usePoints by viewModel.useOmniPoints.collectAsStateWithLifecycle()
    val paymentMethod by viewModel.selectedPaymentMethod.collectAsStateWithLifecycle()

    val stripeCardNum by viewModel.stripeCardNumber.collectAsStateWithLifecycle()
    val stripeExpiry by viewModel.stripeExpiry.collectAsStateWithLifecycle()
    val stripeCvc by viewModel.stripeCvc.collectAsStateWithLifecycle()
    val paypalEmail by viewModel.paypalEmail.collectAsStateWithLifecycle()
    val paypalPassword by viewModel.paypalPassword.collectAsStateWithLifecycle()
    val paymentState by viewModel.paymentState.collectAsStateWithLifecycle()
    val paymentError by viewModel.paymentError.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.resetPaymentState()
    }

    if (studio == null || station == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Payment context missing.", color = OnSurfaceText)
        }
        return
    }

    val duration = selectedSlots.size
    val rate = station!!.pricePerHour
    val subtotal = duration * rate
    val fee = 5.00
    val ptsDiscount = if (usePoints) 10.0 else 0.0
    val total = (subtotal + fee - discount - ptsDiscount).coerceAtLeast(0.0)

    val methods = listOf(
        PaymentOption("Stripe Secure Card", "Credit Card", Icons.Default.Add, "Pay with Visa, Master, Amex"),
        PaymentOption("PayPal Express", "Digital Wallet", Icons.Default.Share, "Pay with PayPal Wallet"),
        PaymentOption("Visa ending in 4242", "Saved Card", Icons.Default.DateRange, "Exp 12/25"),
        PaymentOption("Mastercard ending in 8899", "Saved Card", Icons.Default.DateRange, "Exp 08/24")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecondaryCyan)
            }
            Text(
                text = "Checkout",
                style = MaterialTheme.typography.headlineLarge,
                color = OnSurfaceText,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Order Summary card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, OutlineVariantColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.7f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Order Summary", style = MaterialTheme.typography.headlineMedium, color = PrimaryPurple, fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .background(TertiaryRose.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(0.5.dp, TertiaryRose.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("ACTIVE SESSION", color = TertiaryRoseDim, style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("OmniPlayer Elite Pass - 1 Month", color = OnSurfaceVariantText)
                    Text("RM$subtotal", style = MaterialTheme.typography.labelLarge, color = OnSurfaceText)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("VR Pod Booking ($duration Hrs)", color = OnSurfaceVariantText)
                    Text("RM$fee", style = MaterialTheme.typography.labelLarge, color = OnSurfaceText)
                }
                if (discount > 0.0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Loyalty Discount applied", color = SecondaryCyan)
                        Text("-RM$discount", style = MaterialTheme.typography.labelLarge, color = SecondaryCyan)
                    }
                }
                if (usePoints) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Omni Points Discount", color = PrimaryPurple)
                        Text("-RM$ptsDiscount", style = MaterialTheme.typography.labelLarge, color = PrimaryPurple)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = OutlineVariantColor.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Due", style = MaterialTheme.typography.bodyLarge, color = OnSurfaceVariantText)
                    Text("RM${total.toInt()}", style = MaterialTheme.typography.displayLarge, color = SecondaryCyan, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Select Payment Method
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Select Payment Method",
            style = MaterialTheme.typography.headlineMedium,
            color = PrimaryPurple,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            methods.forEach { option ->
                val isSel = paymentMethod == option.name
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (isSel) SecondaryCyan else OutlineVariantColor.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.setPaymentMethod(option.name) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSel) SecondaryCyan.copy(alpha = 0.05f) else DarkSurface
                    ),
                    shape = RoundedCornerShape(12.dp)
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
                                .size(44.dp)
                                .background(DarkSurfaceLowest, RoundedCornerShape(8.dp))
                                .border(0.5.dp, if (isSel) SecondaryCyan else OutlineVariantColor, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(option.icon, contentDescription = null, tint = if (isSel) SecondaryCyan else OnSurfaceText)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = option.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = OnSurfaceText
                            )
                            if (option.subtitle.isNotEmpty()) {
                                Text(option.subtitle, color = OnSurfaceVariantText, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(if (isSel) SecondaryCyan.copy(alpha = 0.15f) else DarkSurfaceHighest, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                option.tag.uppercase(),
                                color = if (isSel) SecondaryCyan else OutlineColor,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 8.sp
                            )
                        }
                    }
                }
            }

            // Conditional forms based on selected gateway method
            if (paymentMethod == "Stripe Secure Card") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(1.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "STRIPE SECURE CARD PORTAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = SecondaryCyan,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = stripeCardNum,
                            onValueChange = { if (it.length <= 16) viewModel.setStripeCardNumber(it) },
                            label = { Text("CARD NUMBER (16 DIGITS)", color = OnSurfaceVariantText) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("4111 2222 3333 4444", color = OutlineColor.copy(alpha = 0.5f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SecondaryCyan,
                                unfocusedBorderColor = OutlineVariantColor,
                                focusedTextColor = OnSurfaceText,
                                unfocusedTextColor = OnSurfaceText
                            ),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = stripeExpiry,
                                onValueChange = { if (it.length <= 5) viewModel.setStripeExpiry(it) },
                                label = { Text("EXPIRY (MM/YY)", color = OnSurfaceVariantText) },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("12/26", color = OutlineColor.copy(alpha = 0.5f)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SecondaryCyan,
                                    unfocusedBorderColor = OutlineVariantColor,
                                    focusedTextColor = OnSurfaceText,
                                    unfocusedTextColor = OnSurfaceText
                                ),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = stripeCvc,
                                onValueChange = { if (it.length <= 3) viewModel.setStripeCvc(it) },
                                label = { Text("CVC", color = OnSurfaceVariantText) },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("123", color = OutlineColor.copy(alpha = 0.5f)) },
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
                }
            } else if (paymentMethod == "PayPal Express") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(1.dp, SecondaryCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "PAYPAL EXPRESS CHECKOUT PORTAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = SecondaryCyan,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        OutlinedTextField(
                            value = paypalEmail,
                            onValueChange = { viewModel.setPaypalEmail(it) },
                            label = { Text("PAYPAL ACCOUNT EMAIL", color = OnSurfaceVariantText) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("gamer.one@paypal.com", color = OutlineColor.copy(alpha = 0.5f)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SecondaryCyan,
                                unfocusedBorderColor = OutlineVariantColor,
                                focusedTextColor = OnSurfaceText,
                                unfocusedTextColor = OnSurfaceText
                            ),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = paypalPassword,
                            onValueChange = { viewModel.setPaypalPassword(it) },
                            label = { Text("PAYPAL ACCESS KEY / PASSWORD", color = OnSurfaceVariantText) },
                            modifier = Modifier.fillMaxWidth(),
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
            }

            // Omni points toggle row
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, OutlineVariantColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(TertiaryRose.copy(alpha = 0.1f), CircleShape)
                                .border(0.5.dp, TertiaryRose, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = TertiaryRose)
                        }
                        Column {
                            Text("Use Omni Points", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                            Text("Balance: 12,450 pts", style = MaterialTheme.typography.labelLarge, color = TertiaryRoseDim)
                        }
                    }

                    Switch(
                        checked = usePoints,
                        onCheckedChange = { viewModel.toggleUseOmniPoints() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = OnSurfaceText,
                            checkedTrackColor = SecondaryCyan
                        )
                    )
                }
            }
        }

        // Error log
        paymentError?.let { err ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TertiaryRose, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = TertiaryRose.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = TertiaryRose)
                    Text(err, color = TertiaryRose, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Encryption badges
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = OutlineColor, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("256-BIT ENCRYPTION  ", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText, fontSize = 9.sp)
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = OutlineColor, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("PCI DSS COMPLIANT", style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariantText, fontSize = 9.sp)
        }

        // Pay action button
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                when (paymentMethod) {
                    "Stripe Secure Card" -> viewModel.processStripePayment(onSuccess = onPaymentSuccess)
                    "PayPal Express" -> viewModel.processPaypalPayment(onSuccess = onPaymentSuccess)
                    else -> {
                        // Quick check mock for saved card options
                        viewModel.completeBooking {
                            onPaymentSuccess()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(listOf(PrimaryPurple, SecondaryCyan)),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = DarkSurfaceLowest)
                    Text(
                        text = "Secure Pay RM${total.toInt()}",
                        color = DarkSurfaceLowest,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "By confirming, you agree to the OmniPlay Terms of Service.",
            textAlign = TextAlign.Center,
            color = OnSurfaceVariantText.copy(alpha = 0.6f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (paymentState == PaymentState.PROCESSING) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { }) {
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = SecondaryCyan)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "SECURING LINK HANDSHAKE...",
                        style = MaterialTheme.typography.labelLarge,
                        color = SecondaryCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Executing secure SDK charging protocols over safe gateway relays.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariantText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

data class PaymentOption(
    val name: String,
    val tag: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val subtitle: String
)

// ==========================================
// 4. BOOKING CONFIRMED SCREEN
// ==========================================
@Composable
fun BookingConfirmedScreen(
    viewModel: OmniPlayViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateHome: () -> Unit
) {
    val studio by viewModel.selectedStudio.collectAsStateWithLifecycle()
    val station by viewModel.selectedStation.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedSlots by viewModel.selectedTimeSlots.collectAsStateWithLifecycle()

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
            // Animated Success Icon with glow
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(SecondaryCyan.copy(alpha = 0.15f), CircleShape)
                    .border(2.dp, SecondaryCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SecondaryCyan,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Booking Confirmed!",
                style = MaterialTheme.typography.headlineLarge,
                color = OnSurfaceText,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your high-performance pod is locked in and ready for deployment.",
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariantText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Glass details pod with QR
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, OutlineVariantColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // QR Code module with target borders
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCuIJsKi-drfGVtpJnr333dZDW7-11CeMo_KimtQGExz9syxqcyJLAYmxSe4SPogGxkAfshXVf0hTOKSZTSYmkxd2r5PkAWtmmRGr-YBb-kcqstjFmgyAum8LdjgGNXhNWc4nmp0JI6riLhbHhDNeTE-YPSEK7zBkH4iB94AV0QpkJUrv15rqISv8kSZS95ah-LhT-G1lQlK62OW8SyHvLUIOZ8BnLvd1kEhut-bMb0i4Gg25fBqiQmsT6MuGCn6nP1r3a0fermiQ",
                            contentDescription = "QR Code Scanner",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).background(TertiaryRose, CircleShape))
                        Text(
                            "SCAN AT ENTRY",
                            color = SecondaryCyan,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = OutlineVariantColor.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Booking Details list
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DetailRow(
                            label = "Station",
                            value = if (station != null) station!!.name else "PS5 Station #1",
                            icon = Icons.Default.PlayArrow
                        )
                        DetailRow(
                            label = "Date & Time",
                            value = if (selectedSlots.isNotEmpty()) "$selectedDate, ${selectedSlots.first()} - ${selectedSlots.last()}" else "$selectedDate, 10:00 AM - 12:00 PM",
                            icon = Icons.Default.DateRange
                        )
                        DetailRow(
                            label = "Location",
                            value = if (studio != null) studio!!.name else "CTRL+PLAY SS15",
                            icon = Icons.Default.LocationOn
                        )
                    }
                }
            }

            // CTAs
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onNavigateToHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                        Brush.linearGradient(listOf(PrimaryPurple, SecondaryCyan)),
                        shape = RoundedCornerShape(28.dp)
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.List, contentDescription = null, tint = DarkSurfaceLowest)
                        Text("View History", color = DarkSurfaceLowest, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                border = BorderStroke(1.dp, SecondaryCyan.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SecondaryCyan),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Text("Add to Calendar", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .clickable { onNavigateHome() }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = OnSurfaceVariantText, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Return to Home", color = OnSurfaceVariantText, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = OnSurfaceVariantText)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(16.dp))
            Text(value, style = MaterialTheme.typography.labelLarge, color = OnSurfaceText, fontWeight = FontWeight.Bold)
        }
    }
}
