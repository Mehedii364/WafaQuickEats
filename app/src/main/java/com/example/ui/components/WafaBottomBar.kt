package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.WafaOrange

enum class CustomerScreen(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    CART("Cart", Icons.Default.ShoppingCart),
    TRACKING("Orders", Icons.Default.ReceiptLong),
    PROFILE("Profile", Icons.Default.Person)
}

@Composable
fun WafaBottomBar(
    currentScreen: CustomerScreen,
    cartItemCount: Int,
    onScreenSelected: (CustomerScreen) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        CustomerScreen.values().forEach { screen ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onScreenSelected(screen) },
                icon = {
                    if (screen == CustomerScreen.CART && cartItemCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = WafaOrange,
                                    contentColor = androidx.compose.ui.graphics.Color.White
                                ) {
                                    Text("$cartItemCount")
                                }
                            }
                        ) {
                            Icon(screen.icon, contentDescription = screen.title)
                        }
                    } else {
                        Icon(screen.icon, contentDescription = screen.title)
                    }
                },
                label = { Text(screen.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = WafaOrange,
                    selectedTextColor = WafaOrange,
                    indicatorColor = WafaOrange.copy(alpha = 0.15f)
                )
            )
        }
    }
}
