package io.github.ivan951236

data class Player(
    val name: String,
    val inventory: List<String> = emptyList()
)

// Enum to represent game modes
enum class GameMode {
    SINGLE_PLAYER,
    DUEL
}

// Global state to track the current game mode and players
object GameState {
    var currentMode: GameMode = GameMode.SINGLE_PLAYER
    var player1: Player? = null
    var player2: Player? = null
    
    fun resetToSinglePlayer() {
        currentMode = GameMode.SINGLE_PLAYER
        player1 = null
        player2 = null
    }
}