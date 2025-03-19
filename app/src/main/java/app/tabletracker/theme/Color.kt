package app.tabletracker.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


// 🎨 Primary Brand Colors - Modern & Clean
object ColorLightTokens {
    val Primary = Color(0xFFFE7C5D) // Modern Soft Orange-Red Accent
    val OnPrimary = Color.White // White text for clarity
    val PrimaryContainer = Color(0xFFF2F3F7) // Soft Muted Pastel Peach
    val OnPrimaryContainer = Color(0xFF202425) // Deep Warm Red for contrast
    val InversePrimary = Color(0xFFFFB9A6) // Softer Pastel Version of Primary

    val Secondary = Color(0xFFEAEAEA) // Soft Light Gray
    val OnSecondary = Color(0xFF444444) // Dark Gray Text for Contrast
    val SecondaryContainer = Color(0xFFF5F5F5) // Very Light Gray
    val OnSecondaryContainer = Color(0xFF666666) // Medium Gray

    val Tertiary = Color(0xFFD8D8D8) // Neutral Muted Gray
    val OnTertiary = Color(0xFF333333) // Dark Gray for text
    val TertiaryContainer = Color(0xFFF2F2F2) // Light Neutral Gray
    val OnTertiaryContainer = Color(0xFF505050) // Medium Gray for readability

    val Background = Color(0xFFFFFFFF) // Pure White Background
    val OnBackground = Color(0xFF202425) // Modern Black for Contrast

    val Surface = Color(0xFFF1EFEF) // Very Soft White
    val OnSurface = Color(0xFF292D2E) // Dark Modern Gray
    val SurfaceVariant = Color(0xFFF0F0F0) // Muted Soft Gray
    val OnSurfaceVariant = Color(0xFF525252) // Medium Dark Gray

    val SurfaceTint = Primary
    val InverseSurface = Color(0xFF303437) // Muted Dark Gray
    val InverseOnSurface = Color.White

    val Error = Color(0xFFD81A1A) // Soft Modern Red for Errors
    val OnError = Color.White
    val ErrorContainer = Color(0xFFFFD7D0) // Muted Light Red
    val OnErrorContainer = Color(0xFF8B2E22) // Deep Red-Brown for readability

    val Outline = Color(0xFFD5D5D5) // Light Gray Outline
    val OutlineVariant = Color(0xFFA5A5A5) // Medium Gray
    val Scrim = Color.Black

    val SurfaceBright = Color(0xFFF8F8F8) // Almost White
    val SurfaceContainer = Color(0xFFF1F1F1) // Very Light Gray
    val SurfaceContainerHigh = Color(0xFFEAEAEA) // Soft Gray
    val SurfaceContainerHighest = Color(0xFFE0E0E0) // Medium Gray
    val SurfaceContainerLow = Color(0xFFFCFCFC) // Ultra Light Gray
    val SurfaceContainerLowest = Color.White
    val SurfaceDim = Color(0xFFE5E5E5) // Muted Soft Gray
}

object ColorDarkTokens {
    val Primary = Color(0xFFFE7C5D) // Same Modern Accent Color
    val OnPrimary = Color(0xFFFFFFFF) // Deep Warm Brown-Black
    val PrimaryContainer = Color(0xFF202425) // Darker Version of Primary
    val OnPrimaryContainer = Color(0xFFFFFFFF) // Soft Pastel Peach

    val InversePrimary = Color(0xFFFF9B87) // Muted Warm Orange-Red

    val Secondary = Color(0xFF3A3F41) // Modern Dark Gray
    val OnSecondary = Color(0xFFCACACA) // Light Gray Text
    val SecondaryContainer = Color(0xFF2F3436) // Slightly Softer Black
    val OnSecondaryContainer = Color(0xFFD9D9D9) // Muted White for Readability

    val Tertiary = Color(0xFF454A4C) // Cool Medium Gray
    val OnTertiary = Color(0xFFB0B5B6) // Softer Muted White
    val TertiaryContainer = Color(0xFF353A3B) // Deeper Muted Gray
    val OnTertiaryContainer = Color(0xFFC3C6C7) // Soft Off-White

    val Background = Color(0xFF35393A) // Modern Black
    val OnBackground = Color(0xFFE4E7E8) // Soft Light Gray for Readability

    val Surface = Color(0xFF282C2D) // Darker Muted Gray
    val OnSurface = Color(0xFFCFCFCF) // Soft White
    val SurfaceVariant = Color(0xFF3A3F41) // Medium Modern Gray
    val OnSurfaceVariant = Color(0xFFA0A5A6) // Softer White-Gray

    val SurfaceTint = Primary
    val InverseSurface = Color(0xFFE3E6E7) // Light Cool Gray
    val InverseOnSurface = Color(0xFF202425) // Dark Modern Black

    val Error = Color(0xFFD81A1A) // Soft Modern Red
    val OnError = Color(0xFFFFE0DD) // Light Muted Red
    val ErrorContainer = Color(0xFF7D3A31) // Deep Muted Red-Brown
    val OnErrorContainer = Color(0xFFFFB7AA) // Soft Pastel Red

