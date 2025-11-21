package com.childsafety.parent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.childsafety.parent.data.ChildDevice
import com.childsafety.parent.utils.PreferenceManager
import com.childsafety.parent.websocket.ParentWebSocketManager

class MainActivity : ComponentActivity() {
    
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var webSocketManager: ParentWebSocketManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        preferenceManager = PreferenceManager(this)
        webSocketManager = ParentWebSocketManager(this)
        
        setContent {
            ParentControlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
        
        // Connect to WebSocket
        webSocketManager.connect()
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen() {
        var selectedTab by remember { mutableStateOf(0) }
        var childDevices by remember { mutableStateOf(listOf<ChildDevice>()) }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Parent Control") },
                    actions = {
                        IconButton(onClick = { /* Add child */ }) {
                            Icon(Icons.Default.Add, "Add Child")
                        }
                        IconButton(onClick = { /* Settings */ }) {
                            Icon(Icons.Default.Settings, "Settings")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, "Children") },
                        label = { Text("Children") },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.LocationOn, "Location") },
                        label = { Text("Location") },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Warning, "Alerts") },
                        label = { Text("Alerts") },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTab) {
                    0 -> ChildrenListScreen(childDevices)
                    1 -> LocationScreen()
                    2 -> AlertsScreen()
                }
            }
        }
    }
    
    @Composable
    fun ChildrenListScreen(childDevices: List<ChildDevice>) {
        if (childDevices.isEmpty()) {
            EmptyStateScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(childDevices) { child ->
                    ChildDeviceCard(child)
                }
            }
        }
    }
    
    @Composable
    fun EmptyStateScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "No Children Added",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Add your first child's device to start monitoring",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { /* Navigate to add child */ },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Child Device")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📱 How to Add a Child:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("1. Install Child Safety Monitor app on child's device")
                    Text("2. Complete setup on child's device")
                    Text("3. Enter the 6-digit linking code shown above")
                    Text("4. Start monitoring!")
                }
            }
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChildDeviceCard(child: ChildDevice) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* Navigate to details */ }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = child.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (child.isOnline) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (child.isOnline) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (child.isOnline) "Online" else "Offline",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row {
                        Chip(text = "📍 ${child.location}")
                        Spacer(modifier = Modifier.width(8.dp))
                        Chip(text = "⏱️ ${child.screenTime}")
                    }
                }
                
                IconButton(onClick = { /* More options */ }) {
                    Icon(Icons.Default.MoreVert, "More")
                }
            }
        }
    }
    
    @Composable
    fun Chip(text: String) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
    
    @Composable
    fun LocationScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Location Tracking Map")
        }
    }
    
    @Composable
    fun AlertsScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Safety Alerts")
        }
    }
}

data class ChildDevice(
    val id: String,
    val name: String,
    val isOnline: Boolean,
    val location: String,
    val screenTime: String,
    val lastUpdate: Long
)

@Composable
fun ParentControlTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFF1976D2),
            secondary = androidx.compose.ui.graphics.Color(0xFF4CAF50)
        ),
        content = content
    )
}
