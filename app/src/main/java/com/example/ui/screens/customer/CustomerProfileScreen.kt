package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.PointLedgerEntry
import com.example.data.model.SupportTicket
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun CustomerProfileScreen(
    user: User,
    pointsHistory: List<PointLedgerEntry>,
    supportTickets: List<SupportTicket>,
    language: String,
    onRoleChange: (UserRole) -> Unit,
    onLanguageToggle: () -> Unit,
    onCreateSupportTicket: (subject: String, category: String, message: String) -> Unit
) {
    var showTicketDialog by remember { mutableStateOf(false) }
    var ticketSubject by remember { mutableStateOf("") }
    var ticketCategory by remember { mutableStateOf("Food Quality") }
    var ticketMessage by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp, bottom = 100.dp)
    ) {
        // User Profile Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(WafaOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.name.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(user.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(user.phone, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("ID: ${user.id}", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = WafaOrange)
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = WafaAmber.copy(alpha = 0.2f)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("${user.points}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFE65100))
                            Text("Points", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                        }
                    }
                }
            }
        }

        // App Role Switcher
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Switch App Role (পরীক্ষার জন্য রোল পরিবর্তন)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UserRole.values().forEach { role ->
                    val isSelected = user.role == role
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) WafaOrange else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onRoleChange(role) }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (role) {
                                    UserRole.CUSTOMER -> Icons.Default.Person
                                    UserRole.RIDER -> Icons.Default.TwoWheeler
                                    UserRole.RESTAURANT -> Icons.Default.Restaurant
                                    UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                },
                                contentDescription = role.name,
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = role.name.take(4),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Points Ledger History
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Points Ledger (পয়েন্ট হিস্ট্রি)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Total: ${user.points} ৳", fontSize = 12.sp, color = WafaOrange, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(pointsHistory) { entry ->
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
                    Column {
                        Text(entry.reason, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        if (entry.orderId != null) {
                            Text("Order #${entry.orderId}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Text(
                        text = if (entry.amount >= 0) "+${entry.amount}" else "${entry.amount}",
                        color = if (entry.amount >= 0) WafaGreen else MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Support & Disputes Section
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Support & Disputes (সাপোর্ট টিকেট)", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = { showTicketDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = WafaOrange),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ New Ticket", fontSize = 11.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(supportTickets) { ticket ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(ticket.subject, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = WafaOrange.copy(alpha = 0.15f)
                        ) {
                            Text(
                                ticket.status,
                                fontSize = 10.sp,
                                color = WafaOrange,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(ticket.message, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }

    if (showTicketDialog) {
        AlertDialog(
            onDismissRequest = { showTicketDialog = false },
            title = { Text("Open Support Ticket") },
            text = {
                Column {
                    OutlinedTextField(
                        value = ticketSubject,
                        onValueChange = { ticketSubject = it },
                        label = { Text("Subject (e.g. Missing Item)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = ticketMessage,
                        onValueChange = { ticketMessage = it },
                        label = { Text("Detailed description of issue") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (ticketSubject.isNotEmpty() && ticketMessage.isNotEmpty()) {
                            onCreateSupportTicket(ticketSubject, ticketCategory, ticketMessage)
                            showTicketDialog = false
                            ticketSubject = ""
                            ticketMessage = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WafaOrange)
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTicketDialog = false }) { Text("Cancel") }
            }
        )
    }
}
