package io.github.ivan951236.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.ivan951236.GameState
import io.github.ivan951236.utils.ThemePreferences
import io.github.ivan951236.utils.PermissionsUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    themeState: io.github.ivan951236.utils.ThemeState,
    onNavigateToDarkThemes: () -> Unit = {},
    onNavigateToLightThemes: () -> Unit = {}
) {
    val context = LocalContext.current
    val themePreferences = remember { ThemePreferences.getInstance(context) }
    
    var selectedThemeMode by remember { 
        mutableStateOf(themePreferences.getThemeMode()) 
    }
    var selectedAppTheme by remember { 
        mutableStateOf(themePreferences.getSelectedAppTheme()) 
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back button
        TextButton(onClick = onBackClick) {
            Text("Back")
        }
        
        Text(
            text = "Theme Settings",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Select Theme Mode",
            style = MaterialTheme.typography.titleLarge
        )
        
        // Theme selection options
        ThemeRadioButton(
            text = "Auto (Material You on Android 12+, Purple on older)",
            selected = selectedThemeMode == ThemePreferences.ThemeMode.AUTO,
            onClick = {
                selectedThemeMode = ThemePreferences.ThemeMode.AUTO
                themePreferences.setThemeMode(ThemePreferences.ThemeMode.AUTO)
                themeState.updateTheme(themePreferences)
            }
        )
        
        ThemeRadioButton(
            text = "Material You (Dynamic Colors)",
            selected = selectedThemeMode == ThemePreferences.ThemeMode.MATERIAL_YOU,
            onClick = {
                selectedThemeMode = ThemePreferences.ThemeMode.MATERIAL_YOU
                themePreferences.setThemeMode(ThemePreferences.ThemeMode.MATERIAL_YOU)
                themeState.updateTheme(themePreferences)
            }
        )
        
        ThemeRadioButton(
            text = "Purple Theme (Default)",
            selected = selectedThemeMode == ThemePreferences.ThemeMode.PURPLE,
            onClick = {
                selectedThemeMode = ThemePreferences.ThemeMode.PURPLE
                themePreferences.setThemeMode(ThemePreferences.ThemeMode.PURPLE)
                themeState.updateTheme(themePreferences)
            }
        )
        
        // App theme selection options
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "App Themes",
            style = MaterialTheme.typography.titleLarge
        )
        
        ThemeRadioButton(
            text = "Default Theme",
            selected = selectedAppTheme == io.github.ivan951236.ui.theme.AppTheme.DEFAULT,
            onClick = {
                selectedAppTheme = io.github.ivan951236.ui.theme.AppTheme.DEFAULT
                themePreferences.setSelectedAppTheme(io.github.ivan951236.ui.theme.AppTheme.DEFAULT)
                themeState.updateTheme(themePreferences)
            }
        )
        
        ThemeRadioButton(
            text = "Light Purple",
            selected = selectedAppTheme == io.github.ivan951236.ui.theme.AppTheme.LIGHT_PURPLE,
            onClick = {
                selectedAppTheme = io.github.ivan951236.ui.theme.AppTheme.LIGHT_PURPLE
                themePreferences.setSelectedAppTheme(io.github.ivan951236.ui.theme.AppTheme.LIGHT_PURPLE)
                themeState.updateTheme(themePreferences)
            }
        )
        
        ThemeRadioButton(
            text = "Black Purple (AMOLED)",
            selected = selectedAppTheme == io.github.ivan951236.ui.theme.AppTheme.BLACK_PURPLE_AMOLED,
            onClick = {
                selectedAppTheme = io.github.ivan951236.ui.theme.AppTheme.BLACK_PURPLE_AMOLED
                themePreferences.setSelectedAppTheme(io.github.ivan951236.ui.theme.AppTheme.BLACK_PURPLE_AMOLED)
                themeState.updateTheme(themePreferences)
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Custom themes selector
        Text(
            text = "Custom Themes",
            style = MaterialTheme.typography.titleLarge
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onNavigateToDarkThemes,
                modifier = Modifier.weight(1f)
            ) {
                Text("Dark Themes")
            }
            
            Button(
                onClick = onNavigateToLightThemes,
                modifier = Modifier.weight(1f)
            ) {
                Text("Light Themes")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // All Files access permission button
        Button(
            onClick = { 
                PermissionsUtils.requestAllFilesAccessPermission(context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            val hasPermission = PermissionsUtils.hasAllFilesAccessPermission(context)
            Text(if (hasPermission) "All Files Access Granted" else "Request All Files Access")
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Show option to return to single player if in duel mode
        if (io.github.ivan951236.GameState.currentMode == io.github.ivan951236.GameMode.DUEL) {
            Button(
                onClick = { 
                    GameState.resetToSinglePlayer()
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Return to Single Player Mode")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        Text(
            text = "Current Android Version: ${android.os.Build.VERSION.SDK_INT}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ThemeRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}