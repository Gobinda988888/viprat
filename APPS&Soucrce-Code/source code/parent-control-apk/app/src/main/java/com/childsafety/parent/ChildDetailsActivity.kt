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
import androidx.compose.ui.unit.dp

class ChildDetailsActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val childId = intent.getStringExtra("child_id") ?: ""
        val childName = intent.getStringExtra("child_name") ?: ""
        
        setContent {
            ParentControlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChildDetailsScreen(childName)
                }
            }
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChildDetailsScreen(childName: String) {
        var selectedTab by remember { mutableStateOf(0) }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(childName) },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Overview") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Apps") }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Activity") }
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        text = { Text("Controls") }
                    )
                }
                
                when (selectedTab) {
                    0 -> OverviewTab()
                    1 -> AppsTab()
                    2 -> ActivityTab()
                    3 -> ControlsTab()
                }
            }
        }
    }
    
    @Composable
    fun OverviewTab() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                StatsCard(
                    title = "Screen Time Today",
                    value = "3h 24m",
                    icon = Icons.Default.Phone,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                StatsCard(
                    title = "Current Location",
                    value = "Home",
                    icon = Icons.Default.LocationOn,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            item {
                StatsCard(
                    title = "Most Used App",
                    value = "YouTube - 1h 15m",
                    icon = Icons.Default.Email,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Quick Actions",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        QuickActionButton(
                            text = "📍 View Live Location",
                            onClick = { }
                        )
                        QuickActionButton(
                            text = "📸 Take Screenshot",
                            onClick = { }
                        )
                        QuickActionButton(
                            text = "🔒 Lock Device",
                            onClick = { }
                        )
                        QuickActionButton(
                            text = "📞 View Call History",
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
    
    @Composable
    fun StatsCard(
        title: String,
        value: String,
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        color: androidx.compose.ui.graphics.Color
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = color
                )
            }
        }
    }
    
    @Composable
    fun QuickActionButton(text: String, onClick: () -> Unit) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(text, modifier = Modifier.fillMaxWidth())
        }
    }
    
    @Composable
    fun AppsTab() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getSampleApps()) { app ->
                AppUsageCard(app)
            }
        }
    }
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppUsageCard(app: AppUsageInfo) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = app.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Used: ${app.usageTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = !app.isBlocked,
                        onCheckedChange = { /* Toggle block */ }
                    )
                    IconButton(onClick = { /* More options */ }) {
                        Icon(Icons.Default.MoreVert, "More")
                    }
                }
            }
        }
    }
    
    @Composable
    fun ActivityTab() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getSampleActivities()) { activity ->
                ActivityCard(activity)
            }
        }
    }
    
    @Composable
    fun ActivityCard(activity: ActivityInfo) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = activity.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = activity.description, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = activity.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
    
    @Composable
    fun ControlsTab() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Screen Time Limits",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    TimeLimit("Weekdays", "3 hours")
                    TimeLimit("Weekends", "5 hours")
                    TimeLimit("Bedtime", "10:00 PM - 7:00 AM")
                }
            }
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Content Filters",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ToggleSetting("Block Adult Content", true)
                    ToggleSetting("Safe Search", true)
                    ToggleSetting("Block Social Media", false)
                }
            }
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Location & Safety",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ToggleSetting("Location Tracking", true)
                    ToggleSetting("Geofence Alerts", true)
                    ToggleSetting("SOS Button", true)
                }
            }
        }
    }
    
    @Composable
    fun TimeLimit(label: String, value: String) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    
    @Composable
    fun ToggleSetting(label: String, checked: Boolean) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = checked,
                onCheckedChange = { /* Toggle */ }
            )
        }
    }
    
    private fun getSampleApps() = listOf(
        AppUsageInfo("YouTube", "1h 15m", false),
        AppUsageInfo("Instagram", "45m", false),
        AppUsageInfo("WhatsApp", "32m", false),
        AppUsageInfo("TikTok", "28m", true),
        AppUsageInfo("Chrome", "1h 2m", false)
    )
    
    private fun getSampleActivities() = listOf(
        ActivityInfo(Icons.Default.LocationOn, "Left home", "10 minutes ago"),
        ActivityInfo(Icons.Default.Phone, "Opened Instagram", "15 minutes ago"),
        ActivityInfo(Icons.Default.Email, "Received SMS", "1 hour ago"),
        ActivityInfo(Icons.Default.Phone, "Called Mom", "2 hours ago")
    )
}

data class AppUsageInfo(
    val name: String,
    val usageTime: String,
    val isBlocked: Boolean
)

data class ActivityInfo(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val description: String,
    val timestamp: String
)
