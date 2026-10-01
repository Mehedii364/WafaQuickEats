package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.data.model.Restaurant
import com.example.data.model.User
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun CheckoutScreen(
    user: User,
    restaurant: Restaurant,
    cartItems: List<CartItem>,
    discountBdt: Double,
    onBackClick: () -> Unit,
    onConfirmOrder: (address: String, lat: Double, lng: Double, instructions: String, paymentMethod: String) -> Unit
) {
    var selectedLocationType by remember { mutableStateOf("Home") }
    var addressText by remember { mutableStateOf("House 12, Road 7, Dhanmondi, Dhaka") }
    var buildingDetails by remember { mutableStateOf("Apt 4B, Floor 4, Landmark: Near Star Kabab") }
    var deliveryInstructions by remember { mutableStateOf("Please ring bell twice or call before arriving.") }
    var selectedPaymentMethod by remember { mutableStateOf("bKash (বিকাশ)") }

    val paymentMethods = listOf(
        "bKash (বিকাশ)" to "Instant 2.0 Mobile Banking",
        "Nagad (নগদ)" to "Post Office Digital Wallet",
        "Rocket (রকেট)" to "DBBL Mobile Payment",
        "Cash on Delivery" to "Pay cash to rider upon OTP verification",
        "Credit / Debit Card" to "Visa, Mastercard, AMEX"
    )

    val subtotal = cartItems.sumOf { it.totalPrice }
    val deliveryFee = restaurant.deliveryFeeBdt
    val vat = (subtotal * 0.05).toInt().toDouble()
    val total = (subtotal + deliveryFee + vat - discountBdt).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Checkout ⚡", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Address Section
            Text("Delivery Address", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Home", "Office", "Other").forEach { type ->
                    val isSelected = selectedLocationType == type
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) WafaOrange.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedLocationType = type }
                    ) {
                        Text(
                            text = type,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) WafaOrange else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = addressText,
                onValueChange = { addressText = it },
                label = { Text("Street Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = buildingDetails,
                onValueChange = { buildingDetails = it },
                label = { Text("Building, Floor, Flat, Landmark") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = deliveryInstructions,
                onValueChange = { deliveryInstructions = it },
                label = { Text("Delivery note for Rider") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Methods
            Text("Payment Method", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            paymentMethods.forEach { (method, desc) ->
                val isSelected = selectedPaymentMethod == method
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedPaymentMethod = method },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) WafaOrange.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                    ),
                    border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(WafaOrange)) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedPaymentMethod = method },
                            colors = RadioButtonDefaults.colors(selectedColor = WafaOrange)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(method, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Order items summary
            Text("Order Summary (${cartItems.size} items)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    cartItems.forEach { item ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${item.quantity}x ${item.foodItem.name}", fontSize = 11.sp)
                            Text("৳%.0f".format(item.totalPrice), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Payable Total", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("৳%.0f".format(total), fontSize = 15.sp, fontWeight = FontWeight.Black, color = WafaOrange)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val fullAddress = "$addressText, $buildingDetails"
                onConfirmOrder(fullAddress, 23.7538, 90.3756, deliveryInstructions, selectedPaymentMethod)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Confirm & Place Order ⚡ (৳%.0f)".format(total), fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}
