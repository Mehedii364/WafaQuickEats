package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Invoice
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange

@Composable
fun InvoiceCard(
    invoice: Invoice,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with Wafa Branding
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(WafaOrange),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Bolt, "Logo", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("WAFA QUICKEATS ⚡", fontWeight = FontWeight.Black, fontSize = 14.sp, color = WafaOrange)
                        Text("Official Digital Tax Receipt", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Surface(
                    color = WafaGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, "Paid", tint = WafaGreen, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(invoice.paymentStatus.uppercase(), color = WafaGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Invoice details
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("INVOICE NUMBER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(invoice.invoiceNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("DATE & TIME", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(invoice.date, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Parties
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("RESTAURANT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(invoice.restaurantName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text(invoice.restaurantAddress, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text("BILLED TO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(invoice.customerName, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text(invoice.customerPhone, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Items List
            Text("ORDER ITEMS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(6.dp))
            invoice.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.quantity}x ${item.foodItem.name}", fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text("৳%.0f".format(item.totalPrice), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            // Summary math
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("৳%.0f".format(invoice.subtotalBdt), fontSize = 11.sp)
            }
            if (invoice.discountBdt > 0) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Discount (Promo)", fontSize = 11.sp, color = WafaGreen)
                    Text("-৳%.0f".format(invoice.discountBdt), fontSize = 11.sp, color = WafaGreen)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Delivery Fee", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("৳%.0f".format(invoice.deliveryFeeBdt), fontSize = 11.sp)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("VAT / Taxes (5%)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("৳%.0f".format(invoice.vatTaxBdt), fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Paid", fontSize = 15.sp, fontWeight = FontWeight.Black)
                Text("৳%.0f".format(invoice.totalBdt), fontSize = 16.sp, fontWeight = FontWeight.Black, color = WafaOrange)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Payment and Rider note
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Payment: ${invoice.paymentMethod}", fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    if (invoice.riderName != null) {
                        Text("Delivered by: ${invoice.riderName}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = WafaOrange)
            ) {
                Text("Close Receipt")
            }
        }
    }
}
