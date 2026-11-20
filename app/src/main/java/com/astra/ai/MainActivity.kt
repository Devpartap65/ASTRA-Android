
package com.astra.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AstraApp()
        }
    }
}

private val Navy = Color(0xFF172554)
private val Blue = Color(0xFF3978F6)
private val Purple = Color(0xFF8064E8)
private val Background = Color(0xFFF6F8FF)

@Composable
fun AstraApp() {
    var prompt by remember { mutableStateOf("") }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                Brush.linearGradient(
                                    listOf(Blue, Purple)
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "A",
                            color = Color.White,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            "ASTRA",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Navy
                        )
                        Text(
                            "Think. Create. Explore.",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Text("⋮", fontSize = 28.sp, color = Navy)
                }

                Spacer(Modifier.height(75.dp))

                Text(
                    "Hello, Explorer.",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "What would you like to discover today?",
                    fontSize = 15.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(30.dp))

                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Ask ASTRA anything...")
                    },
                    shape = RoundedCornerShape(22.dp),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blue,
                        unfocusedBorderColor = Color(0xFFDCE2F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                ) {
                    Text(
                        "Ask ASTRA  →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(36.dp))

                Text(
                    "EXPLORE YOUR CREATIVITY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    letterSpacing = 1.5.sp
                )

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ToolChip("⌘", "Code", Modifier.weight(1f))
                    ToolChip("✧", "Images", Modifier.weight(1f))
                    ToolChip("＋", "Create", Modifier.weight(1f))
                }

                Spacer(Modifier.weight(1f))

                Text(
                    "Your ideas. One intelligent companion.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ToolChip(
    symbol: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(78.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(symbol, fontSize = 23.sp, color = Blue)
            Text(
                label,
                fontSize = 13.sp,
                color = Navy,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
