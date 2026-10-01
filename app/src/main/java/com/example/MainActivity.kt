package com.example

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.customer.*
import com.example.ui.screens.restaurant.RestaurantDashboardScreen
import com.example.ui.screens.rider.RiderDashboardScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as WafaQuickEatsApp
        val repository = app.repository

        setContent {
            MyApplicationTheme {
                // Request location & notification permissions
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    // Handled gracefully in background service
                }

                LaunchedEffect(Unit) {
                    val permissionsToRequest = mutableListOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    permissionLauncher.launch(permissionsToRequest.toTypedArray())
                }

                // Collect state from repository
                val user by repository.currentUser.collectAsStateWithLifecycle()
                val language by repository.currentLanguage.collectAsStateWithLifecycle()
                val isAiEnabled by repository.isAiEnabled.collectAsStateWithLifecycle()
                val isOrderingPaused by repository.isOrderingPaused.collectAsStateWithLifecycle()
                val cartItems by repository.cartItems.collectAsStateWithLifecycle()
                val orders by repository.orders.collectAsStateWithLifecycle()
                val activeOrder by repository.activeOrder.collectAsStateWithLifecycle()
                val liveRiderLocation by repository.liveRiderLocation.collectAsStateWithLifecycle()
                val pointsHistory by repository.pointsHistory.collectAsStateWithLifecycle()
                val riderNotes by repository.riderNotes.collectAsStateWithLifecycle()
                val supportTickets by repository.supportTickets.collectAsStateWithLifecycle()
                val aiProviders by repository.aiProviders.collectAsStateWithLifecycle()
                val auditLogs by repository.auditLogs.collectAsStateWithLifecycle()

                // Customer Navigation sub-state
                var currentCustomerTab by remember { mutableStateOf(CustomerScreen.HOME) }
                var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }
                var selectedFoodForCustomization by remember { mutableStateOf<FoodItem?>(null) }
                var isCheckoutActive by remember { mutableStateOf(false) }
                var checkoutDiscount by remember { mutableDoubleStateOf(0.0) }

                // Dialog states
                var showAiSheet by remember { mutableStateOf(false) }
                var viewingInvoice by remember { mutableStateOf<Invoice?>(null) }
                var ratingOrderTarget by remember { mutableStateOf<Order?>(null) }

                // Back navigation handling
                BackHandler(enabled = isCheckoutActive || selectedRestaurant != null || currentCustomerTab != CustomerScreen.HOME) {
                    when {
                        isCheckoutActive -> isCheckoutActive = false
                        selectedRestaurant != null -> selectedRestaurant = null
                        currentCustomerTab != CustomerScreen.HOME -> currentCustomerTab = CustomerScreen.HOME
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        WafaTopBar(
                            currentRole = user.role,
                            points = user.points,
                            language = language,
                            onRoleClick = {
                                // Cycle through roles: Customer -> Rider -> Restaurant -> Admin
                                val nextRole = when (user.role) {
                                    UserRole.CUSTOMER -> UserRole.RIDER
                                    UserRole.RIDER -> UserRole.RESTAURANT
                                    UserRole.RESTAURANT -> UserRole.ADMIN
                                    UserRole.ADMIN -> UserRole.CUSTOMER
                                }
                                repository.switchUserRole(nextRole)
                            },
                            onLanguageClick = { repository.toggleLanguage() },
                            onCartClick = if (user.role == UserRole.CUSTOMER) {
                                {
                                    selectedRestaurant = null
                                    isCheckoutActive = false
                                    currentCustomerTab = CustomerScreen.CART
                                }
                            } else null,
                            cartItemCount = cartItems.sumOf { it.quantity }
                        )
                    },
                    bottomBar = {
                        if (user.role == UserRole.CUSTOMER && !isCheckoutActive) {
                            WafaBottomBar(
                                currentScreen = currentCustomerTab,
                                cartItemCount = cartItems.sumOf { it.quantity },
                                onScreenSelected = { tab ->
                                    selectedRestaurant = null
                                    currentCustomerTab = tab
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (user.role) {
                            UserRole.CUSTOMER -> {
                                when {
                                    isCheckoutActive -> {
                                        val activeRestaurant = selectedRestaurant ?: repository.restaurants.first()
                                        CheckoutScreen(
                                            user = user,
                                            restaurant = activeRestaurant,
                                            cartItems = cartItems,
                                            discountBdt = checkoutDiscount,
                                            onBackClick = { isCheckoutActive = false },
                                            onConfirmOrder = { address, lat, lng, instructions, paymentMethod ->
                                                val created = repository.placeOrder(
                                                    restaurant = activeRestaurant,
                                                    deliveryAddress = address,
                                                    customerLat = lat,
                                                    customerLng = lng,
                                                    deliveryInstructions = instructions,
                                                    paymentMethod = paymentMethod
                                                )
                                                isCheckoutActive = false
                                                currentCustomerTab = CustomerScreen.TRACKING
                                            }
                                        )
                                    }

                                    selectedRestaurant != null -> {
                                        RestaurantDetailScreen(
                                            restaurant = selectedRestaurant!!,
                                            foods = repository.foods,
                                            onBackClick = { selectedRestaurant = null },
                                            onFoodClick = { food -> selectedFoodForCustomization = food }
                                        )
                                    }

                                    currentCustomerTab == CustomerScreen.HOME -> {
                                        CustomerHomeScreen(
                                            restaurants = repository.restaurants,
                                            foods = repository.foods,
                                            categories = repository.categories,
                                            activeOrder = activeOrder,
                                            points = user.points,
                                            onRestaurantClick = { rest -> selectedRestaurant = rest },
                                            onFoodClick = { food -> selectedFoodForCustomization = food },
                                            onTrackOrderClick = { order -> currentCustomerTab = CustomerScreen.TRACKING }
                                        )
                                    }

                                    currentCustomerTab == CustomerScreen.CART -> {
                                        CartScreen(
                                            cartItems = cartItems,
                                            deliveryFee = 40.0,
                                            onQuantityChange = { item, qty -> repository.updateCartItemQuantity(item, qty) },
                                            onClearCart = { repository.clearCart() },
                                            onProceedToCheckout = { discount, promo ->
                                                checkoutDiscount = discount
                                                isCheckoutActive = true
                                            }
                                        )
                                    }

                                    currentCustomerTab == CustomerScreen.TRACKING -> {
                                        val displayOrder = activeOrder ?: orders.firstOrNull()
                                        if (displayOrder != null) {
                                            LiveTrackingScreen(
                                                order = displayOrder,
                                                riderGps = liveRiderLocation,
                                                onBackClick = { currentCustomerTab = CustomerScreen.HOME },
                                                onViewInvoiceClick = { viewingInvoice = repository.generateInvoice(displayOrder) },
                                                onRateClick = { ratingOrderTarget = displayOrder }
                                            )
                                        } else {
                                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                androidx.compose.material3.Text("No current active orders to track.")
                                            }
                                        }
                                    }

                                    currentCustomerTab == CustomerScreen.PROFILE -> {
                                        CustomerProfileScreen(
                                            user = user,
                                            pointsHistory = pointsHistory,
                                            supportTickets = supportTickets,
                                            language = language,
                                            onRoleChange = { repository.switchUserRole(it) },
                                            onLanguageToggle = { repository.toggleLanguage() },
                                            onCreateSupportTicket = { subj, cat, msg ->
                                                repository.createSupportTicket(subj, cat, msg, "Normal", activeOrder?.id)
                                            }
                                        )
                                    }
                                }
                            }

                            UserRole.RIDER -> {
                                RiderDashboardScreen(
                                    rider = user,
                                    activeOrder = activeOrder ?: orders.firstOrNull(),
                                    orders = orders,
                                    riderNotes = riderNotes,
                                    riderGps = liveRiderLocation,
                                    onStartDelivery = { order ->
                                        repository.updateOrderStatus(order.id, OrderStatus.ON_THE_WAY, "RIDER")
                                    },
                                    onCompleteDelivery = { order, otp ->
                                        repository.updateOrderStatus(order.id, OrderStatus.DELIVERED, "RIDER", otp)
                                    },
                                    onAddNote = { orderId, title, details, category, priority, privacy ->
                                        repository.addRiderNote(
                                            orderId,
                                            title,
                                            details,
                                            category,
                                            priority,
                                            privacy,
                                            liveRiderLocation?.latitude,
                                            liveRiderLocation?.longitude
                                        )
                                    }
                                )
                            }

                            UserRole.RESTAURANT -> {
                                val restaurantProfile = repository.restaurants.first()
                                RestaurantDashboardScreen(
                                    restaurant = restaurantProfile,
                                    orders = orders,
                                    foods = repository.foods,
                                    onUpdateOrderStatus = { order, newStatus ->
                                        repository.updateOrderStatus(order.id, newStatus, "RESTAURANT")
                                    }
                                )
                            }

                            UserRole.ADMIN -> {
                                AdminDashboardScreen(
                                    orders = orders,
                                    aiProviders = aiProviders,
                                    auditLogs = auditLogs,
                                    isAiEnabled = isAiEnabled,
                                    isOrderingPaused = isOrderingPaused,
                                    onToggleAi = { repository.toggleAiEnabled() },
                                    onTogglePauseOrders = { repository.setOrderingPaused(it) },
                                    onSelectAiProvider = { repository.setActiveAiProvider(it) },
                                    onTestAiProvider = { providerId, callback ->
                                        repository.testAiProvider(providerId, callback)
                                    }
                                )
                            }
                        }

                        // Floating AI Assistant Button (when enabled)
                        if (isAiEnabled) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(bottom = if (user.role == UserRole.CUSTOMER && !isCheckoutActive) 70.dp else 16.dp)
                            ) {
                                GlobalAiFab(isAiEnabled = isAiEnabled, onClick = { showAiSheet = true })
                            }
                        }

                        // Modals
                        if (showAiSheet) {
                            AiAssistantSheet(
                                activeOrder = activeOrder,
                                onDismiss = { showAiSheet = false },
                                onSelectFoodPrompt = { promptCat ->
                                    showAiSheet = false
                                }
                            )
                        }

                        if (selectedFoodForCustomization != null) {
                            FoodDetailDialog(
                                food = selectedFoodForCustomization!!,
                                onDismiss = { selectedFoodForCustomization = null },
                                onAddToCart = { qty, variant, addons, instructions ->
                                    repository.addToCart(selectedFoodForCustomization!!, qty, variant, addons, instructions)
                                }
                            )
                        }

                        if (viewingInvoice != null) {
                            InvoiceCard(
                                invoice = viewingInvoice!!,
                                onDismiss = { viewingInvoice = null }
                            )
                        }

                        if (ratingOrderTarget != null) {
                            RatingDialog(
                                order = ratingOrderTarget!!,
                                onDismiss = { ratingOrderTarget = null },
                                onSubmit = { riderStars, restStars, tags, review ->
                                    repository.addPoints(user.id, UserRole.CUSTOMER, 25, "Review Reward Points", ratingOrderTarget!!.id)
                                    ratingOrderTarget = null
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
