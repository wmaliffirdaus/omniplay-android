package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.OmniPlayViewModel
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    private val viewModel: OmniPlayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppLayout(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppLayout(viewModel: OmniPlayViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Determine if we show bottom navigation bar
    val showBottomBar = currentRoute in listOf("home", "explore", "bookings", "profile")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        bottomBar = {
            if (showBottomBar && isLoggedIn) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                        .background(Color.Transparent)
                ) {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, OutlineVariantColor, RoundedCornerShape(24.dp)),
                        containerColor = DarkSurface.copy(alpha = 0.95f),
                        tonalElevation = 8.dp
                    ) {
                        // Home Tab
                        NavigationBarItem(
                            selected = currentRoute == "home",
                            onClick = {
                                if (currentRoute != "home") {
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SecondaryCyan,
                                selectedTextColor = SecondaryCyan,
                                unselectedIconColor = OutlineColor,
                                unselectedTextColor = OutlineColor,
                                indicatorColor = SecondaryCyan.copy(alpha = 0.15f)
                            )
                        )

                        // Explore Tab
                        NavigationBarItem(
                            selected = currentRoute == "explore",
                            onClick = {
                                if (currentRoute != "explore") {
                                    navController.navigate("explore") {
                                        popUpTo("home")
                                    }
                                }
                            },
                            icon = { Icon(Icons.Default.Search, contentDescription = "Explore") },
                            label = { Text("Explore", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SecondaryCyan,
                                selectedTextColor = SecondaryCyan,
                                unselectedIconColor = OutlineColor,
                                unselectedTextColor = OutlineColor,
                                indicatorColor = SecondaryCyan.copy(alpha = 0.15f)
                            )
                        )

                        // Bookings Tab
                        NavigationBarItem(
                            selected = currentRoute == "bookings",
                            onClick = {
                                if (currentRoute != "bookings") {
                                    navController.navigate("bookings") {
                                        popUpTo("home")
                                    }
                                }
                            },
                            icon = { Icon(Icons.Default.List, contentDescription = "Bookings") },
                            label = { Text("Bookings", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SecondaryCyan,
                                selectedTextColor = SecondaryCyan,
                                unselectedIconColor = OutlineColor,
                                unselectedTextColor = OutlineColor,
                                indicatorColor = SecondaryCyan.copy(alpha = 0.15f)
                            )
                        )

                        // Profile Tab
                        NavigationBarItem(
                            selected = currentRoute == "profile",
                            onClick = {
                                if (currentRoute != "profile") {
                                    navController.navigate("profile") {
                                        popUpTo("home")
                                    }
                                }
                            },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SecondaryCyan,
                                selectedTextColor = SecondaryCyan,
                                unselectedIconColor = OutlineColor,
                                unselectedTextColor = OutlineColor,
                                indicatorColor = SecondaryCyan.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "home" else "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            // LOGIN SCREEN
            composable("login") {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    }
                )
            }

            // REGISTER SCREEN
            composable("register") {
                RegisterScreen(
                    viewModel = viewModel,
                    onRegisterSuccess = {
                        navController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }

            // HOME DASHBOARD
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToStudio = { navController.navigate("studio_details") },
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateToProfile = { navController.navigate("profile") },
                    onNavigateToExplore = { navController.navigate("explore") }
                )
            }

            // EXPLORE STUDIOS
            composable("explore") {
                ExploreScreen(
                    viewModel = viewModel,
                    onNavigateToStudio = { navController.navigate("studio_details") },
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateToProfile = { navController.navigate("profile") }
                )
            }

            // STUDIO DETAILS
            composable("studio_details") {
                StudioDetailsScreen(
                    viewModel = viewModel,
                    onNavigateToSchedule = { navController.navigate("select_schedule") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // SELECT SCHEDULE
            composable("select_schedule") {
                SelectScheduleScreen(
                    viewModel = viewModel,
                    onNavigateToReview = { navController.navigate("review_booking") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // REVIEW BOOKING (ORDER SUMMARY & COUPON)
            composable("review_booking") {
                ReviewBookingScreen(
                    viewModel = viewModel,
                    onNavigateToPayment = { navController.navigate("checkout") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // SECURE PAY (CHECKOUT)
            composable("checkout") {
                PaymentScreen(
                    viewModel = viewModel,
                    onPaymentSuccess = {
                        navController.navigate("booking_confirmed") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // BOOKING CONFIRMED
            composable("booking_confirmed") {
                BookingConfirmedScreen(
                    viewModel = viewModel,
                    onNavigateToHistory = {
                        navController.navigate("bookings") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
                    onNavigateHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            // BOOKINGS HISTORY LOGS
            composable("bookings") {
                BookingsScreen(
                    viewModel = viewModel,
                    onNavigateToExplore = { navController.navigate("explore") },
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateToProfile = { navController.navigate("profile") }
                )
            }

            // PROFILE STATS & PREFERENCES
            composable("profile") {
                ProfileScreen(
                    viewModel = viewModel,
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToNotifications = { navController.navigate("notifications") },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // NOTIFICATIONS (ALERT CENTER)
            composable("notifications") {
                NotificationScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
