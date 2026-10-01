package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.FoodAddon
import com.example.data.model.FoodItem
import com.example.data.model.FoodVariant
import com.example.ui.theme.WafaOrange

@Composable
fun FoodDetailDialog(
    food: FoodItem,
    onDismiss: () -> Unit,
    onAddToCart: (quantity: Int, variant: FoodVariant?, addons: List<FoodAddon>, instructions: String) -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    var selectedVariant by remember { mutableStateOf(food.variants.firstOrNull()) }
    val selectedAddons = remember { mutableStateListOf<FoodAddon>() }
    var instructions by remember { mutableStateOf("") }

    val currentTotal = remember(quantity, selectedVariant, selectedAddons.toList()) {
        val base = food.discountPriceBdt ?: food.priceBdt
        val variantExtra = selectedVariant?.extraPriceBdt ?: 0.0
        val addonsTotal = selectedAddons.sumOf { it.priceBdt }
        (base + variantExtra + addonsTotal) * quantity
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Top close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Food Image
                AsyncImage(
                    model = food.imageUrl,
                    contentDescription = food.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Title and price
                Text(food.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(food.nameBn, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "৳%.0f".format(food.discountPriceBdt ?: food.priceBdt),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = WafaOrange
                    )
                    if (food.discountPriceBdt != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "৳%.0f".format(food.priceBdt),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(food.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                // Variants if any
                if (food.variants.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Select Variant", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    food.variants.forEach { variant ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedVariant?.id == variant.id) WafaOrange.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { selectedVariant = variant }
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(variant.name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(
                                if (variant.extraPriceBdt > 0) "+৳%.0f".format(variant.extraPriceBdt) else "Included",
                                fontSize = 12.sp,
                                color = WafaOrange
                            )
                        }
                    }
                }

                // Add-ons if any
                if (food.addons.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Optional Add-ons", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    food.addons.forEach { addon ->
                        val isSelected = selectedAddons.any { it.id == addon.id }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) WafaOrange.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    if (isSelected) selectedAddons.removeAll { it.id == addon.id }
                                    else selectedAddons.add(addon)
                                }
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { checked ->
                                        if (checked) selectedAddons.add(addon)
                                        else selectedAddons.removeAll { it.id == addon.id }
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = WafaOrange)
                                )
                                Text(addon.name, fontSize = 13.sp)
                            }
                            Text("+৳%.0f".format(addon.priceBdt), fontSize = 12.sp, color = WafaOrange)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Special Instructions
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Special instructions (e.g. less spicy)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Quantity and Add to Cart Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Minus", modifier = Modifier.size(16.dp))
                        }
                        Text("$quantity", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Plus", modifier = Modifier.size(16.dp))
                        }
                    }

                    Button(
                        onClick = {
                            onAddToCart(quantity, selectedVariant, selectedAddons.toList(), instructions)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Add • ৳%.0f".format(currentTotal), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
