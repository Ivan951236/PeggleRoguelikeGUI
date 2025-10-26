package io.github.ivan951236.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build

class ThemePreferences private constructor(context: Context) {
    
    val prefs: SharedPreferences = context.applicationContext.getSharedPreferences(
        "theme_preferences", Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
        private const val DEFAULT_THEME_MODE = 0 // 0 = Auto (Material You on Android 12+, Purple on older), 1 = Material You, 2 = Purple
        
        @Volatile
        private var INSTANCE: ThemePreferences? = null
        
        fun getInstance(context: Context): ThemePreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ThemePreferences(context).also { INSTANCE = it }
            }
        }
    }
    
    enum class ThemeMode {
        AUTO,
        MATERIAL_YOU,
        PURPLE
    }
    
    fun getThemeMode(): ThemeMode {
        val value = prefs.getInt(KEY_THEME_MODE, DEFAULT_THEME_MODE)
        return when (value) {
            1 -> ThemeMode.MATERIAL_YOU
            2 -> ThemeMode.PURPLE
            else -> ThemeMode.AUTO
        }
    }
    
    fun setThemeMode(themeMode: ThemeMode) {
        val value = when (themeMode) {
            ThemeMode.MATERIAL_YOU -> 1
            ThemeMode.PURPLE -> 2
            ThemeMode.AUTO -> 0
        }
        prefs.edit().putInt(KEY_THEME_MODE, value).apply()
    }
    
    fun shouldUseDynamicColors(): Boolean {
        return when (getThemeMode()) {
            ThemeMode.MATERIAL_YOU -> true
            ThemeMode.PURPLE -> false
            ThemeMode.AUTO -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        }
    }
}