package com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.firebase_expert.fireauth.android.R
import com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.component.text_field.EmailTextField
import com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.component.text_field.PhoneTextField
import com.firebase_expert.fireauth.android.ui.screen.auth.component.ContinueButton
import com.firebase_expert.fireauth.android.ui.theme.FireAuthManagerTheme
import com.firebase_expert.fireauth.android.util.DARK_MODE
import com.firebase_expert.fireauth.android.util.LIGHT_MODE
import com.firebase_expert.fireauth.android.util.TEST_CODE
import com.firebase_expert.fireauth.android.util.TEST_COUNTRY_CODE
import com.firebase_expert.fireauth.android.util.TEST_PHONE_NUMBER
import com.firebase_expert.fireauth.android.util.extensions.prefixWithPlus

const val UNLISTED_COUNTRIES_CREDENTIALS = "For unlisted countries, use:"
const val PHONE = "Phone"
const val CODE = "Code"

@Composable
fun AuthEntryContent(
    innerPadding: PaddingValues,
    onSendSignInLinkToEmail: (String) -> Unit,
    onVerifyPhoneNumber: (String) -> Unit,
    isLoading: Boolean
) {
    val keyboard = LocalSoftwareKeyboardController.current
    var showEmailAuth by rememberSaveable { mutableStateOf(true) }
    val emailState = rememberTextFieldState()
    var countryPhoneCode by rememberSaveable { mutableStateOf("") }
    val phoneState = rememberTextFieldState()

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(innerPadding)
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (showEmailAuth) {
            EmailTextField(
                emailState = emailState
            )
            ContinueButton(
                onClick = {
                    onSendSignInLinkToEmail(emailState.text.toString())
                    emailState.clearText()
                    keyboard?.hide()
                },
                enabled = emailState.text.isNotEmpty(),
                isLoading = isLoading
            )
        } else {
            PhoneTextField(
                phoneState = phoneState,
                onCountrySelected = { selectedCountryPhoneCode ->
                    countryPhoneCode = selectedCountryPhoneCode
                }
            )
            Column {
                Text(
                    text = UNLISTED_COUNTRIES_CREDENTIALS
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.None)) {
                            append("• $PHONE: ")
                        }
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("$TEST_COUNTRY_CODE$TEST_PHONE_NUMBER")
                        }
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.None)) {
                            append("• $CODE: ")
                        }
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(TEST_CODE)
                        }
                    }
                )
            }
            ContinueButton(
                onClick = {
                    val phoneNumber = phoneState.text.toString()
                    onVerifyPhoneNumber("$countryPhoneCode$phoneNumber".prefixWithPlus())
                    phoneState.clearText()
                    keyboard?.hide()
                },
                enabled = phoneState.text.isNotEmpty(),
                isLoading = isLoading
            )
        }
        AuthEntryHorizontalDivider(
            resId = R.string.or
        )
        ContinueWithButton(
            resId = if (showEmailAuth) R.string.continue_with_phone_button else R.string.continue_with_email_button,
            imageVector = if (showEmailAuth) Icons.Filled.Phone else Icons.Filled.Email,
            contentDescription = if (showEmailAuth) R.string.phone_icon else R.string.email_icon,
            onContinueWithButtonClick = {
                showEmailAuth = !showEmailAuth
            },
            isLoading = isLoading
        )
    }
}

@Preview(
    name = LIGHT_MODE,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = DARK_MODE,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AuthContentPreview() {
    FireAuthManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            AuthEntryContent(
                innerPadding = PaddingValues(),
                onSendSignInLinkToEmail = {},
                onVerifyPhoneNumber = {},
                isLoading = false
            )
        }
    }
}