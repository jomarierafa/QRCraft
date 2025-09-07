package com.jvrcoding.qrcraft.qr.presentation.models

import androidx.compose.ui.graphics.Color
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Contact
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Geo
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Link
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Phone
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Text
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.Wifi
import com.jvrcoding.qrcraft.core.presentation.util.UiText

enum class QrTypeUi(
    val title: UiText,
    val icon: Int,
    val iconColor: Color
) {
    TEXT(
        title = UiText.StringResource(R.string.text),
        icon = R.drawable.ic_text,
        iconColor = Text
    ),
    LINK(
        title = UiText.StringResource(R.string.link),
        icon = R.drawable.ic_link,
        iconColor = Link
    ),
    GEOLOCATION(
        title = UiText.StringResource(R.string.geolocation),
        icon = R.drawable.ic_geolocation,
        iconColor = Geo
    ),
    WIFI(
        title = UiText.StringResource(R.string.wifi),
        icon = R.drawable.ic_wifi,
        iconColor = Wifi
    ),
    CONTACT(
        title = UiText.StringResource(R.string.contact),
        icon = R.drawable.ic_contact,
        iconColor = Contact
    ),
    PHONE(
        title = UiText.StringResource(R.string.phone),
        icon = R.drawable.ic_phone,
        iconColor = Phone
    )
}