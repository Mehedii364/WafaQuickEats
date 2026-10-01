package com.example.ui.screens.rider

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.location.RiderLocationService
import com.example.data.model.GpsLocation
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.RiderNote
import com.example.data.model.User
import com.example.ui.components.LiveTrackingMapCanvas
import com.example.ui.components.RiderNoteDialog
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun RiderDashboardScreen(
    rider: User,
    activeOrder: Order?,
    orders: List<Order>,
    riderNotes: List<RiderNote>,
    riderGps: GpsLocation?,
    onStartDelivery: (Order) -> Unit,
    onCompleteDelivery: (Order, String) -> Boolean,
    onAddNote: (orderId: String, title: String, details: String, category: String, priority: String, privacy: String) -> Unit
) {
    val context = LocalContext.current
    var isOnline by remember { mutableStateOf(true) }
    var isGpsTrackingActive by remember { mutableStateOf(activeOrder?.status == OrderStatus.ON_THE_WAY || activeOrder?.status == OrderStatus.PICKED_UP) }
    var showOtpDialog by remember { mutableStateOf(false) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var otpInput by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, bottom = 100.dp)
    ) {
        // Rider Header & Online Toggle
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(WafaOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.TwoWheeler, "Rider", tint = Color.White, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(rider.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("ID: ${rider.id}", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = WafaOrange)
                            }
                        }

                        // Online Switch
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (isOnline) "ONLINE" else "OFFLINE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isOnline) WafaGreen else MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = isOnline,
                                onCheckedChange = { isOnline = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = WafaGreen, checkedTrackColor = WafaGreen.copy(alpha = 0.3f))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Rider Earnings & Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("TODAY'S EARNINGS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("৳১,৪৫০", fontSize = 16.sp, fontWeight = FontWeight.Black, color = WafaOrange)
                            }
                        }
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("DELIVERIES", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("8 Orders", fontSize = 16.sp, fontWeight = FontWeight.Black)
                            }
                        }
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("RATING", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, "Rating", tint = WafaAmber, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("4.95", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Delivery Card (Core GPS Tracking)
        if (activeOrder != null && activeOrder.status != OrderStatus.DELIVERED && activeOrder.status != OrderStatus.CANCELLED) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isGpsTrackingActive) WafaGreen.copy(alpha = 0.15f) else WafaAmber.copy(alpha = 0.2f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isGpsTrackingActive) Icons.Default.GpsFixed else Icons.Default.GpsNotFixed,
                                            contentDescription = "GPS",
                                            tint = if (isGpsTrackingActive) WafaGreen else Color(0xFFE65100),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            if (isGpsTrackingActive) "GPS BROADCAST LIVE ⚡" else "GPS IDLE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isGpsTrackingActive) WafaGreen else Color(0xFFE65100)
                                        )
                                    }
                                }
                            }
                            Text(activeOrder.status.displayTitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WafaOrange)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Road navigation details
                        Text("Pickup: ${activeOrder.restaurantName}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(activeOrder.restaurantAddress, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Drop-off: ${activeOrder.customerName}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(activeOrder.deliveryAddress, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (activeOrder.deliveryInstructions.isNotEmpty()) {
                            Text("Note: ${activeOrder.deliveryInstructions}", fontSize = 11.sp, color = WafaOrange)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Road map canvas
                        LiveTrackingMapCanvas(order = activeOrder, riderGps = riderGps)

                        Spacer(modifier = Modifier.height(12.dp))

                        // Controls: Start Delivery / Add Note / Complete OTP
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (!isGpsTrackingActive) {
                                Button(
                                    onClick = {
                                        isGpsTrackingActive = true
                                        onStartDelivery(activeOrder)
                                        RiderLocationService.startService(context, activeOrder.id, rider.id)
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = WafaGreen),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Navigation, "Start", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Start Delivery ⚡", fontSize = 12.sp)
                                }
                            } else {
                                Button(
                                    onClick = { showOtpDialog = true },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, "OTP", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Verify & Complete", fontSize = 12.sp)
                                }
                            }

                            OutlinedButton(
                                onClick = { showNoteDialog = true },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.NoteAdd, "Note", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Note", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Rider Notes Feed
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text("Operational Rider Notes (রাইডার নোট)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(riderNotes) { note ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(note.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = WafaAmber.copy(alpha = 0.2f)
                        ) {
                            Text(
                                note.category,
                                fontSize = 10.sp,
                                color = Color(0xFFE65100),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(note.details, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (note.latitude != null) {
                        Text(
                            "GPS: %.4f, %.4f".format(note.latitude, note.longitude),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            color = WafaOrange
                        )
                    }
                }
            }
        }
    }

    // OTP Verification Dialog
    if (showOtpDialog && activeOrder != null) {
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            title = { Text("Delivery Completion OTP") },
            text = {
                Column {
                    Text(
                        "Enter the 4-digit verification code provided by the customer to confirm real hand-over.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = {
                            if (it.length <= 4) otpInput = it
                            otpError = ""
                        },
                        placeholder = { Text("Enter 4-digit OTP") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    if (otpError.isNotEmpty()) {
                        Text(otpError, color = MaterialTheme.colorScheme.error, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val success = onCompleteDelivery(activeOrder, otpInput)
                        if (success) {
                            isGpsTrackingActive = false
                            RiderLocationService.stopService(context)
                            showOtpDialog = false
                            otpInput = ""
                        } else {
                            otpError = "Invalid OTP code. Please verify with customer."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WafaGreen)
                ) {
                    Text("Confirm Delivery")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOtpDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Rider Note Dialog
    if (showNoteDialog && activeOrder != null) {
        RiderNoteDialog(
            orderId = activeOrder.id,
            currentLat = riderGps?.latitude ?: 23.7820,
            currentLng = riderGps?.longitude ?: 90.4000,
            onDismiss = { showNoteDialog = false },
            onSubmit = { title, details, category, priority, privacy ->
                onAddNote(activeOrder.id, title, details, category, priority, privacy)
            }
        )
    }
}
