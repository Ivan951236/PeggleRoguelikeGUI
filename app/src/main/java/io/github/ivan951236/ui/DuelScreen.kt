package io.github.ivan951236.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.ivan951236.GameMode
import io.github.ivan951236.GameState
import io.github.ivan951236.Player
import io.github.ivan951236.generateInventory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuelScreen(
    onBackClick: () -> Unit,
    onStartGame: () -> Unit
) {
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Duel Mode",
            style = MaterialTheme.typography.headlineMedium
        )
        
        OutlinedTextField(
            value = player1Name,
            onValueChange = { player1Name = it },
            label = { Text("Player 1 Name") },
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = player2Name,
            onValueChange = { player2Name = it },
            label = { Text("Player 2 Name") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            
            Button(
                onClick = {
                    if (player1Name.isNotBlank() && player2Name.isNotBlank()) {
                        // Generate inventories for both players
                        val player1Inventory = generateInventory()
                        val player2Inventory = generateInventory()
                        
                        GameState.currentMode = GameMode.DUEL
                        GameState.player1 = Player(player1Name.trim(), player1Inventory)
                        GameState.player2 = Player(player2Name.trim(), player2Inventory)
                        
                        onStartGame()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = player1Name.isNotBlank() && player2Name.isNotBlank()
            ) {
                Text("Start Duel")
            }
        }
        
        if (player1Name.isBlank() || player2Name.isBlank()) {
            Text(
                text = "Please enter names for both players",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}