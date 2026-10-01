package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun OrderProgressBar(currentStatus: OrderStatus) {
    val keyStages = listOf(
        OrderStatus.CONFIRMED to "Confirmed",
        OrderStatus.PREPARING to "Kitchen",
        OrderStatus.PICKED_UP to "Picked Up",
        OrderStatus.ON_THE_WAY to "On Way",
        OrderStatus.DELIVERED to "Delivered"
    )

    val currentStep = currentStatus.stepIndex

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            keyStages.forEachIndexed { index, (stage, label) ->
                val isCompleted = currentStep >= stage.stepIndex
                val isCurrent = currentStatus == stage

                // Step Circle
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> WafaGreen
                                isCurrent -> WafaOrange
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Done",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Connecting Line between steps
                if (index < keyStages.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .background(
                                if (currentStep > stage.stepIndex) WafaGreen
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            keyStages.forEach { (stage, label) ->
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = if (currentStatus == stage) FontWeight.Bold else FontWeight.Normal,
                    color = if (currentStatus == stage) WafaOrange else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
