package com.childsafety.monitor

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.childsafety.monitor.utils.PreferenceManager
import java.util.UUID

class SetupActivity : ComponentActivity() {
    
    private lateinit var preferenceManager: PreferenceManager
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle permissions
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        preferenceManager = PreferenceManager(this)
        
        setContent {
            ChildSafetyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupScreen()
                }
            }
        }
    }
    
    @Composable
    fun SetupScreen() {
        var currentStep by remember { mutableStateOf(0) }
        var serverUrl by remember { mutableStateOf(TextFieldValue("wss://your-server.com")) }
        var parentCode by remember { mutableStateOf(TextFieldValue("")) }
        var childName by remember { mutableStateOf(TextFieldValue("")) }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (currentStep) {
                0 -> WelcomeStep {
                    currentStep = 1
                }
                1 -> ServerSetupStep(
                    serverUrl = serverUrl,
                    onServerUrlChange = { serverUrl = it },
                    onNext = {
                        preferenceManager.setServerUrl(serverUrl.text)
                        currentStep = 2
                    }
                )
                2 -> ParentLinkStep(
                    parentCode = parentCode,
                    onParentCodeChange = { parentCode = it },
                    childName = childName,
                    onChildNameChange = { childName = it },
                    onNext = {
                        preferenceManager.setParentCode(parentCode.text)
                        preferenceManager.setChildName(childName.text)
                        currentStep = 3
                    }
                )
                3 -> PermissionsStep {
                    requestAllPermissions()
                    currentStep = 4
                }
                4 -> CompletionStep {
                    completeSetup()
                }
            }
        }
    }
    
    @Composable
    fun WelcomeStep(onNext: () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🛡️ Child Safety Setup",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "This app will help keep your child safe online and in the real world.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get Started")
            }
        }
    }
    
    @Composable
    fun ServerSetupStep(
        serverUrl: TextFieldValue,
        onServerUrlChange: (TextFieldValue) -> Unit,
        onNext: () -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Server Configuration",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedTextField(
                value = serverUrl,
                onValueChange = onServerUrlChange,
                label = { Text("Server URL") },
                placeholder = { Text("wss://your-server.com") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Enter the monitoring server URL provided by your parent",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                enabled = serverUrl.text.isNotEmpty()
            ) {
                Text("Continue")
            }
        }
    }
    
    @Composable
    fun ParentLinkStep(
        parentCode: TextFieldValue,
        onParentCodeChange: (TextFieldValue) -> Unit,
        childName: TextFieldValue,
        onChildNameChange: (TextFieldValue) -> Unit,
        onNext: () -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Link to Parent",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedTextField(
                value = childName,
                onValueChange = onChildNameChange,
                label = { Text("Child's Name") },
                placeholder = { Text("Enter child's name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = parentCode,
                onValueChange = onParentCodeChange,
                label = { Text("Parent Code") },
                placeholder = { Text("Enter 6-digit code") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Ask your parent for the linking code from their app",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                enabled = parentCode.text.length == 6 && childName.text.isNotEmpty()
            ) {
                Text("Link Device")
            }
        }
    }
    
    @Composable
    fun PermissionsStep(onNext: () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Required Permissions",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("The following permissions are needed:")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("📍 Location - Track device location")
                    Text("📱 Usage Stats - Monitor app usage")
                    Text("📞 Phone - Monitor calls and SMS")
                    Text("🔔 Notifications - Receive alerts")
                    Text("♿ Accessibility - Monitor screen activity")
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Grant Permissions")
            }
        }
    }
    
    @Composable
    fun CompletionStep(onComplete: () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✅ Setup Complete!",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Child safety monitoring is now active. Your parent can monitor this device remotely.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finish")
            }
        }
    }
    
    private fun requestAllPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE
        )
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        
        permissionLauncher.launch(permissions.toTypedArray())
    }
    
    private fun completeSetup() {
        // Generate device ID
        val deviceId = UUID.randomUUID().toString().substring(0, 8)
        preferenceManager.setDeviceId(deviceId)
        preferenceManager.setSetupCompleted(true)
        preferenceManager.setMonitoringEnabled(true)
        
        // Start main activity
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
