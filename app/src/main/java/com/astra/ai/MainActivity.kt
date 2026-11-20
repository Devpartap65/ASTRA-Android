
package com.astra.ai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray

private val Blue = Color(0xFF4778F5)
private val Purple = Color(0xFF8965E8)
private val Navy = Color(0xFF172554)
private val Background = Color(0xFFF5F7FF)
private val Muted = Color(0xFF77819B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AstraApp()
        }
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@Composable
fun AstraApp() {
    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences("astra_data", Context.MODE_PRIVATE)
    }

    var screen by remember {
        mutableStateOf(
            preferences.getString("screen", "home") ?: "home"
        )
    }

    var language by remember {
        mutableStateOf(
            preferences.getString("language", "") ?: ""
        )
    }

    var input by remember { mutableStateOf("") }

    val messages = remember {
        mutableStateListOf<ChatMessage>().apply {
            val saved = preferences.getString("messages", "[]") ?: "[]"
            try {
                val array = JSONArray(saved)
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    add(
                        ChatMessage(
                            item.getString("text"),
                            item.getBoolean("user")
                        )
                    )
                }
            } catch (_: Exception) {}
        }
    }

    fun saveMessages() {
        val array = JSONArray()
        messages.forEach {
            val item = org.json.JSONObject()
            item.put("text", it.text)
            item.put("user", it.isUser)
            array.put(item)
        }
        preferences.edit().putString("messages", array.toString()).apply()
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Blue,
            secondary = Purple,
            background = Background,
            surface = Color.White
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {
            when {
                language.isEmpty() -> {
                    LanguageScreen { selected ->
                        language = selected
                        preferences.edit()
                            .putString("language", selected)
                            .apply()
                        screen = "home"
                    }
                }

                screen == "home" -> {
                    HomeScreen(
                        onAsk = { screen = "chat" },
                        onHistory = { screen = "history" },
                        onSettings = { screen = "settings" },
                        onTool = {
                            input = "Help me with $it"
                            screen = "chat"
                        }
                    )
                }

                screen == "chat" -> {
                    ChatScreen(
                        messages = messages,
                        input = input,
                        onInput = { input = it },
                        onBack = { screen = "home" },
                        onSend = {
                            val question = input.trim()
                            if (question.isNotEmpty()) {
                                messages.add(ChatMessage(question, true))
                                messages.add(
                                    ChatMessage(
                                        "Your question has been saved. Real AI responses will be activated when ASTRA's secure AI backend is connected.",
                                        false
                                    )
                                )
                                input = ""
                                saveMessages()
                            }
                        },
                        onVoice = {
                            input = "Voice input can be activated through Android speech recognition."
                        }
                    )
                }

                screen == "history" -> {
                    HistoryScreen(
                        messages = messages,
                        onBack = { screen = "home" },
                        onClear = {
                            messages.clear()
                            saveMessages()
                        },
                        onChat = { screen = "chat" }
                    )
                }

                screen == "settings" -> {
                    SettingsScreen(
                        language = language,
                        onBack = { screen = "home" },
                        onLanguage = { screen = "language" }
                    )
                }

                screen == "language" -> {
                    LanguageScreen { selected ->
                        language = selected
                        preferences.edit()
                            .putString("language", selected)
                            .apply()
                        screen = "settings"
                    }
                }
            }
        }
    }
}

