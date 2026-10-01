package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.WafaOrange

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RiderNoteDialog(
    orderId: String,
    currentLat: Double?,
    currentLng: Double?,
    onDismiss: () -> Unit,
    onSubmit: (title: String, details: String, category: String, priority: String, privacy: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Road Issue") }
    var selectedPriority by remember { mutableStateOf("Normal") }
    var selectedPrivacy by remember { mutableStateOf("Customer Visible") }

    val categories = listOf("Road Issue", "Customer Delay", "Location Unclear", "Restaurant Wait", "Heavy Rain", "Gate Locked")
    val priorities = listOf("Normal", "Important")
    val privacies = listOf("Customer Visible", "Admin Only", "Rider Visible")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text("Add Delivery Note ⚡", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "GPS Tag: ${if (currentLat != null) "%.4f, %.4f".format(currentLat, currentLng) else "Detecting..."}",
                    fontSize = 11.sp,
                    color = WafaOrange
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Note Title (e.g. Flyover closed)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Details & Instructions") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Category", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (selectedCategory == cat) WafaOrange.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable {
                                selectedCategory = cat
                                if (title.isEmpty()) title = cat
                            }
                        ) {
                            Text(
                                cat,
                                fontSize = 11.sp,
                                color = if (selectedCategory == cat) WafaOrange else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Priority
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Priority:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    priorities.forEach { p ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selectedPriority == p) WafaOrange.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .clickable { selectedPriority = p }
                        ) {
                            Text(
                                p,
                                fontSize = 11.sp,
                                color = if (selectedPriority == p) WafaOrange else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotEmpty()) {
                                onSubmit(title, details, selectedCategory, selectedPriority, selectedPrivacy)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WafaOrange)
                    ) {
                        Text("Save Note")
                    }
                }
            }
        }
    }
}
