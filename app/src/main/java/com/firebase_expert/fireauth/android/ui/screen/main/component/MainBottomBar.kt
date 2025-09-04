package com.firebase_expert.fireauth.android.ui.screen.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.firebase_expert.fireauth.android.R

@Composable
fun MainBottomBar(
    openGooglePlayApp: () -> Unit
) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.interested_in)
            )
            Text(
                text = stringResource(R.string.fireapp),
                Modifier.clickable {
                    openGooglePlayApp()
                },
                textDecoration = TextDecoration.Underline
            )
        }
    }
}