    val Outline = Color(0xFF606567) // Medium Gray Outline
    val OutlineVariant = Color(0xFF8D9192) // Light Gray Outline
    val Scrim = Color.Black

    val SurfaceBright = Color(0xFF343A3B) // Dark Cool Gray
    val SurfaceContainer = Color(0xFF2C3132) // Muted Black Tint
    val SurfaceContainerHigh = Color(0xFF383D3F) // Medium Dark Gray Tint
    val SurfaceContainerHighest = Color(0xFF44494B) // Soft Black
    val SurfaceContainerLow = Color(0xFF222729) // Almost Black
    val SurfaceContainerLowest = Color.Black
    val SurfaceDim = Color(0xFF1C1F21) // True Dark Black
}


enum class MaterialColor(val color: Color) {
    PureRed(Color(0xFFD81A1A)),
    Red(Color(0xFFE91E63)),
    Blue(Color(0xFF2196F3)),
    Green(Color(0xFF4CAF50)),
    Yellow(Color(0xFFFFEB3B)),
    Purple(Color(0xFF9C27B0)),
    Teal(Color(0xFF009688)),
    Orange(Color(0xFFFF9800)),
    Pink(Color(0xFFE91E63)),
    Brown(Color(0xFF795548)),
    Grey(Color(0xFF9E9E9E)),
    Indigo(Color(0xFF3F51B5)),
    DeepPurple(Color(0xFF673AB7)),
    LightBlue(Color(0xFF03A9F4)),
    LightGreen(Color(0xFF8BC34A)),
    Lime(Color(0xFFCDDC39)),
    DeepOrange(Color(0xFFFF5722)),
    Cyan(Color(0xFF00BCD4)),
    Amber(Color(0xFFFFC107)),
    BlueGrey(Color(0xFF607D8B));

    companion object {
        fun fromArgb(argb: Int): MaterialColor? {
            return MaterialColor.entries.find { it.color.toArgb() == argb }
        }
    }
}

val MaterialRed = Color(0xFFE91E63)
val MaterialBlue = Color(0xFF2196F3)
//val MaterialGreen = Color(0xFF4CAF50)
//val MaterialYellow = Color(0xFFFFEB3B)
//val MaterialPurple = Color(0xFF9C27B0)
//val MaterialTeal = Color(0xFF009688)
//val MaterialOrange = Color(0xFFFF9800)
//val MaterialPink = Color(0xFFE91E63)
//val MaterialBrown = Color(0xFF795548)
//val MaterialGrey = Color(0xFF9E9E9E)
//val MaterialIndigo = Color(0xFF3F51B5)
//val MaterialDeepPurple = Color(0xFF673AB7)
//val MaterialLightBlue = Color(0xFF03A9F4)
//val MaterialLightGreen = Color(0xFF8BC34A)
//val MaterialLime = Color(0xFFCDDC39)
//val MaterialDeepOrange = Color(0xFFFF5722)
//val MaterialCyan = Color(0xFF00BCD4)
//val MaterialAmber = Color(0xFFFFC107)
//val MaterialBlueGrey = Color(0xFF607D8B)
//val MaterialIndigoLight = Color(0xFFB3E5FC)
//val MaterialDeepPurpleLight = Color(0xFFE1BEE7)
//val MaterialLightBlueLight = Color(0xFFBBDEFB)
//val MaterialLightGreenLight = Color(0xFFC8E6C9)
//val MaterialLimeLight = Color(0xFFF1F8E9)
//val MaterialDeepOrangeLight = Color(0xFFFFCCBC)
//val MaterialCyanLight = Color(0xFFB2EBF2)
//val MaterialAmberLight = Color(0xFFFFE0B2)
//val MaterialBlueGreyLight = Color(0xFFECEFF1)
//val MaterialIndigoDark = Color(0xFF1A237E)
//val MaterialDeepPurpleDark = Color(0xFF311B92)
//val MaterialLightBlueDark = Color(0xFF01579B)
//val MaterialLightGreenDark = Color(0xFF388E3C)
//val MaterialLimeDark = Color(0xFF827717)
//val MaterialDeepOrangeDark = Color(0xFFBF360C)
//val MaterialCyanDark = Color(0xFF006064)
//val MaterialAmberDark = Color(0xFFFFA000)
//val MaterialBlueGreyDark = Color(0xFF455A64)
//val MaterialIndigoDarker = Color(0xFF111444)
//val MaterialDeepPurpleDarker = Color(0xFF0B0737)
//val MaterialLightBlueDarker = Color(0xFF002984)
//val MaterialLightGreenDarker = Color(0xFF004D40)
//val MaterialLimeDarker = Color(0xFF003300)
//val MaterialDeepOrangeDarker = Color(0xFF6D0000)
//val MaterialCyanDarker = Color(0xFF003536)
//val MaterialAmberDarker = Color(0xFFFF8F00)
//val MaterialBlueGreyDarker = Color(0xFF263238)
//val MaterialIndigoLighter = Color(0xFFE8EAF6)
//val MaterialDeepPurpleLighter = Color(0xFFF3E5F5)
//val MaterialLightBlueLighter = Color(0xFFE1F5FE)