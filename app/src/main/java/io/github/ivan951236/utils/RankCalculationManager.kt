package io.github.ivan951236.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.system.Os
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

// This is a simplified TOML writer since Android doesn't have built-in TOML support
// For a production app, you'd typically add a TOML library dependency
class RankCalculationManager(private val context: Context) {
    
    data class RankData(
        val roguelikeWins: Int = 0,
        val levelWins: Int = 0,
        val clutches: Int = 0,
        val totalMisses: Int = 0,
        val lostRounds: Int = 0,
        val lostOnBossLevel: Int = 0,
        val feverModePoints: Int = 0,
        val sessionId: String = ""
    )
    
    private fun writeTOMLToFile(file: File, rankData: RankData) {
        FileWriter(file).use { writer ->
            writer.write("[stats]\n")
            writer.write("roguelike_wins = ${rankData.roguelikeWins}\n")
            writer.write("level_wins = ${rankData.levelWins}\n")
            writer.write("clutches = ${rankData.clutches}\n")
            writer.write("total_misses = ${rankData.totalMisses}\n")
            writer.write("lost_rounds = ${rankData.lostRounds}\n")
            writer.write("lost_on_boss_level = ${rankData.lostOnBossLevel}\n")
            writer.write("fever_mode_points = ${rankData.feverModePoints}\n")
            writer.write("session_id = \"${rankData.sessionId}\"\n")
        }
    }
    
    fun saveRankCalculation(rankData: RankData) {
        try {
            // Create the rankCalcs directory
            val rankCalcsDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // For Android 11+ with scoped storage
                val externalDir = Environment.getExternalStorageDirectory()
                val rankCalcsDir = File(externalDir, "rankCalcs")
                
                // Check if we have All Files access permission
                if (hasAllFilesAccess()) {
                    // Store in internal memory
                    val internalDir = Environment.getExternalStorageDirectory()
                    val internalRankCalcsDir = File(internalDir, "rankCalcs")
                    if (!internalRankCalcsDir.exists()) {
                        internalRankCalcsDir.mkdirs()
                    }
                    internalRankCalcsDir
                } else {
                    // Store in other storage (Downloads folder area)
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val otherStorageRankCalcsDir = File(downloadsDir.parentFile, "rankCalcs")
                    if (!otherStorageRankCalcsDir.exists()) {
                        otherStorageRankCalcsDir.mkdirs()
                    }
                    otherStorageRankCalcsDir
                }
            } else {
                // For older Android versions, store in Downloads folder area
                val downloadsDir = Environment.getExternalStorageDirectory()
                val rankCalcsDir = File(downloadsDir, "rankCalcs")
                if (!rankCalcsDir.exists()) {
                    rankCalcsDir.mkdirs()
                }
                rankCalcsDir
            }
            
            // Create the TOML file
            val fileName = "rank_calc_${System.currentTimeMillis()}.toml"
            val tomlFile = File(rankCalcsDir, fileName)
            
            writeTOMLToFile(tomlFile, rankData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun hasAllFilesAccess(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            true // For older versions, we have access by default with permission
        }
    }
    
    fun loadRankCalculation(): RankData {
        // For simplicity, return default data
        // In a real implementation, you would find and parse TOML files
        return RankData(sessionId = SessionIdUtils.generateSessionId())
    }
}