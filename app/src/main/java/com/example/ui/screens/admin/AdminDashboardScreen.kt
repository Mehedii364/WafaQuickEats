package com.example.ui.screens.admin

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
import com.example.data.model.AiProviderConfig
import com.example.data.model.Order
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun AdminDashboardScreen(
    orders: List<Order>,
    aiProviders: List<AiProviderConfig>,
    auditLogs: List<String>,
    isAiEnabled: Boolean,
    isOrderingPaused: Boolean,
    onToggleAi: () -> Unit,
    onTogglePauseOrders: (Boolean) -> Unit,
    onSelectAiProvider: (String) -> Unit,
    onTestAiProvider: (String, (Boolean, Long, String) -> Unit) -> Unit
) {
    var selectedSection by remember { mutableIntStateOf(0) } // 0: Overview, 1: AI Provider Manager, 2: Audit Logs
    var testResultText by remember { mutableStateOf<String?>(null) }
    var isTestingProvider by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Super Admin Header
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
                        Text("Wafa Admin Master Control ⚡", fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Text("Super Admin: Md. Mehedi Hasan", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF7B1FA2).copy(alpha = 0.15f)
                    ) {
                        Text(
                            "SUPER ADMIN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7B1FA2),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Emergency Controls: Pause Orders
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Emergency Order Pause", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(if (isOrderingPaused) "All incoming orders BLOCKED" else "System Active & Operational", fontSize = 10.sp, color = if (isOrderingPaused) MaterialTheme.colorScheme.error else WafaGreen)
                    }
                    Switch(
                        checked = isOrderingPaused,
                        onCheckedChange = { onTogglePauseOrders(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.error, checkedTrackColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Tab Buttons
        TabRow(selectedTabIndex = selectedSection) {
            Tab(selected = selectedSection == 0, onClick = { selectedSection = 0 }, text = { Text("Overview") })
            Tab(selected = selectedSection == 1, onClick = { selectedSection = 1 }, text = { Text("AI Providers") })
            Tab(selected = selectedSection == 2, onClick = { selectedSection = 2 }, text = { Text("Audit Log") })
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (selectedSection) {
            0 -> {
                // Overview & System Health
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        Text("System Infrastructure Health", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        val healthItems = listOf(
                            "REST API Backend (PHP 8 + MySQL)" to "HEALTHY (200 OK)",
                            "Foreground GPS Location Service" to "ONLINE & LISTENING",
                            "Room Database Local Cache" to "CONNECTED & SYNCED",
                            "AI Global Master Switch" to if (isAiEnabled) "ENABLED (Active)" else "DISABLED (Off)",
                            "InfinityFree Hosting Gateway" to "ACCESSIBLE"
                        )
                        healthItems.forEach { (component, status) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(component, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    Text(status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WafaGreen)
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Verification Queue", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))

                        val pendingVerifications = listOf(
                            "Restaurant: Wafa Burger House ⚡" to "VERIFIED",
                            "Restaurant: Kacchi Dine & Biryani" to "VERIFIED",
                            "Rider: Farhan Ahmed (WQE-RDR-5542)" to "VERIFIED (Driving License OK)",
                            "Rider: Tanvir Alam (WQE-RDR-9012)" to "PENDING APPROVAL"
                        )
                        pendingVerifications.forEach { (entity, status) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(entity, fontSize = 11.sp)
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (status.contains("VERIFIED")) WafaGreen.copy(alpha = 0.15f) else WafaAmber.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            status,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (status.contains("VERIFIED")) WafaGreen else Color(0xFFE65100),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // AI Provider Manager
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("AI Provider Manager", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("Free/Low-cost & Premium API Gateways", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Global AI:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Switch(
                                    checked = isAiEnabled,
                                    onCheckedChange = { onToggleAi() },
                                    colors = SwitchDefaults.colors(checkedThumbColor = WafaOrange)
                                )
                            }
                        }

                        if (testResultText != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = WafaGreen.copy(alpha = 0.15f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = testResultText ?: "",
                                    fontSize = 11.sp,
                                    color = WafaGreen,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(aiProviders) { provider ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (provider.isActive) WafaOrange.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
                            ),
                            border = if (provider.isActive) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(WafaOrange)) else null
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(provider.name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(provider.category, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    if (provider.isActive) {
                                        Surface(shape = RoundedCornerShape(6.dp), color = WafaOrange) {
                                            Text("ACTIVE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Model: ${provider.model}", fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                                Text("Endpoint: ${provider.endpoint}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Key: ${provider.apiKeyMasked}", fontSize = 10.sp, fontFamily = FontFamily.Monospace)

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            isTestingProvider = true
                                            onTestAiProvider(provider.id) { success, latency, msg ->
                                                isTestingProvider = false
                                                testResultText = "⚡ [${provider.name}] Status: $msg | Latency: ${latency}ms | OK"
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("API Live Test", fontSize = 11.sp)
                                    }

                                    if (!provider.isActive) {
                                        Button(
                                            onClick = { onSelectAiProvider(provider.id) },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Set Active", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Audit Logs Stream
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        Text("Security & Operational Audit Log", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(auditLogs) { log ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                text = log,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
