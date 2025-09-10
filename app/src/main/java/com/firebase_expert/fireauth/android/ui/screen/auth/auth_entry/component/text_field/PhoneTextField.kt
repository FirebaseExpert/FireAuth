package com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.component.text_field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.firebase_expert.fireauth.android.R
import com.firebase_expert.fireauth.android.domain.model.Country
import com.firebase_expert.fireauth.android.ui.screen.auth.util.transformation.DigitOnlyInputTransformation
import com.firebase_expert.fireauth.android.ui.screen.auth.util.transformation.PhoneNumberOutputTransformation
import com.firebase_expert.fireauth.android.util.COUNTRIES
import com.firebase_expert.fireauth.android.util.extensions.prefixWithPlus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneTextField(
    phoneState: TextFieldState,
    onCountrySelected: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var expanded by remember { mutableStateOf(false) }
    val countries = COUNTRIES.map { (code, phoneCode) ->
        Country(
            name = code,
            phoneCode = phoneCode
        )
    }.sortedBy { country ->
        country.name
    }
    var selectedCountry by remember { mutableStateOf(countries[8]) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        onCountrySelected(selectedCountry.phoneCode)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { isExpanded ->
            expanded = isExpanded
        }
    ) {
        OutlinedTextField(
            state = phoneState,
            modifier = Modifier.fillMaxWidth()
                .focusRequester(focusRequester)
                .semantics {
                    contentType = ContentType.PhoneNumber
                },
            label = {
                Text(stringResource(R.string.phone_label))
            },
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 8.dp
                    ).menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = selectedCountry.phoneCode.prefixWithPlus()
                    )
                    Icon(
                        imageVector = if (expanded) {
                            Icons.Default.ArrowDropUp
                        } else {
                            Icons.Default.ArrowDropDown
                        },
                        contentDescription = if (expanded) {
                            stringResource(R.string.collapse_dropdown_menu_icon)
                        } else {
                            stringResource(R.string.expand_dropdown_menu_icon)
                        }
                    )
                    VerticalDivider(
                        modifier = Modifier.height(56.dp),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            trailingIcon = {
                if (phoneState.text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_phone_input_icon),
                        modifier = Modifier.clickable {
                            phoneState.clearText()
                        }
                    )
                }
            },
            inputTransformation = DigitOnlyInputTransformation().maxLength(10),
            outputTransformation = PhoneNumberOutputTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            countries.forEach { country ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = country.name
                            )
                            Text(
                                text = "(${country.phoneCode.prefixWithPlus()})"
                            )
                        }
                    },
                    onClick = {
                        selectedCountry = country
                        onCountrySelected(country.phoneCode)
                        focusRequester.requestFocus()
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhoneTextFieldPreview() = PhoneTextField(
    phoneState = rememberTextFieldState(),
    onCountrySelected = {}
)