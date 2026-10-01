package com.example.ui.screens.restaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FoodItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.Restaurant
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun RestaurantDashboardScreen(
    restaurant: Restaurant,
    orders: List<Order>,
    foods: List<FoodItem>,
    onUpdateOrderStatus: (Order, OrderStatus) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Orders, 1: Menu
    val restaurantOrders = orders.filter { it.restaurantId == restaurant.id }
    val restaurantFoods = foods.filter { it.restaurantId == restaurant.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Restaurant Header
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
                    Column {
                        Text(restaurant.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("ID: ${restaurant.id}", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = WafaOrange)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = WafaGreen.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Storefront, "Open", tint = WafaGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("OPEN & ACCEPTING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WafaGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("TODAY'S ORDERS", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${restaurantOrders.size} Orders", fontSize = 15.sp, fontWeight = FontWeight.Black)
                        }
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("SALES VOLUME", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("৳${restaurantOrders.sumOf { it.totalBdt }.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.Black, color = WafaOrange)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs: Incoming Orders vs Menu Items
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Kitchen Queue (${restaurantOrders.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Menu (${restaurantFoods.size})", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedTab == 0) {
            // Orders List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                if (restaurantOrders.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            Text("No incoming orders currently.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                items(restaurantOrders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(order.id, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = WafaOrange.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        order.status.displayTitle,
                                        fontSize = 10.sp,
                                        color = WafaOrange,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Customer: ${order.customerName} (${order.customerPhone})", fontSize = 12.sp)
                            Text("Address: ${order.deliveryAddress}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            order.items.forEach { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${item.quantity}x ${item.foodItem.name}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text("৳%.0f".format(item.totalPrice), fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Order Status Stepper Action buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                when (order.status) {
                                    OrderStatus.PENDING, OrderStatus.CONFIRMED -> {
                                        Button(
                                            onClick = { onUpdateOrderStatus(order, OrderStatus.PREPARING) },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Accept & Cook 🍳", fontSize = 11.sp)
                                        }
                                    }
                                    OrderStatus.PREPARING -> {
                                        Button(
                                            onClick = { onUpdateOrderStatus(order, OrderStatus.READY_FOR_PICKUP) },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = WafaGreen),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Mark Ready for Rider ⚡", fontSize = 11.sp)
                                        }
                                    }
                                    else -> {
                                        Text(
                                            "Order handed to Rider / In Transit",
                                            fontSize = 11.sp,
                                            color = WafaGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Menu Items List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(restaurantFoods) { food ->
                    var isAvailable by remember { mutableStateOf(food.isAvailable) }
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(food.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("৳%.0f • Prep: ${food.preparationTimeMinutes}m".format(food.priceBdt), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(if (isAvailable) "Available" else "Sold Out", fontSize = 10.sp, color = if (isAvailable) WafaGreen else MaterialTheme.colorScheme.error)
                                Spacer(modifier = Modifier.width(6.dp))
                                Switch(
                                    checked = isAvailable,
                                    onCheckedChange = { isAvailable = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = WafaGreen, checkedTrackColor = WafaGreen.copy(alpha = 0.3f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
