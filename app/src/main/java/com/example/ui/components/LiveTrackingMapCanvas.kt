package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GpsLocation
import com.example.data.model.Order
import com.example.ui.theme.WafaAmber
import com.example.ui.theme.WafaGreen
import com.example.ui.theme.WafaOrange
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LiveTrackingMapCanvas(
    order: Order,
    riderGps: GpsLocation?,
    modifier: Modifier = Modifier
) {
    // Pulse animation for Rider GPS radar
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 18f,
        targetValue = 42f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E222D))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Draw subtle road grid
                val gridColor = Color(0xFF2C3240)
                for (x in 0..canvasWidth.toInt() step 60) {
                    drawLine(gridColor, Offset(x.toFloat(), 0f), Offset(x.toFloat(), canvasHeight), strokeWidth = 1f)
                }
                for (y in 0..canvasHeight.toInt() step 60) {
                    drawLine(gridColor, Offset(0f, y.toFloat()), Offset(canvasWidth, y.toFloat()), strokeWidth = 1f)
                }

                // Locations on screen:
                // Restaurant (Top Left)
                val restPos = Offset(canvasWidth * 0.22f, canvasHeight * 0.25f)
                // Customer (Bottom Right)
                val custPos = Offset(canvasWidth * 0.78f, canvasHeight * 0.75f)
                // Rider (interpolated on route between rest and cust)
                val riderLat = riderGps?.latitude ?: order.riderLat ?: order.restaurantLat
                val progress = 0.62f // Progress along the delivery road
                val riderPos = Offset(
                    restPos.x + (custPos.x - restPos.x) * progress,
                    restPos.y + (custPos.y - restPos.y) * progress + 25f
                )

                // Draw simulated Road Polyline Route (with bend)
                val roadWay1 = Offset(canvasWidth * 0.45f, canvasHeight * 0.28f)
                val roadWay2 = Offset(canvasWidth * 0.52f, canvasHeight * 0.65f)

                val routePath = Path().apply {
                    moveTo(restPos.x, restPos.y)
                    lineTo(roadWay1.x, roadWay1.y)
                    lineTo(riderPos.x, riderPos.y)
                    lineTo(roadWay2.x, roadWay2.y)
                    lineTo(custPos.x, custPos.y)
                }

                // Gray outer road border
                drawPath(
                    path = routePath,
                    color = Color(0xFF3F475A),
                    style = Stroke(width = 10f, cap = StrokeCap.Round)
                )

                // Active Orange Route line
                drawPath(
                    path = routePath,
                    color = WafaOrange,
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )

                // Restaurant Marker (Red/Amber pin)
                drawCircle(color = WafaAmber, radius = 12f, center = restPos)
                drawCircle(color = Color.White, radius = 5f, center = restPos)

                // Customer Destination Marker (Green pin)
                drawCircle(color = WafaGreen, radius = 12f, center = custPos)
                drawCircle(color = Color.White, radius = 5f, center = custPos)

                // Rider GPS Pulse Wave
                drawCircle(
                    color = WafaOrange.copy(alpha = pulseAlpha),
                    radius = pulseRadius,
                    center = riderPos
                )

                // Rider Icon Circle
                drawCircle(color = Color.White, radius = 14f, center = riderPos)
                drawCircle(color = WafaOrange, radius = 10f, center = riderPos)

                // Draw bearing direction pointer
                val bearingRad = Math.toRadians((riderGps?.bearing ?: 45f).toDouble())
                val arrowEnd = Offset(
                    (riderPos.x + 18f * sin(bearingRad)).toFloat(),
                    (riderPos.y - 18f * cos(bearingRad)).toFloat()
                )
                drawLine(
                    color = Color.Yellow,
                    start = riderPos,
                    end = arrowEnd,
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
            }

            // Legend Overlay (Top Left)
            Surface(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopStart),
                color = Color.Black.copy(alpha = 0.75f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(WafaAmber))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Kitchen", fontSize = 10.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(WafaOrange))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Rider ⚡", fontSize = 10.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(WafaGreen))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("You", fontSize = 10.sp, color = Color.White)
                }
            }

            // Telemetry Floating Pill (Bottom Center)
            Surface(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomCenter),
                color = Color(0xFF14171F).copy(alpha = 0.92f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Speed",
                        tint = WafaOrange,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.0f km/h".format(riderGps?.speed ?: 26f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = "GPS Accuracy",
                        tint = WafaGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Acc: %.0fm".format(riderGps?.accuracy ?: 5f),
                        fontSize = 11.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Bearing",
                        tint = WafaAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.0f°".format(riderGps?.bearing ?: 195f),
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
