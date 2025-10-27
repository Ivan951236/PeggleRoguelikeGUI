package io.github.ivan951236.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// Light Purple theme color scheme
private val LightPurpleColorScheme = lightColorScheme(
    primary = LightPurplePrimary,
    secondary = LightPurpleSecondary,
    tertiary = LightPurplePrimaryVariant,
    surface = LightPurpleSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Black Purple (AMOLED) theme color scheme
private val BlackPurpleAmoledColorScheme = darkColorScheme(
    primary = AmoledPrimary,
    secondary = AmoledSecondary,
    tertiary = AmoledPrimaryVariant,
    surface = AmoledSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = AmoledOnSurface
)

enum class AppTheme {
    DEFAULT,
    LIGHT_PURPLE,
    BLACK_PURPLE_AMOLED
}

@Composable
fun PeggleRoguelikePresetGeneratorTheme(
    theme: AppTheme = AppTheme.DEFAULT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT_PURPLE -> LightPurpleColorScheme
        AppTheme.BLACK_PURPLE_AMOLED -> BlackPurpleAmoledColorScheme
        AppTheme.DEFAULT -> {
            when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    val context = LocalContext.current
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
                }
                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
