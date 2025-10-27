package io.github.ivan951236

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.ivan951236.ui.DuelScreen
import io.github.ivan951236.ui.RankCalculationScreen
import io.github.ivan951236.ui.settings.SettingsScreen
import io.github.ivan951236.ui.themes.DarkThemesScreen
import io.github.ivan951236.ui.themes.LightThemesScreen
import io.github.ivan951236.utils.ThemePreferences
import io.github.ivan951236.utils.ThemeState
import io.github.ivan951236.ui.theme.PeggleRoguelikePresetGeneratorTheme

// 1) Top‐level name lists
private val inventoryItemNames = listOf(
    "Bjorn",
    "Jimmy Lighting",
    "Kat Tut",
    "Spork",
    "Claude",
    "Reinfield",
    "Tula",
    "Warren",
    "Lord Cinderbottom",
    "Master Hu"
)

private val regularLevelNames = listOf(
    "Peggleland", "Slip and Slide", "Bjorn's Gazebo", "Das Bucket", "Snow Day",
    "Birdy's Crib", "Buffalo Wings", "Skate Park", "Spiral of Doom", "Mr. Peepers",
    "Scarab Crunch", "Infinite Cheese", "Ra Deal", "Croco-Gator Pit", "The Fever Level",
    "The Amoeban", "The Last Flower", "We Come In Peace", "Maid In Space", "Getting The Spare",
    "Pearl Clam", "Insane Aquarium", "Tasty Waves", "Our Favorite Eel", "Love Story",
    "Waves", "Spiderweb", "Blockers", "Baseball", "Vermin", "Holland Oats",
    "I Heart Flowers", "Workin From Home", "Tula's Ride", "70 and Sunny", "Win a Monkey",
    "Dog Pinball", "Spin Again", "Roll 'em", "Five of a Kind"
)

private val miniBossLevelNames = listOf(
    "The Love Moat", "Doom with a View", "Rhombi", "9 Luft Ballons", "Twister Sisters",
    "Spin Cycle", "The Dude Abides", "When Pigs Fly", "Yang, Yin", "Zen Frog"
)

private val bossLevelNames = listOf(
    "Paw Reader",
    "End of Time",
    "Billions & Billions",
    "Don't Panic",
    "Beyond Reason"
)

// 2) Navigation routes
sealed class Screen(val route: String, val title: String) {
    object PeggleLevels : Screen("peggle_levels", "Peggle Levels")
    object BossLevel    : Screen("boss_level",   "Boss Level")
    object Inventory    : Screen("inventory",     "Inventory")
    object Settings     : Screen("settings",      "Settings")
    object Duel         : Screen("duel",          "Duel")
    object RankCalculation : Screen("rank_calculation", "Rank Calculator")
    object DarkThemes   : Screen("dark_themes",   "Dark Themes")
    object LightThemes  : Screen("light_themes",  "Light Themes")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeState = ThemeState.rememberThemeState()
            
