package io.github.ivan951236

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*       // Material 3
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import io.github.ivan951236.ui.theme.PeggleRoguelikePresetGeneratorTheme
import ua.ivan95.pegglepegs.ui.theme.PeggleRoguelikePresetGeneratorTheme

// 1) Top‐level name lists
private val inventoryItemNames = listOf(
    "Bjorn",
    "Jimmy Lighting",
    "Kat Tut",
    "Sprok",
    "Claude",
    "Reinfield",
    "Tula",
    "Waren",
    "Lord Cinderbottom",
    "Master Hu"
)

private val peggleLevelNames = listOf(
    "Peggleland", "Slip and Slide", "Bjorn's Gazebo", "Das Bucket", "Snow Day",
    "Birdy's Crib", "Buffalo Wings", "Skate Park", "Spiral of Doom", "Mr. Peepers",
    "Scarab Crunch", "Infinite Cheese", "Ra Deal", "Croco-Gator Pit", "The Fever Level",
    "The Amoeban", "The Last Flower", "We Come In Peace", "Maid In Space", "Getting The Spare",
    "Pearl Clam", "Insane Aquarium", "Tasty Waves", "Our Favorite Eel", "Love Story",
    "Waves", "Spiderweb", "Blockers", "Baseball", "Vermin", "Holland Oats",
    "I Heart Flowers", "Workin From Home", "Tula's Ride", "70 and Sunny", "Win a Monkey",
    "Dog Pinball", "Spin Again", "Roll 'em", "Five of a Kind", "The Love Moat",
    "Doom with a View", "Rhombi", "9 Luft Ballons", "Twister Sisters", "Spin Cycle",
    "The Dude Abides", "When Pigs Fly", "Yang, Yin", "Zen Frog"
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
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PeggleRoguelikePresetGeneratorTheme {
                AppNavigator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            topAppBar {
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
                        else                      -> "Peggle App"
                    }
                )
            }
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.PeggleLevels.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Screen.PeggleLevels.route) { PeggleLevelsScreen() }
            composable(Screen.BossLevel.route)    { BossLevelScreen()    }
            composable(Screen.Inventory.route)    { InventoryScreen()    }
        }
    }
}

fun topAppBar(scrollBehavior: @Composable () -> Unit) {}

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
                icon     = { /* optional icon */ },
                label    = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick  = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }
            )
        }
    }
}

// 3) Generators that pick names (internally using indices)
fun generatePeggleLevels(): List<Pair<String, String>> =
    List(15) {
        val level    = peggleLevelNames.random()
        val modifier = inventoryItemNames.random()
        level to modifier
    }

fun generateBossLevel(): Pair<String, String> {
    val boss     = bossLevelNames.random()
    val modifier = inventoryItemNames.random()
    return boss to modifier
}

fun generateInventory(): List<String> =
    List(3) {
        inventoryItemNames.random()
    }

// 4) Composables that only render names
@Composable
fun PeggleLevelsScreen() {
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

@Composable
fun BossLevelScreen() {
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

@Composable
fun InventoryScreen() {
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