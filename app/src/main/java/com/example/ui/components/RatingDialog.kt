package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Order
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaOrange

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatingDialog(
    order: Order,
    onDismiss: () -> Unit,
    onSubmit: (riderRating: Int, restaurantRating: Int, tags: List<String>, review: String) -> Unit
) {
    var riderStars by remember { mutableIntStateOf(5) }
    var restaurantStars by remember { mutableIntStateOf(5) }
    val selectedTags = remember { mutableStateListOf<String>() }
    var reviewText by remember { mutableStateOf("") }

    val quickTags = listOf(
        "Friendly Rider",
        "Lightning Fast ⚡",
        "Hot & Fresh Food",
        "Spill-proof Packaging",
        "Polite Communication"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text("Rate Your Experience ⚡", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Earn +25 Wafa Points for this review!", fontSize = 12.sp, color = WafaOrange)

                Spacer(modifier = Modifier.height(14.dp))

                // Rate Rider
                Text("Delivery Rider: ${order.riderName ?: "Rider"}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star $star",
                            tint = if (star <= riderStars) WafaAmber else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { riderStars = star }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Rate Restaurant
                Text("Food Quality: ${order.restaurantName}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star $star",
                            tint = if (star <= restaurantStars) WafaAmber else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { restaurantStars = star }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick feedback tags
                Text("Quick Feedback", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) WafaOrange.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable {
                                if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                            }
                        ) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                color = if (isSelected) WafaOrange else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("Write your review (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Skip")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSubmit(riderStars, restaurantStars, selectedTags.toList(), reviewText)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WafaOrange)
                    ) {
                        Text("Submit Review")
                    }
                }
            }
        }
    }
}
