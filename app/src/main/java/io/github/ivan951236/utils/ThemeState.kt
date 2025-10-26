package io.github.ivan951236.utils

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.DisposableEffect

class ThemeState {
    private val _dynamicColor = mutableStateOf(false)
    val dynamicColor: Boolean by _dynamicColor

    fun updateTheme(themePreferences: ThemePreferences) {
        val newDynamicColor = themePreferences.shouldUseDynamicColors()
        if (_dynamicColor.value != newDynamicColor) {
            _dynamicColor.value = newDynamicColor
        }
    }

    companion object {
        @Composable
        fun rememberThemeState(): ThemeState {
            val context = LocalContext.current
            val themePreferences = remember { ThemePreferences.getInstance(context) }
            val themeState = remember { ThemeState() }

            // Initialize theme state
            DisposableEffect(themePreferences) {
                themeState.updateTheme(themePreferences)
                
                // Watch for changes to theme preferences
                val listener = object : android.content.SharedPreferences.OnSharedPreferenceChangeListener {
                    override fun onSharedPreferenceChanged(sharedPreferences: android.content.SharedPreferences?, key: String?) {
                        if (key == "theme_mode") {
                            themeState.updateTheme(themePreferences)
                        }
                    }
                }
                
                themePreferences.prefs.registerOnSharedPreferenceChangeListener(listener)
                
                onDispose {
                    themePreferences.prefs.unregisterOnSharedPreferenceChangeListener(listener)
                }
            }

            return themeState
        }
    }
}