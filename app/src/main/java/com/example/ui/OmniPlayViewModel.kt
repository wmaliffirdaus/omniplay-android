package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.repository.OmniPlayRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class OmniPlayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OmniPlayRepository

    // Auth state
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userFullName = MutableStateFlow("Player One")
    val userFullName: StateFlow<String> = _userFullName.asStateFlow()

    private val _userEmail = MutableStateFlow("player.one@omniplay.gg")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    private val _userLevel = MutableStateFlow(42)
    val userLevel: StateFlow<Int> = _userLevel.asStateFlow()

    private val _userPoints = MutableStateFlow(12500)
    val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

    private val _userHours = MutableStateFlow(420)
    val userHours: StateFlow<Int> = _userHours.asStateFlow()

    private val _selectedPlatforms = MutableStateFlow(listOf("PS5", "PC", "VR", "Racing"))
    val selectedPlatforms: StateFlow<List<String>> = _selectedPlatforms.asStateFlow()

    // Query states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Details/Booking States
    private val _selectedStudio = MutableStateFlow<Studio?>(null)
    val selectedStudio: StateFlow<Studio?> = _selectedStudio.asStateFlow()

    private val _selectedStation = MutableStateFlow<Station?>(null)
    val selectedStation: StateFlow<Station?> = _selectedStation.asStateFlow()

    private val _selectedStationFilter = MutableStateFlow("ALL")
    val selectedStationFilter: StateFlow<String> = _selectedStationFilter.asStateFlow()

    private val _selectedDate = MutableStateFlow("Oct 24")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedTimeSlots = MutableStateFlow(listOf("10:00 AM", "11:00 AM"))
    val selectedTimeSlots: StateFlow<List<String>> = _selectedTimeSlots.asStateFlow()

    private val _promoCode = MutableStateFlow("")
    val promoCode: StateFlow<String> = _promoCode.asStateFlow()

    private val _discount = MutableStateFlow(0.0)
    val discount: StateFlow<Double> = _discount.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow("Visa ending in 4242")
    val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod.asStateFlow()

    private val _useOmniPoints = MutableStateFlow(false)
    val useOmniPoints: StateFlow<Boolean> = _useOmniPoints.asStateFlow()

    // Auth error state
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    // Payment Gateway states
    private val _paymentState = MutableStateFlow(PaymentState.IDLE)
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()

    private val _paymentError = MutableStateFlow<String?>(null)
    val paymentError: StateFlow<String?> = _paymentError.asStateFlow()

    // Stripe input states
    private val _stripeCardNumber = MutableStateFlow("")
    val stripeCardNumber: StateFlow<String> = _stripeCardNumber.asStateFlow()

    private val _stripeExpiry = MutableStateFlow("")
    val stripeExpiry: StateFlow<String> = _stripeExpiry.asStateFlow()

    private val _stripeCvc = MutableStateFlow("")
    val stripeCvc: StateFlow<String> = _stripeCvc.asStateFlow()

    // PayPal input states
    private val _paypalEmail = MutableStateFlow("")
    val paypalEmail: StateFlow<String> = _paypalEmail.asStateFlow()

    private val _paypalPassword = MutableStateFlow("")
    val paypalPassword: StateFlow<String> = _paypalPassword.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = OmniPlayRepository(database)
        
        viewModelScope.launch {
            repository.seedIfNeeded()
        }
    }

    // Main Flows
    val studios: StateFlow<List<Studio>> = combine(_selectedCategory, _searchQuery, repository.allStudios) { category, query, all ->
        var list = all
        if (category != "All") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }
        if (query.isNotEmpty()) {
            list = list.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.description.contains(query, ignoreCase = true) ||
                it.featuresString.contains(query, ignoreCase = true)
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stations: StateFlow<List<Station>> = combine(_selectedStudio, _selectedStationFilter) { studio, filter ->
        if (studio == null) return@combine emptyList<Station>()
        val allStations = repository.getStationsForStudio(studio.id).first()
        when (filter) {
            "PC" -> allStations.filter { it.type.equals("PC", ignoreCase = true) }
            "CONSOLE" -> allStations.filter { it.type.equals("Console", ignoreCase = true) }
            else -> allStations
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<Notification>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationCount: StateFlow<Int> = repository.allNotifications
        .map { list -> list.count { !it.isRead } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Actions
    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectStudio(studio: Studio) {
        _selectedStudio.value = studio
        _selectedStation.value = null // reset selection
        _selectedStationFilter.value = "ALL"
    }

    fun selectStation(station: Station) {
        if (station.status == "Available") {
            _selectedStation.value = station
        }
    }

    fun selectStationFilter(filter: String) {
        _selectedStationFilter.value = filter
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun toggleTimeSlot(slot: String) {
        val current = _selectedTimeSlots.value.toMutableList()
        if (current.contains(slot)) {
            current.remove(slot)
        } else {
            current.add(slot)
        }
        _selectedTimeSlots.value = current.sorted()
    }

    fun setPromoCode(code: String) {
        _promoCode.value = code
    }

    fun applyPromo() {
        if (_promoCode.value.equals("OMNIPLAY20", ignoreCase = true) || _promoCode.value.equals("CLAIM20", ignoreCase = true)) {
            _discount.value = 15.00
        } else if (_promoCode.value.isNotEmpty()) {
            _discount.value = 5.00
        }
    }

    fun setPaymentMethod(method: String) {
        _selectedPaymentMethod.value = method
    }

    fun toggleUseOmniPoints() {
        _useOmniPoints.value = !_useOmniPoints.value
    }

    // Authenticate Actions
    fun login(email: String) {
        login(email, "••••••••")
    }

    fun login(email: String, securityKey: String): Boolean {
        _authError.value = null
        val trimmedEmail = email.trim()
        if (trimmedEmail.isEmpty()) {
            _authError.value = "Identification / Email cannot be empty."
            return false
        }
        if (!trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
            _authError.value = "Please enter a valid email address."
            return false
        }
        if (securityKey.isEmpty() || securityKey.length < 6) {
            _authError.value = "Security Key must be at least 6 characters."
            return false
        }

        _isLoggedIn.value = true
        _userEmail.value = trimmedEmail
        _userFullName.value = trimmedEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
        _authError.value = null
        return true
    }

    fun register(name: String, email: String, platforms: List<String>) {
        register(name, email, "••••••••", platforms)
    }

    fun register(name: String, email: String, securityKey: String, platforms: List<String>): Boolean {
        _authError.value = null
        val trimmedName = name.trim()
        val trimmedEmail = email.trim()
        if (trimmedName.isEmpty()) {
            _authError.value = "Full Name cannot be empty."
            return false
        }
        if (trimmedEmail.isEmpty() || !trimmedEmail.contains("@")) {
            _authError.value = "Please enter a valid email address."
            return false
        }
        if (securityKey.length < 6) {
            _authError.value = "Password must be at least 6 characters."
            return false
        }

        _isLoggedIn.value = true
        _userFullName.value = trimmedName
        _userEmail.value = trimmedEmail
        _selectedPlatforms.value = platforms
        _authError.value = null
        return true
    }

    fun logout() {
        _isLoggedIn.value = false
        _authError.value = null
    }

    fun clearAuthError() {
        _authError.value = null
    }

    // Stripe & PayPal Input Setters & Actions
    fun setStripeCardNumber(value: String) {
        _stripeCardNumber.value = value
    }

    fun setStripeExpiry(value: String) {
        _stripeExpiry.value = value
    }

    fun setStripeCvc(value: String) {
        _stripeCvc.value = value
    }

    fun setPaypalEmail(value: String) {
        _paypalEmail.value = value
    }

    fun setPaypalPassword(value: String) {
        _paypalPassword.value = value
    }

    fun resetPaymentState() {
        _paymentState.value = PaymentState.IDLE
        _paymentError.value = null
    }

    fun processStripePayment(onSuccess: () -> Unit) {
        val cardNum = _stripeCardNumber.value.trim()
        val expiry = _stripeExpiry.value.trim()
        val cvc = _stripeCvc.value.trim()

        if (cardNum.length < 16) {
            _paymentError.value = "Invalid card number. Must be 16 digits."
            _paymentState.value = PaymentState.ERROR
            return
        }
        if (!expiry.contains("/") || expiry.length < 5) {
            _paymentError.value = "Invalid expiration date (MM/YY)."
            _paymentState.value = PaymentState.ERROR
            return
        }
        if (cvc.length < 3) {
            _paymentError.value = "Invalid CVC code (3 digits)."
            _paymentState.value = PaymentState.ERROR
            return
        }

        _paymentState.value = PaymentState.PROCESSING
        _paymentError.value = null

        viewModelScope.launch {
            kotlinx.coroutines.delay(2000) // Simulate secure Stripe charging API request
            completeBooking {
                _paymentState.value = PaymentState.SUCCESS
                _stripeCardNumber.value = ""
                _stripeExpiry.value = ""
                _stripeCvc.value = ""
                onSuccess()
            }
        }
    }

    fun processPaypalPayment(onSuccess: () -> Unit) {
        val email = _paypalEmail.value.trim()
        val password = _paypalPassword.value.trim()

        if (!email.contains("@") || email.length < 5) {
            _paymentError.value = "Invalid PayPal email address."
            _paymentState.value = PaymentState.ERROR
            return
        }
        if (password.length < 6) {
            _paymentError.value = "Password must be at least 6 characters."
            _paymentState.value = PaymentState.ERROR
            return
        }

        _paymentState.value = PaymentState.PROCESSING
        _paymentError.value = null

        viewModelScope.launch {
            kotlinx.coroutines.delay(2000) // Simulate secure PayPal express checkout handshake
            completeBooking {
                _paymentState.value = PaymentState.SUCCESS
                _paypalEmail.value = ""
                _paypalPassword.value = ""
                onSuccess()
            }
        }
    }

    // Booking Management Actions
    fun cancelBooking(bookingId: Int) {
        viewModelScope.launch {
            val bookingList = repository.allBookings.first()
            val booking = bookingList.find { it.id == bookingId } ?: return@launch
            
            // Mark booking as cancelled
            val updatedBooking = booking.copy(isCancelled = true)
            repository.updateBooking(updatedBooking)
            
            // Mark associated station as available
            val stationsForStudio = repository.getStationsForStudio(booking.studioId).first()
            val station = stationsForStudio.find { it.id == booking.stationId }
            if (station != null) {
                repository.updateStationStatus(station.copy(status = "Available"))
            }

            // Add cancellation alert system notification
            repository.addNotification(
                Notification(
                    title = "Booking Cancelled - Refund Issued",
                    body = "Your session for ${booking.stationName} on ${booking.dateText} was cancelled. A refund of RM${booking.totalAmount.toInt()} was sent to your payment method.",
                    category = "SYSTEM ALERT",
                    timeText = "Just now"
                )
            )

            // Adjust point values
            _userPoints.value = (_userPoints.value - 150).coerceAtLeast(0)
        }
    }

    fun rescheduleBooking(bookingId: Int, newDate: String, newTime: String) {
        viewModelScope.launch {
            val bookingList = repository.allBookings.first()
            val booking = bookingList.find { it.id == bookingId } ?: return@launch
            
            val updatedBooking = booking.copy(dateText = newDate, timeText = newTime)
            repository.updateBooking(updatedBooking)

            // Add notification alert
            repository.addNotification(
                Notification(
                    title = "Reschedule Confirmed",
                    body = "Your session at ${booking.studioName} was rescheduled to $newDate, $newTime.",
                    category = "SYSTEM ALERT",
                    timeText = "Just now"
                )
            )
        }
    }

    fun completeBooking(onComplete: (Booking) -> Unit) {
        val studio = _selectedStudio.value ?: return
        val station = _selectedStation.value ?: return
        val date = _selectedDate.value
        val slots = _selectedTimeSlots.value
        val duration = slots.size.coerceAtLeast(1)
        val rate = station.pricePerHour
        val subtotal = rate * duration
        val fee = 5.00
        val promo = _discount.value
        val ptsDiscount = if (_useOmniPoints.value) 10.0 else 0.0
        val finalAmount = (subtotal + fee - promo - ptsDiscount).coerceAtLeast(0.0)

        val qrCode = "OMNIPLAY-${UUID.randomUUID().toString().take(8).uppercase()}"

        val newBooking = Booking(
            studioId = studio.id,
            studioName = studio.name,
            stationId = station.id,
            stationName = station.name,
            dateText = "$date, 2026",
            timeText = if (slots.isNotEmpty()) "${slots.first().substringBefore(" ")} - ${slots.last().substringBefore(" ")} ${slots.last().substringAfter(" ")}" else "10:00 AM - 12:00 PM",
            studioLocation = studio.address,
            pricePerHour = rate,
            totalAmount = finalAmount,
            qrCodePayload = qrCode
        )

        viewModelScope.launch {
            repository.insertBooking(newBooking)
            
            // Mark station as booked
            repository.updateStationStatus(station.copy(status = "Booked"))
            
            // Add system alert for successful booking
            repository.addNotification(
                Notification(
                    title = "Booking Confirmed at ${studio.name}",
                    body = "Your high-performance station ${station.name} is booked for $date. View the active session dashboard for setup instructions.",
                    category = "SYSTEM ALERT",
                    timeText = "Just now"
                )
            )

            // Grant point reward
            _userPoints.value = _userPoints.value + 150
            _userHours.value = _userHours.value + duration

            onComplete(newBooking)
        }
    }

    fun markNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }
}

enum class PaymentState {
    IDLE,
    PROCESSING,
    SUCCESS,
    ERROR
}

enum class PaymentGateway {
    STRIPE,
    PAYPAL
}
