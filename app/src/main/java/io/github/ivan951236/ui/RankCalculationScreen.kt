package io.github.ivan951236.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankCalculationScreen(
    onBackClick: () -> Unit
) {
    var roguelikeWins by remember { mutableStateOf("") }
    var levelWins by remember { mutableStateOf("") }
    var clutches by remember { mutableStateOf("") }
    var totalMisses by remember { mutableStateOf("") }
    var lostRounds by remember { mutableStateOf("") }
    var lostOnBossLevel by remember { mutableStateOf("") }
    var feverModePoints by remember { mutableStateOf("") }
    
    var calculatedRank by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Rank Calculator",
            style = MaterialTheme.typography.headlineMedium
        )
        
        // Input fields for rank calculation
        OutlinedTextField(
            value = roguelikeWins,
            onValueChange = { roguelikeWins = it },
            label = { Text("Roguelike Wins") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        
        OutlinedTextField(
            value = levelWins,
            onValueChange = { levelWins = it },
            label = { Text("Level Wins") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        
        OutlinedTextField(
            value = clutches,
            onValueChange = { clutches = it },
            label = { Text("Clutches") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        
        OutlinedTextField(
            value = totalMisses,
            onValueChange = { totalMisses = it },
            label = { Text("Total Misses") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        
        OutlinedTextField(
            value = lostRounds,
            onValueChange = { lostRounds = it },
            label = { Text("Lost Rounds") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        
        OutlinedTextField(
            value = lostOnBossLevel,
            onValueChange = { lostOnBossLevel = it },
            label = { Text("Lost on Boss Level") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        
        OutlinedTextField(
            value = feverModePoints,
            onValueChange = { feverModePoints = it },
            label = { Text("Every 100000 Points given in Fever Mode") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        
        Button(
            onClick = {
                try {
                    val roguelikeWinsVal = roguelikeWins.toIntOrNull() ?: 0
                    val levelWinsVal = levelWins.toIntOrNull() ?: 0
                    val clutchesVal = clutches.toIntOrNull() ?: 0
                    val totalMissesVal = totalMisses.toIntOrNull() ?: 1  // Avoid division by zero
                    val lostRoundsVal = lostRounds.toIntOrNull() ?: 0
                    val lostOnBossLevelVal = lostOnBossLevel.toIntOrNull() ?: 0
                    val feverModePointsVal = feverModePoints.toIntOrNull() ?: 0
                    
                    // Calculate the rank using the formula:
                    // Roguelike Wins x Level Wins x Clutches / Total Misses / Lost Rounds + 3 / Lost on Boss Level + 2 (for every single Boss Level lost) * Every 100000 Points given in Fever Mode + 1 = Overall = Rank
                    var calculation = (roguelikeWinsVal * levelWinsVal * clutchesVal).toDouble()
                    calculation = calculation / totalMissesVal
                    
                    if (lostRoundsVal > 0) {
                        calculation = calculation / lostRoundsVal
                        calculation += 3.0 // Add 3 after dividing with Lost Rounds
                    }
                    
                    if (lostOnBossLevelVal > 0) {
                        calculation += (lostOnBossLevelVal * 2.0) // Add 2 for every single Boss Level lost
                    }
                    
                    val feverPointsFactor = (feverModePointsVal / 100000)
                    calculation = calculation * (feverPointsFactor + 1) // Add 1 after multiplication with Every 100000 Points given in Fever Mode
                    
                    val overall = calculation.toInt()
                    calculatedRank = "Overall Score: $overall\nRank: ${getRankByScore(overall)}"
                } catch (e: Exception) {
                    calculatedRank = "Error calculating rank: ${e.message}"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate Rank")
        }
        
        if (calculatedRank.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = calculatedRank,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

// Define the rank thresholds from the rank_info.txt file
private fun getRankByScore(score: Int): String {
    val ranks = listOf(
        Rank("Newbie", listOf(0, 23, 46, 55)),
        Rank("Private", listOf(65, 76, 89, 109)),
        Rank("Admiral", listOf(115, 127, 131, 151)),
        Rank("Newbie VIP", listOf(165, 173, 181, 193)),
        Rank("VIP", listOf(195, 221, 251, 278)),
        Rank("Senior VIP", listOf(300, 345, 402, 412)),
        Rank("Demon Newbie", listOf(450, 510, 550, 610)),
        Rank("Demon Private", listOf(650, 699, 757, 789)),
        Rank("Demon Admiral", listOf(950, 1001, 1110, 1143)),
        Rank("Demon Newbie VIP", listOf(1150, 1200, 1225, 1300)),
        Rank("Demon VIP", listOf(1350, 1399, 1450, 1549)),
        Rank("Demon Senior VIP", listOf(1550, 1600, 1700, 1888)),
        Rank("Godly Newbie", listOf(2000, 2010, 2020, 2077)),
        Rank("Godly Private", listOf(2255, 2277, 2311, 2560)),
        Rank("Godly Admiral", listOf(2750, 2999, 3009, 3110)),
        Rank("Godly Newbie VIP", listOf(3120, 3200, 3260, 3310)),
        Rank("Godly VIP", listOf(3330, 3350, 3500, 3610)),
        Rank("Godly Senior VIP", listOf(3650, 3750, 3850, 3950)),
        Rank("Developer", listOf(4000, 4100, 4200, 4400)),
        Rank("Kratos of Gaming", listOf(4500, 5000, 7000, 9000)),
        Rank("METAPCs", listOf(10000, 20000, 30000, 50000))
    )
    
    // Check which range the score falls into
    for (rank in ranks) {
        if (rank.thresholds.any { score <= it }) {
            return rank.name
        }
    }
    
    return "Unranked"
}

data class Rank(
    val name: String,
    val thresholds: List<Int>
)