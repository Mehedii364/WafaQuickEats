package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantSheet(
    activeOrder: Order?,
    onDismiss: () -> Unit,
    onSelectFoodPrompt: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var aiResponse by remember {
        mutableStateOf(
            if (activeOrder != null) {
                "⚡ Wafa AI Delivery Status:\nRider is currently en route on Gulshan-Banani connecting avenue. Estimated arrival in ${activeOrder.estimatedMinutes} minutes. Speed is 26 km/h. Everything is proceeding smoothly."
            } else {
                "⚡ Welcome to Wafa QuickEats AI Assistant! How may I assist you today? You can ask for food suggestions, calorie inquiries, or current promotional offers."
            }
        )
    }
    var isThinking by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(WafaOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Wafa AI Smart Assistant ⚡", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Powered by Configurable AI Providers", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Response Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bolt, "Lightning", tint = WafaOrange, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AI Insights", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WafaOrange)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    if (isThinking) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = WafaOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing request...", fontSize = 12.sp)
                        }
                    } else {
                        Text(
                            text = aiResponse,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Prompt Chips
            Text("Suggested Actions", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            val quickPrompts = listOf(
                "Best Burgers in Banani 🍔",
                "Why is my order taking time? ⏱️",
                "Recommend a meal under ৳500 💰",
                "Summarize delivery route & notes 📍"
            )
            quickPrompts.forEach { prompt ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable {
                            query = prompt
                            isThinking = true
                            // Simulate quick local response logic
                            when {
                                prompt.contains("Burgers") -> {
                                    aiResponse = "Recommended: Wafa Double Blast Beef Burger (৳310) from Wafa Burger House! Features juicy grilled beef patties, cheddar, and our signature sauce."
                                    onSelectFoodPrompt("বার্গার (Burgers)")
                                }
                                prompt.contains("taking time") -> {
                                    aiResponse = "Your food was prepared fresh in 14 mins. The rider is taking the optimal route avoiding flyover construction traffic. ETA remains within safety margins."
                                }
                                prompt.contains("under ৳500") -> {
                                    aiResponse = "Try the Shahi Mutton Kacchi Biryani (৳440) or Crispy Thunder Chicken Burger (৳250) + Belgian Chocolate Shake (৳220) = ৳470!"
                                }
                                prompt.contains("route") -> {
                                    aiResponse = "Rider Farhan Ahmed is navigating via Banani Road 11 towards Dhanmondi. Current speed: 26 km/h. Distance remaining: ~2.4 km."
                                }
                            }
                            isThinking = false
                        }
                ) {
                    Text(
                        text = prompt,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Ask anything about food or tracking...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (query.isNotEmpty()) {
                            aiResponse = "Wafa AI query processed: '$query'. Recommendation: Try our popular Wafa Double Blast Burger with fast 20-min delivery!"
                            query = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(WafaOrange)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
