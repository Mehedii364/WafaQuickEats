package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WafaTopBar(
    currentRole: UserRole,
    points: Int,
    language: String,
    onRoleClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onCartClick: (() -> Unit)? = null,
    cartItemCount: Int = 0
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(WafaOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Wafa Lightning",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "WAFA",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = WafaOrange
                        )
                        Text(
                            text = "QUICKEATS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                    Text(
                        text = "Wafa Zone by Mehedi364",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            // Language switch pill (BN / EN)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clickable { onLanguageClick() }
            ) {
                Text(
                    text = if (language == "bn") "বাংলা" else "EN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = WafaOrange,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Role Badge (Customer, Rider, Restaurant, Admin)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when (currentRole) {
                    UserRole.CUSTOMER -> WafaOrange.copy(alpha = 0.15f)
                    UserRole.RIDER -> WafaAmber.copy(alpha = 0.2f)
                    UserRole.RESTAURANT -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                    UserRole.ADMIN -> Color(0xFF9C27B0).copy(alpha = 0.15f)
                },
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clickable { onRoleClick() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = when (currentRole) {
                            UserRole.CUSTOMER -> Icons.Default.Person
                            UserRole.RIDER -> Icons.Default.TwoWheeler
                            UserRole.RESTAURANT -> Icons.Default.Restaurant
                            UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                        },
                        contentDescription = "Role",
                        tint = when (currentRole) {
                            UserRole.CUSTOMER -> WafaOrange
                            UserRole.RIDER -> Color(0xFFE65100)
                            UserRole.RESTAURANT -> Color(0xFF2E7D32)
                            UserRole.ADMIN -> Color(0xFF7B1FA2)
                        },
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentRole.name.take(3),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when (currentRole) {
                            UserRole.CUSTOMER -> WafaOrange
                            UserRole.RIDER -> Color(0xFFE65100)
                            UserRole.RESTAURANT -> Color(0xFF2E7D32)
                            UserRole.ADMIN -> Color(0xFF7B1FA2)
                        }
                    )
                }
            }

            // Cart Icon for Customer
            if (onCartClick != null) {
                IconButton(onClick = onCartClick) {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge(
                                    containerColor = WafaOrange,
                                    contentColor = Color.White
                                ) {
                                    Text("$cartItemCount")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart"
                        )
                    }
                }
            }
        }
    )
}