            PeggleRoguelikePresetGeneratorTheme(
                theme = themeState.selectedTheme,
                dynamicColor = themeState.dynamicColor
            ) {
                AppNavigator(themeState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigator(themeState: ThemeState) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val themePreferences = remember { ThemePreferences.getInstance(context) }
    
    // For menu state
    var showMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val currentRoute = navController
                        .currentBackStackEntryAsState()
                        .value
                        ?.destination
                        ?.route
                    Text(
                        text = when (currentRoute) {
                            Screen.PeggleLevels.route -> Screen.PeggleLevels.title
                            Screen.BossLevel.route    -> Screen.BossLevel.title
                            Screen.Inventory.route    -> Screen.Inventory.title
                            Screen.Settings.route     -> Screen.Settings.title
                            Screen.Duel.route         -> Screen.Duel.title
                            Screen.RankCalculation.route -> Screen.RankCalculation.title
                            else                      -> "Peggle App"
                        }
                    )
                },
                actions = {
                    val currentRoute = navController
                        .currentBackStackEntryAsState()
                        .value
                        ?.destination
                        ?.route
                    
                    // Only show menu on main screens, not on settings, duel, or rank screens
                    if (currentRoute != Screen.Settings.route && 
                        currentRoute != Screen.Duel.route && 
                        currentRoute != Screen.RankCalculation.route) {
                        
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Menu"
                            )
                        }
                        
                        // Menu dropdown
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Screen.Settings.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Duel Mode") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Screen.Duel.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Rank Calculator") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Screen.RankCalculation.route)
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = { 
            val currentRoute = navController
                .currentBackStackEntryAsState()
                .value
                ?.destination
                ?.route
            
            // Only show bottom navigation on main screens, not on settings, duel, or rank screens
            if (currentRoute != Screen.Settings.route && 
                currentRoute != Screen.Duel.route && 
                currentRoute != Screen.RankCalculation.route) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.PeggleLevels.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Screen.PeggleLevels.route) { PeggleLevelsScreen() }
            composable(Screen.BossLevel.route)    { BossLevelScreen()    }
            composable(Screen.Inventory.route)    { InventoryScreen()    }
            composable(Screen.Settings.route)     { 
                SettingsScreen(
                    onBackClick = { navController.popBackStack() },
                    themeState = themeState,
                    onNavigateToDarkThemes = { navController.navigate(Screen.DarkThemes.route) },
                    onNavigateToLightThemes = { navController.navigate(Screen.LightThemes.route) }
                ) 
            }
            composable(Screen.Duel.route) {
                DuelScreen(
                    onBackClick = { navController.popBackStack() },
                    onStartGame = { navController.navigate(Screen.PeggleLevels.route) }
                )
            }
            composable(Screen.RankCalculation.route) {
                RankCalculationScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.DarkThemes.route) {
                DarkThemesScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.LightThemes.route) {
                LightThemesScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.PeggleLevels,
        Screen.BossLevel,
        Screen.Inventory
    )
    NavigationBar {
        val currentRoute = navController
            .currentBackStackEntryAsState()
            .value
            ?.destination
            ?.route

        items.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { /* Empty icon for now, can be added later */ }
            )
        }
    }
}

// 3) Generators that pick names (internally using indices)
fun generatePeggleLevels(): List<Pair<String, String>> =
    List(31) { index ->
        val level = when (index + 1) {
            16, 20, 26 -> miniBossLevelNames.random() // Mini-boss levels
            else -> regularLevelNames.random() // Regular levels
        }
        val modifier = inventoryItemNames.random()
        level to modifier
    }

// Dual player version
fun generatePeggleLevelsForDual(): Pair<List<Pair<String, String>>, List<Pair<String, String>>> {
    val player1Levels = List(31) { index ->
        val level = when (index + 1) {
            16, 20, 26 -> miniBossLevelNames.random() // Mini-boss levels
            else -> regularLevelNames.random() // Regular levels
        }
        val modifier = inventoryItemNames.random()
        level to modifier
    }
    val player2Levels = List(31) { index ->
        val level = when (index + 1) {
            16, 20, 26 -> miniBossLevelNames.random() // Mini-boss levels
            else -> regularLevelNames.random() // Regular levels
        }
        val modifier = inventoryItemNames.random()
        level to modifier
    }
    return Pair(player1Levels, player2Levels)
}

fun generateBossLevel(): Pair<String, String> {
    val boss     = bossLevelNames.random()
    val modifier = inventoryItemNames.random()
    return boss to modifier
}

// Dual player version
fun generateBossLevelForDual(): Pair<Pair<String, String>, Pair<String, String>> {
    val player1Boss = generateBossLevel()
    val player2Boss = generateBossLevel()
    return Pair(player1Boss, player2Boss)
}

fun generateInventory(): List<String> =
    List(2) {
        inventoryItemNames.random()
    }

