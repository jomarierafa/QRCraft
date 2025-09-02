package com.jvrcoding.qrcraft.core.presentation.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFFEBFF69)
val Surface = Color(0xFFEDF2F5)
val SurfaceHigher = Color(0xFFFFFFFF)
val OnSurface = Color(0xFF273037)
val OnSurfaceAlt = Color(0xFF505F6A)
val OnSurfaceDisabled = Color(0xFF8C99A2)
val Overlay = Color(0xFF000000).copy(alpha = 0.5f)
val OnOverlay = Color(0xFFFFFFFF)
val Outline = Color(0xFFCCD5DC)
val Link = Color(0xFF373F05)
val LinkBG = Color(0xFFEBFF69).copy(alpha = 0.3f)
val Error = Color(0xFFF12244)
val Success = Color(0xFF4DDA9D)
val Text = Color(0xFF583DC5)
val TextBG = Color(0xFF583DC5).copy(alpha = 0.1f)
val Contact = Color(0xFF259570)
val ContactBG = Color(0xFF259570).copy(alpha = 0.1f)
val Geo = Color(0xFFB51D5C)
val GeoBG = Color(0xFFB51D5C).copy(alpha = 0.1f)
val Phone = Color(0xFFC86017)
val PhoneBG = Color(0xFFC86017).copy(alpha = 0.1f)
val Wifi = Color(0xFF1F44CD)
val WifiBG = Color(0xFF1F44CD).copy(alpha = 0.1f)

val ColorScheme.onSurfaceAlt: Color
    get() = OnSurfaceAlt

val ColorScheme.onSurfaceDisabled: Color
    get() = OnSurfaceDisabled

val ColorScheme.overlay: Color
    get() = Overlay

val ColorScheme.onOverlay: Color
    get() = OnOverlay

val ColorScheme.linkBG: Color
    get() = LinkBG

val ColorScheme.success: Color
    get() = Success