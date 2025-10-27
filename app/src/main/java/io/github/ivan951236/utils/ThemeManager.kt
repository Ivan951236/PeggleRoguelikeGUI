package io.github.ivan951236.utils

import android.content.Context
import android.os.Environment
import android.os.Build
import java.io.File

data class Theme(
    val name: String,
    val isDark: Boolean,
    val primaryColor: String? = null,
    val secondaryColor: String? = null,
    val backgroundColor: String? = null
)

class ThemeManager(private val context: Context) {
    
    fun loadCustomThemes(): List<Theme> {
        val themes = mutableListOf<Theme>()
        
        try {
            // Look for prpgMobileThemes directory
            val themesDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // For Android 11+, use scoped storage or request All Files access
                if (PermissionsUtils.hasAllFilesAccessPermission(context)) {
                    // Use internal memory
                    val internalDir = Environment.getExternalStorageDirectory()
                    File(internalDir, "prpgMobileThemes")
                } else {
                    // Use Downloads folder area
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    File(downloadsDir.parentFile, "prpgMobileThemes")
                }
            } else {
                // For older Android versions
                val downloadsDir = Environment.getExternalStorageDirectory()
                File(downloadsDir, "prpgMobileThemes")
            }
            
            if (themesDir.exists() && themesDir.isDirectory) {
                val themeFiles = themesDir.listFiles { _, name -> 
                    name.endsWith(".toml") || name.endsWith(".theme") 
                }
                
                themeFiles?.forEach { file ->
                    // For simplicity, we'll create mock themes
                    // In a real implementation, we would parse the TOML files to extract theme info
                    val isDark = file.name.contains("dark", ignoreCase = true) || 
                                file.name.contains("black", ignoreCase = true) ||
                                file.name.contains("night", ignoreCase = true)
                    
                    themes.add(Theme(
                        name = file.nameWithoutExtension,
                        isDark = isDark
                    ))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return themes
    }
    
    fun getDarkThemes(): List<Theme> {
        return loadCustomThemes().filter { it.isDark }
    }
    
    fun getLightThemes(): List<Theme> {
        return loadCustomThemes().filter { !it.isDark }
    }
}