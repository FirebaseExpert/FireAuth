package com.firebase_expert.fireauth.android.ui.screen.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.firebase_expert.fireauth.android.BuildConfig
import com.firebase_expert.fireauth.android.R

@Composable
fun MainBottomBar() {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.version)
            )
            Text(
                text = BuildConfig.VERSION_NAME
            )
        }
    }
}