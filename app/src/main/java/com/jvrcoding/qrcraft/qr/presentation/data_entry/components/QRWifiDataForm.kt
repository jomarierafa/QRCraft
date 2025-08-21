package com.jvrcoding.qrcraft.qr.presentation.data_entry.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftButton
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme

@Composable
fun QRWifiDataForm(
    ssid: TextFieldState,
    password: TextFieldState,
    encryption: TextFieldState,
    buttonEnable: Boolean,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .widthIn(max = 480.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QRTextField(
            state = ssid,
            hint = stringResource(R.string.ssid),
            lineLimits = TextFieldLineLimits.SingleLine
        )
        QRTextField(
            state = password,
            hint = stringResource(R.string.password),
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        QRTextField(
            state = encryption,
            hint = stringResource(R.string.encryption_type),
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        Spacer(modifier = Modifier)
        QRCraftButton(
            enabled = buttonEnable,
            text = stringResource(R.string.generate_qr_code),
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.primary
        )
    }

}

@Preview
@Composable
private fun QRWifiDataFormPreview() {
    QRCraftTheme {
        QRWifiDataForm(
            ssid = rememberTextFieldState(),
            password = rememberTextFieldState(),
            encryption = rememberTextFieldState(),
            buttonEnable = true,
            onButtonClick = {}
        )
    }
}