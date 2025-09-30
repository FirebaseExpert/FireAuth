package com.firebase_expert.fireauth.android.ui.screen.auth.verify_code.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.firebase_expert.fireauth.android.ui.screen.auth.component.ContinueButton
import com.firebase_expert.fireauth.android.ui.theme.FireAuthManagerTheme
import com.firebase_expert.fireauth.android.ui.screen.auth.util.transformation.DigitOnlyInputTransformation
import com.firebase_expert.fireauth.android.util.DARK_MODE
import com.firebase_expert.fireauth.android.util.LIGHT_MODE
import kotlinx.coroutines.flow.drop
import com.firebase_expert.fireauth.android.R
import com.firebase_expert.fireauth.android.util.TEST_COUNTRY_CODE
import com.firebase_expert.fireauth.android.util.TEST_PHONE_NUMBER

const val ENTER_CODE_SENT_TO = "Enter the code sent to"

@Composable
fun VerifyCodeContent(
    innerPadding: PaddingValues,
    phoneNumber: String,
    onAuthWithVerificationCode: (String) -> Unit,
    onSendAgain: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(innerPadding)
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val digitCount = 6
        val digits = List(digitCount) { rememberTextFieldState() }
        val focusRequesters = remember { List(digitCount) { FocusRequester() } }
        var focusedIndex by rememberSaveable { mutableIntStateOf(0) }
        val digitInput by remember {
            derivedStateOf {
                digits.joinToString("") { digitState ->
                    digitState.text.toString()
                }
            }
        }

        LaunchedEffect(focusedIndex) {
            focusRequesters[focusedIndex].requestFocus()
        }

        Text(
            text = "$ENTER_CODE_SENT_TO $phoneNumber"
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(digitCount) { index ->
                val digitState = digits[index]

                OutlinedTextField(
                    state = digitState,
                    modifier = Modifier
                        .width(48.dp)
                        .focusRequester(focusRequesters[index])
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                focusedIndex = index
                            }
                        }
                        .onPreviewKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown &&
                                keyEvent.key == Key.Backspace &&
                                digitState.text.isEmpty() &&
                                index > 0
                            ) {
                                digits[index - 1].edit {
                                    replace(0, digits[index - 1].text.length, "")
                                }
                                focusedIndex = index - 1
                                true
                            } else {
                                false
                            }
                        },
                    inputTransformation = DigitOnlyInputTransformation().maxLength(1),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                )

                LaunchedEffect(digitState) {
                    snapshotFlow {
                        digitState.text
                    }.drop(1).collect { text ->
                        if (focusedIndex == index) {
                            if (text.isNotEmpty() && index < digitCount - 1) {
                                focusedIndex = index + 1
                            }
                        }
                    }
                }
            }
        }

        ContinueButton(
            onClick = {
                onAuthWithVerificationCode(digitInput)
            },
            enabled = digitInput.length == digitCount,
            isLoading = isLoading
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.did_not_get_code)
            )
            Text(
                text = stringResource(R.string.send_again),
                modifier = Modifier.clickable {
                    onSendAgain()
                },
                textDecoration = TextDecoration.Underline
            )
        }
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
fun VerifyCodeContentPreview() {
    FireAuthManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            VerifyCodeContent(
                innerPadding = PaddingValues(),
                phoneNumber = "$TEST_COUNTRY_CODE$TEST_PHONE_NUMBER",
                onAuthWithVerificationCode = {},
                onSendAgain = {},
                isLoading = false
            )
        }
    }
}