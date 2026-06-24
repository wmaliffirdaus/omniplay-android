package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.OmniPlayViewModel
import com.example.ui.theme.*

@Composable
fun NotificationScreen(
    viewModel: OmniPlayViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SecondaryCyan)
                }
                Text(
                    text = "Alert Center",
                    style = MaterialTheme.typography.headlineLarge,
                    color = OnSurfaceText,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Mark Read",
                color = SecondaryCyan,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    viewModel.markNotificationsRead()
                    Toast.makeText(context, "All messages marked as read!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        if (notifications.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = OutlineColor,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "All System Nodes Quiet",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnSurfaceText,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "There are no notifications or priority alerts currently routed to your terminal.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariantText,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { notif ->
                    val colorTheme = when (notif.category) {
                        "SYSTEM ALERT" -> TertiaryRose
                        "OMNIELITE" -> PrimaryPurple
                        else -> SecondaryCyan
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                0.5.dp,
                                if (notif.isRead) OutlineVariantColor.copy(alpha = 0.4f) else colorTheme.copy(alpha = 0.5f),
                                RoundedCornerShape(12.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (notif.isRead) DarkSurface.copy(alpha = 0.6f) else DarkSurfaceContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Status icon matching category
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(colorTheme.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (notif.category) {
                                        "SYSTEM ALERT" -> Icons.Default.Warning
                                        "OMNIELITE" -> Icons.Default.Star
                                        else -> Icons.Default.Star
                                    },
                                    contentDescription = null,
                                    tint = colorTheme,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Notification text contents
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = notif.category,
                                        color = colorTheme,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 8.sp
                                    )

                                    Text(
                                        text = notif.timeText,
                                        color = OnSurfaceVariantText,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = notif.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceText
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = notif.body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceVariantText
                                )
                            }

                            // Pulsing unread dot
                            if (!notif.isRead) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(colorTheme, CircleShape)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
