package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    deliveryFee: Double,
    onQuantityChange: (CartItem, Int) -> Unit,
    onClearCart: () -> Unit,
    onProceedToCheckout: (discount: Double, promoCode: String) -> Unit
) {
    var promoCode by remember { mutableStateOf("") }
    var appliedDiscount by remember { mutableDoubleStateOf(0.0) }
    var promoMessage by remember { mutableStateOf("") }

    val subtotal = cartItems.sumOf { it.totalPrice }
    val vat = (subtotal * 0.05).toInt().toDouble()
    val finalTotal = (subtotal + deliveryFee + vat - appliedDiscount).coerceAtLeast(0.0)

    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Empty Cart",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Your Cart is Empty", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Add some delicious food from restaurants to order!", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Your Food Cart (${cartItems.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onClearCart) {
                Text("Clear All", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(cartItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.foodItem.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            if (item.selectedVariant != null) {
                                Text("Variant: ${item.selectedVariant.name}", fontSize = 11.sp, color = WafaOrange)
                            }
                            if (item.selectedAddons.isNotEmpty()) {
                                Text("Add-ons: ${item.selectedAddons.joinToString { it.name }}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("৳%.0f".format(item.totalPrice), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = WafaOrange)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(2.dp)
                        ) {
                            IconButton(
                                onClick = { onQuantityChange(item, item.quantity - 1) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Remove, "Minus", modifier = Modifier.size(14.dp))
                            }
                            Text("${item.quantity}", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 6.dp))
                            IconButton(
                                onClick = { onQuantityChange(item, item.quantity + 1) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Add, "Plus", modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }

            // Promo code field
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = promoCode,
                        onValueChange = { promoCode = it },
                        placeholder = { Text("Coupon code (e.g. WAFA20)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (promoCode.trim().equals("WAFA20", ignoreCase = true)) {
                                appliedDiscount = subtotal * 0.20
                                promoMessage = "⚡ 20% Discount applied!"
                            } else {
                                promoMessage = "Invalid coupon code"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Apply")
                    }
                }
                if (promoMessage.isNotEmpty()) {
                    Text(
                        text = promoMessage,
                        fontSize = 11.sp,
                        color = if (appliedDiscount > 0) WafaGreen else MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            // Summary Breakdown
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", fontSize = 12.sp)
                            Text("৳%.0f".format(subtotal), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        if (appliedDiscount > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Discount (WAFA20)", fontSize = 12.sp, color = WafaGreen)
                                Text("-৳%.0f".format(appliedDiscount), fontSize = 12.sp, color = WafaGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Delivery Fee", fontSize = 12.sp)
                            Text("৳%.0f".format(deliveryFee), fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Govt. VAT (5%)", fontSize = 12.sp)
                            Text("৳%.0f".format(vat), fontSize = 12.sp)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Amount", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("৳%.0f".format(finalTotal), fontSize = 16.sp, fontWeight = FontWeight.Black, color = WafaOrange)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onProceedToCheckout(appliedDiscount, promoCode) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Proceed to Checkout • ৳%.0f".format(finalTotal), fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}
