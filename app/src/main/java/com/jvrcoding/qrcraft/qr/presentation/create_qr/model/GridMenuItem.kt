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
import com.jvrcoding.qrcraft.qr.domain.qr.QrType

sealed class GridMenuItem(
    val label: UiText,
    val iconRes: Int,
    val iconColor: Color,
    val qrType: QrType
) {
    object QrText : GridMenuItem(
        label = UiText.StringResource(R.string.text),
        iconRes = R.drawable.ic_text,
        iconColor = Text,
        qrType = QrType.TEXT
    )
    object QrLink : GridMenuItem(
        label = UiText.StringResource(R.string.link),
        iconRes = R.drawable.ic_link,
        iconColor = Link,
        qrType = QrType.LINK
    )
    object QrContact : GridMenuItem(
        label = UiText.StringResource(R.string.contact),
        iconRes = R.drawable.ic_contact,
        iconColor = Contact,
        qrType = QrType.CONTACT
    )
    object QrPhone : GridMenuItem(
        label = UiText.StringResource(R.string.phone),
        iconRes = R.drawable.ic_phone,
        iconColor = Phone,
        qrType = QrType.PHONE
    )
    object QrGeolocation : GridMenuItem(
        label = UiText.StringResource(R.string.geolocation),
        iconRes = R.drawable.ic_geolocation,
        iconColor = Geo,
        qrType = QrType.GEOLOCATION
    )
    object QrWifi : GridMenuItem(
        label = UiText.StringResource(R.string.wifi),
        iconRes = R.drawable.ic_wifi,
        iconColor = Wifi,
        qrType = QrType.WIFI
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