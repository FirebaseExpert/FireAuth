package com.firebase_expert.fireauth.android.ui.screen.auth.auth_entry.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.firebase_expert.fireauth.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthEntryTopBar() {
    TopAppBar (
        title = {
            Text(
                text = stringResource(R.string.auth_entry_screen_title)
            )
        }
    )
}