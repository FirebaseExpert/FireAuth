package com.firebase_expert.fireauth.android.ui.screen.auth.verify_code.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.firebase_expert.fireauth.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyCodeTopBar(
    onBackButtonClick: () -> Unit
) {
    TopAppBar (
        title = {
            Text(
                text = stringResource(R.string.verify_code_screen_title)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackButtonClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back_icon)
                )
            }
        }
    )
}