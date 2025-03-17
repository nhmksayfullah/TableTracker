package app.tabletracker.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

enum class MaterialColor(val color: Color) {
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