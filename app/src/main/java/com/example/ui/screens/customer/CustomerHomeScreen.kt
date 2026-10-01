package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.FoodItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.Restaurant
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun CustomerHomeScreen(
    restaurants: List<Restaurant>,
    foods: List<FoodItem>,
    categories: List<String>,
    activeOrder: Order?,
    points: Int,
    onRestaurantClick: (Restaurant) -> Unit,
    onFoodClick: (FoodItem) -> Unit,
    onTrackOrderClick: (Order) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "") }

    val filteredFoods = remember(searchQuery, selectedCategory, foods) {
        foods.filter { food ->
            val matchesCategory = selectedCategory.contains("All") || selectedCategory.contains("সব") || food.category == selectedCategory
            val matchesSearch = searchQuery.isEmpty() || food.name.contains(searchQuery, ignoreCase = true) || food.nameBn.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Location & Points Banner
        item {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, "Location", tint = WafaOrange, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("DELIVERING TO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Dhanmondi, Dhaka", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = WafaAmber.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Stars, "Points", tint = Color(0xFFE65100), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("$points Pts", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                        }
                    }
                }
            }
        }

        // Active Order Banner (If any)
        if (activeOrder != null && activeOrder.status != OrderStatus.DELIVERED && activeOrder.status != OrderStatus.CANCELLED) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { onTrackOrderClick(activeOrder) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E222D)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(WafaOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.TwoWheeler, "Rider", tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Active Delivery in Progress ⚡", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(activeOrder.status.displayTitle, color = WafaOrange, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Button(
                            onClick = { onTrackOrderClick(activeOrder) },
                            colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Track Live", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search delicious foods, biryani, burgers...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = WafaOrange,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
        }

        // Promotional Hero Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1550547660-d9450f859349?w=800",
                        contentDescription = "Offer Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent)
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = WafaOrange
                        ) {
                            Text(
                                "WAFA SPEED DELIVERY ⚡",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("২০% ছাড় ও ফ্রি ডেলিভারি!", fontSize = 17.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Text("Use Promo Code: WAFA20 at checkout", fontSize = 11.sp, color = WafaAmber)
                    }
                }
            }
        }

        // Category Pills
        item {
            Text(
                "ক্যাটাগরি (Categories)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 8.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) WafaOrange else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedCategory = category }
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Featured Restaurants
        item {
            Text(
                "জনপ্রিয় রেস্তোরাঁ (Popular Restaurants)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 18.dp, bottom = 8.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(restaurants) { restaurant ->
                    Card(
                        modifier = Modifier
                            .width(220.dp)
                            .clickable { onRestaurantClick(restaurant) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column {
                            AsyncImage(
                                model = restaurant.coverUrl,
                                contentDescription = restaurant.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(restaurant.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                Text(restaurant.cuisine, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, "Rating", tint = WafaAmber, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("${restaurant.rating}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text("⚡ ${restaurant.deliveryTimeMinutes} mins", fontSize = 11.sp, color = WafaOrange, fontWeight = FontWeight.SemiBold)
                                    Text("৳%.0f fee".format(restaurant.deliveryFeeBdt), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Foods Section
        item {
            Text(
                "খাবার তালিকা (Top Menu Items)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
            )
        }

        items(filteredFoods) { food ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onFoodClick(food) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = food.imageUrl,
                        contentDescription = food.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(food.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(food.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "৳%.0f".format(food.discountPriceBdt ?: food.priceBdt),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = WafaOrange
                            )
                            if (food.discountPriceBdt != null) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "৳%.0f".format(food.priceBdt),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodySmall.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                                )
                            }
                        }
                    }
                    Button(
                        onClick = { onFoodClick(food) },
                        colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("+ Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