// Dual player version
fun generateInventoryForDual(): Pair<List<String>, List<String>> {
    val player1Inventory = List(2) {
        inventoryItemNames.random()
    }
    val player2Inventory = List(2) {
        inventoryItemNames.random()
    }
    return Pair(player1Inventory, player2Inventory)
}

// 4) Composables that only render names
@Composable
fun PeggleLevelsScreen() {
    if (GameState.currentMode == GameMode.DUEL && GameState.player1 != null && GameState.player2 != null) {
        // Duel mode: show levels for both players
        val (player1Levels, player2Levels) = remember { generatePeggleLevelsForDual() }
        
        Column(
            modifier           = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Duel Mode", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            
            // Player 1 Levels
            Text("${GameState.player1?.name ?: "Player 1"} Levels", 
                 style = MaterialTheme.typography.headlineSmall,
                 color = MaterialTheme.colorScheme.primary)
            player1Levels.forEach { (level, mod) ->
                Text("$level — Modifier: $mod")
            }
            Spacer(Modifier.height(16.dp))
            
            // Player 2 Levels
            Text("${GameState.player2?.name ?: "Player 2"} Levels", 
                 style = MaterialTheme.typography.headlineSmall,
                 color = MaterialTheme.colorScheme.primary)
            player2Levels.forEach { (level, mod) ->
                Text("$level — Modifier: $mod")
            }
        }
    } else {
        // Single player mode
        val data = remember { generatePeggleLevels() }
        Column(
            modifier           = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Peggle Levels", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            data.forEach { (level, mod) ->
                Text("$level — Modifier: $mod")
            }
        }
    }
}

@Composable
fun BossLevelScreen() {
    if (GameState.currentMode == GameMode.DUEL && GameState.player1 != null && GameState.player2 != null) {
        // Duel mode: show boss levels for both players
        val (player1Boss, player2Boss) = remember { generateBossLevelForDual() }
        
        Column(
            modifier           = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Duel Mode", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            
            // Player 1 Boss
            Text("${GameState.player1?.name ?: "Player 1"} Boss", 
                 style = MaterialTheme.typography.headlineSmall,
                 color = MaterialTheme.colorScheme.primary)
            Text("Boss: ${player1Boss.first}")
            Text("Modifier: ${player1Boss.second}")
            Spacer(Modifier.height(16.dp))
            
            // Player 2 Boss
            Text("${GameState.player2?.name ?: "Player 2"} Boss", 
                 style = MaterialTheme.typography.headlineSmall,
                 color = MaterialTheme.colorScheme.primary)
            Text("Boss: ${player2Boss.first}")
            Text("Modifier: ${player2Boss.second}")
        }
    } else {
        // Single player mode
        val (boss, mod) = remember { generateBossLevel() }
        Column(
            modifier           = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Boss Level", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text("Boss: $boss")
            Text("Modifier: $mod")
        }
    }
}

@Composable
fun InventoryScreen() {
    if (GameState.currentMode == GameMode.DUEL && GameState.player1 != null && GameState.player2 != null) {
        // Duel mode: show inventories for both players
        val (player1Inventory, player2Inventory) = remember { generateInventoryForDual() }
        
        Column(
            modifier           = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Duel Mode", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            
            // Player 1 Inventory
            Text("${GameState.player1?.name ?: "Player 1"} Inventory", 
                 style = MaterialTheme.typography.headlineSmall,
                 color = MaterialTheme.colorScheme.primary)
            player1Inventory.forEach { name ->
                Text(name)
            }
            Spacer(Modifier.height(16.dp))
            
            // Player 2 Inventory
            Text("${GameState.player2?.name ?: "Player 2"} Inventory", 
                 style = MaterialTheme.typography.headlineSmall,
                 color = MaterialTheme.colorScheme.primary)
            player2Inventory.forEach { name ->
                Text(name)
            }
        }
    } else {
        // Single player mode
        val items = remember { generateInventory() }
        Column(
            modifier           = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Inventory", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            items.forEach { name ->
                Text(name)
            }
        }
    }
}