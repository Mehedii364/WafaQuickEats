package com.example.ui.screens.customer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.data.model.GpsLocation
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.ui.components.LiveTrackingMapCanvas
import com.example.ui.components.OrderProgressBar
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun LiveTrackingScreen(
    order: Order,
    riderGps: GpsLocation?,
    onBackClick: () -> Unit,
    onViewInvoiceClick: () -> Unit,
    onRateClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text("Live Order Tracking ⚡", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text(order.id, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = FontFamily.Monospace)
                }
            }
            IconButton(onClick = onViewInvoiceClick) {
                Icon(Icons.Default.Receipt, contentDescription = "Invoice", tint = WafaOrange)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Live Road Map
            LiveTrackingMapCanvas(order = order, riderGps = riderGps)

            Spacer(modifier = Modifier.height(14.dp))

            // Order Status Stepper
            OrderProgressBar(currentStatus = order.status)

            Spacer(modifier = Modifier.height(10.dp))

            // Delivery Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ESTIMATED ARRIVAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                if (order.status == OrderStatus.DELIVERED) "Delivered!" else "${order.estimatedMinutes} Minutes",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = if (order.status == OrderStatus.DELIVERED) WafaGreen else WafaOrange
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = WafaOrange.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = order.status.displayTitle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = WafaOrange,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Rider Info Card with Call button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(WafaOrange.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.TwoWheeler, "Rider", tint = WafaOrange, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(order.riderName ?: "Assigned Rider ⚡", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Rating: 4.9 ★ • Fast delivery hero", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        if (order.riderPhone != null) {
                            IconButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${order.riderPhone}"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(WafaGreen)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Call Rider", tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // OTP Delivery Verification Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "DELIVERY VERIFICATION OTP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = order.otp,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 6.sp,
                        color = WafaOrange
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Share this 4-digit code with the rider when receiving your food for secure confirmation.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Order Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewInvoiceClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Receipt, "Receipt", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Digital Receipt", fontSize = 12.sp)
                }

                if (order.status == OrderStatus.DELIVERED) {
                    Button(
                        onClick = onRateClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Star, "Rate", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Rate Order (+25 Pts)", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