@Composable
fun AstraLogo() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                Brush.linearGradient(listOf(Blue, Purple)),
                RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "A",
            color = Color.White,
            fontSize = 29.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun HomeScreen(
    onAsk: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit,
    onTool: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AstraLogo()
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "ASTRA",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Navy
                )
                Text(
                    "Think. Create. Explore.",
                    fontSize = 12.sp,
                    color = Muted
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                "⚙",
                fontSize = 25.sp,
                modifier = Modifier.clickable { onSettings() }
            )
        }

        Spacer(Modifier.height(55.dp))

        Text(
            "Hello, Explorer.",
            fontSize = 31.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "What would you like to discover today?",
            fontSize = 14.sp,
            color = Muted
        )

        Spacer(Modifier.height(25.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAsk() },
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    "Ask ASTRA anything...",
                    color = Muted,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(25.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        "＋  Attach     ",
                        color = Blue,
                        fontSize = 13.sp
                    )
                    Text(
                        "➤",
                        color = Color.White,
                        modifier = Modifier
                            .background(Blue, CircleShape)
                            .padding(12.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            "EXPLORE YOUR CREATIVITY",
            color = Muted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(Modifier.height(16.dp))

        val tools = listOf(
            "⌘" to "Code",
            "✧" to "Images",
            "◈" to "Games",
            "＋" to "Create"
        )

        tools.chunked(2).forEach { group ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                group.forEach { tool ->
                    ToolCard(
                        symbol = tool.first,
                        name = tool.second,
                        modifier = Modifier.weight(1f)
                    ) {
                        onTool(tool.second)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavigationText("⌂  Home") {}
            NavigationText("▤  History", onHistory)
            NavigationText("⚙  Settings", onSettings)
        }
    }
}

@Composable
fun ToolCard(
    symbol: String,
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(105.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(symbol, fontSize = 27.sp, color = Blue)
            Spacer(Modifier.height(8.dp))
            Text(
                name,
                color = Navy,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun NavigationText(label: String, onClick: () -> Unit) {
    Text(
        label,
        color = Blue,
        fontSize = 13.sp,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    input: String,
    onInput: (String) -> Unit,
    onBack: () -> Unit,
    onSend: () -> Unit,
    onVoice: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "‹",
                fontSize = 35.sp,
                color = Blue,
                modifier = Modifier.clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            AstraLogo()
            Spacer(Modifier.width(12.dp))
            Text(
                "ASTRA AI",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Navy
            )
        }

        Spacer(Modifier.height(15.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Text(
                        "Start a conversation with ASTRA.",
                        color = Muted,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            items(messages) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.isUser)
                        Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (message.isUser) Blue else Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Text(
                            message.text,
                            color = if (message.isUser)
                                Color.White else Navy,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = input,
                    onValueChange = onInput,
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    decorationBox = { inner ->
                        if (input.isEmpty()) {
                            Text("Message ASTRA...", color = Muted)
                        }
                        inner()
                    }
                )

                Text(
                    "🎙",
                    fontSize = 21.sp,
                    modifier = Modifier
                        .clickable { onVoice() }
                        .padding(8.dp)
                )

                Text(
                    "➤",
                    color = Color.White,
                    modifier = Modifier
                        .background(Blue, CircleShape)
                        .clickable { onSend() }
                        .padding(13.dp)
                )
            }
        }
    }
}

@Composable
fun HistoryScreen(
    messages: List<ChatMessage>,
    onBack: () -> Unit,
    onClear: () -> Unit,
    onChat: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(22.dp)) {
        Text(
            "‹  Back",
            color = Blue,
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(Modifier.height(25.dp))

        Text(
            "Your History",
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )

        Spacer(Modifier.height(20.dp))

        if (messages.isEmpty()) {
            Text("No saved conversations yet.", color = Muted)
        } else {
            LazyColumn(Modifier.weight(1f)) {
                items(messages.filter { it.isUser }) { message ->
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clickable { onChat() }
                    ) {
                        Text(
                            message.text,
                            color = Navy,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            TextButton(onClick = onClear) {
                Text("Clear conversation history", color = Color.Red)
            }
        }
    }
}

@Composable
fun SettingsScreen(
    language: String,
    onBack: () -> Unit,
    onLanguage: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(22.dp)) {
        Text(
            "‹  Back",
            color = Blue,
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(Modifier.height(30.dp))

        Text(
            "Settings",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )

        Spacer(Modifier.height(25.dp))

        SettingItem("Application", "ASTRA AI")
        SettingItem("Tagline", "Think. Create. Explore.")
        SettingItem("Current language", language)

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = onLanguage,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change language")
        }

        Spacer(Modifier.height(25.dp))

        Text(
            "Version 1.0",
            color = Muted
        )
    }
}

@Composable
fun SettingItem(title: String, value: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = Muted, fontSize = 12.sp)
            Text(
                value,
                color = Navy,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun LanguageScreen(onSelect: (String) -> Unit) {
    val languages = listOf(
        "English",
        "Hindi",
        "Punjabi",
        "Spanish",
        "French",
        "German",
        "Arabic",
        "Chinese",
        "Japanese"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AstraLogo()

        Spacer(Modifier.height(25.dp))

        Text(
            "Welcome to ASTRA",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )

        Spacer(Modifier.height(10.dp))

        Text(
            "Choose your preferred language.",
            color = Muted
        )

        Spacer(Modifier.height(25.dp))

        languages.forEach { language ->
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onSelect(language) }
            ) {
                Text(
                    language,
                    color = Navy,
                    modifier = Modifier.padding(17.dp)
                )
            }
        }
    }
}
