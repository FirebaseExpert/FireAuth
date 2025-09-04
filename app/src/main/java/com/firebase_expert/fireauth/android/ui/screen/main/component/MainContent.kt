package com.firebase_expert.fireauth.android.ui.screen.main.component

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.firebase_expert.fireauth.android.R
import com.firebase_expert.fireauth.android.ui.theme.FireAppManagerTheme
import com.firebase_expert.fireauth.android.util.DARK_MODE
import com.firebase_expert.fireauth.android.util.LIGHT_MODE

const val APP_URL = "https://www.firebase-expert.com/#apps"

@Composable
fun MainContent(
    innerPadding: PaddingValues,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 30.sp),
            )
            Row {
                Text(
                    text = stringResource(R.string.powered_by_firebase),
                    style = MaterialTheme.typography.bodySmall,
                )
                FirebaseLogo(
                    size = 16.dp
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.interested_in_buying),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
            )
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = APP_URL.toUri()
                    }
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = stringResource(R.string.visit_website_button)
                )
            }
        }
    }
}

@Composable
fun FirebaseLogo(
    size: Dp
) {
    Image(
        imageVector = ImageVector.vectorResource(R.drawable.ic_firebase),
        contentDescription = stringResource(R.string.app_technology),
        modifier = Modifier.size(size)
    )
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
fun MainContentPreview() {
    FireAppManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            MainContent(
                innerPadding = PaddingValues()
            )
        }
    }
}