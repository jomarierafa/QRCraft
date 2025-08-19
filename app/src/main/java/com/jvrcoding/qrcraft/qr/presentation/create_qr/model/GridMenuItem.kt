package com.jvrcoding.qrcraft.qr.presentation.create_qr.model


import androidx.compose.ui.graphics.Color
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Contact
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Geo
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Link
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Phone
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Text
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Wifi
import com.jvrcoding.qrcraft.core.presentation.util.UiText

sealed class GridMenuItem(val label: UiText, val iconRes: Int, val iconColor: Color) {
    object QrText : GridMenuItem(
        label = UiText.StringResource(R.string.text),
        iconRes = R.drawable.ic_text,
        iconColor = Text
    )
    object QrLink : GridMenuItem(
        label = UiText.StringResource(R.string.link),
        iconRes = R.drawable.ic_link,
        iconColor = Link
    )
    object QrContact : GridMenuItem(
        label = UiText.StringResource(R.string.contact),
        iconRes = R.drawable.ic_contact,
        iconColor = Contact
    )
    object QrPhone : GridMenuItem(
        label = UiText.StringResource(R.string.phone),
        iconRes = R.drawable.ic_phone,
        iconColor = Phone
    )
    object QrGeolocation : GridMenuItem(
        label = UiText.StringResource(R.string.geolocation),
        iconRes = R.drawable.ic_geolocation,
        iconColor = Geo
    )
    object QrWifi : GridMenuItem(
        label = UiText.StringResource(R.string.wifi),
        iconRes = R.drawable.ic_wifi,
        iconColor = Wifi
    )
}

val menuItems = listOf(
    GridMenuItem.QrText,
    GridMenuItem.QrLink,
    GridMenuItem.QrContact,
    GridMenuItem.QrPhone,
    GridMenuItem.QrGeolocation,
    GridMenuItem.QrWifi
)