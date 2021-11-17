package nl.sajansen.breaktime

import java.awt.Color
import java.awt.Font

private val streamRobotoThin = ApplicationInfo::class.java.getResourceAsStream("/nl/sajansen/breaktime/Roboto-Thin.ttf")
private val streamRobotoLight = ApplicationInfo::class.java.getResourceAsStream("/nl/sajansen/breaktime/Roboto-Light.ttf")
val defaultFont: Font = Font.createFont(Font.TRUETYPE_FONT, streamRobotoLight)

val textColor = Color(230, 230, 230)
val buttonBackgroundColor = Color(80, 80, 80)