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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jvrcoding.qrcraft.R
import com.jvrcoding.qrcraft.core.presentation.designsystem.components.QRCraftButton
import com.jvrcoding.qrcraft.core.presentation.designsystem.theme.QRCraftTheme

@Composable
fun QRContactDataForm(
    name: TextFieldState,
    email: TextFieldState,
    phoneNumber: TextFieldState,
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
            state = name,
            hint = stringResource(R.string.name),
            lineLimits = TextFieldLineLimits.SingleLine
        )
        QRTextField(
            state = email,
            hint = stringResource(R.string.email),
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardType = KeyboardType.Email
        )
        QRTextField(
            state = phoneNumber,
            hint = stringResource(R.string.phone_number),
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardType = KeyboardType.Phone
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
private fun QRContactDataFormPreview() {
    QRCraftTheme {
        QRContactDataForm(
            name = rememberTextFieldState(),
            email = rememberTextFieldState(),
            phoneNumber = rememberTextFieldState(),
            buttonEnable = false,
            onButtonClick = {},
        )
    }
}