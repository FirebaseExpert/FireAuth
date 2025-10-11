package com.firebase_expert.fireauth.android.ui.screen.main.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase_expert.fireauth.android.R
import com.firebase_expert.fireauth.android.domain.model.App
import com.firebase_expert.fireauth.android.ui.theme.FireAuthManagerTheme
import com.firebase_expert.fireauth.android.util.APPS_DIRECTORY
import com.firebase_expert.fireauth.android.util.APPS_FORM_URL
import com.firebase_expert.fireauth.android.util.APP_NAME_DIRECTORY
import com.firebase_expert.fireauth.android.util.DARK_MODE
import com.firebase_expert.fireauth.android.util.FIREBASE_EXPERT_URL
import com.firebase_expert.fireauth.android.util.FIREBASE_URL
import com.firebase_expert.fireauth.android.util.LIGHT_MODE
import com.firebase_expert.fireauth.android.util.extension.openPlayStore
import com.firebase_expert.fireauth.android.util.extension.openWebsite

@Composable
fun MainContent(
    innerPadding: PaddingValues,
    apps: List<App>
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.powered_by)
                )
                ClickableText(
                    text = stringResource(R.string.firebase),
                    onClick = {
                        context.openWebsite(
                            url = FIREBASE_URL
                        )
                    }
                )
                FirebaseLogo(
                    size = 16.dp
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.developed_by)
                )
                ClickableText(
                    text = stringResource(R.string.firebase_expert),
                    onClick = {
                        context.openWebsite(
                            url = FIREBASE_EXPERT_URL
                        )
                    }
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.interested_in_buying)
            )
            ActionButton(
                resId = R.string.open_request_form_button,
                onClick = {
                    context.openWebsite(
                        url = APPS_FORM_URL
                    )
                }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.for_more_details_visit)
                )
                ClickableText(
                    text = stringResource(R.string.app_website),
                    onClick = {
                        context.openWebsite(
                            url = "$FIREBASE_EXPERT_URL/$APPS_DIRECTORY/$APP_NAME_DIRECTORY"
                        )
                    }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.discover_more)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(apps) { app ->
                    ClickableText(
                        text = app.name,
                        onClick = {
                            context.openPlayStore(
                                url = app.url
                            )
                        }
                    )
                }
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

@Composable
fun ClickableText(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        modifier = Modifier.clickable {
            onClick()
        },
        textDecoration = TextDecoration.Underline
    )
}

@Composable
fun ActionButton(
    resId: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            size = 6.dp
        ),
        elevation = ButtonDefaults.buttonElevation(5.dp),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 12.dp
        )
    ) {
        Text(
            text = stringResource(resId),
            color = LocalContentColor.current,
            fontSize = 18.sp
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
fun MainContentPreview() {
    FireAuthManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            MainContent(
                innerPadding = PaddingValues(),
                apps = listOf(
                    App(
                        name = "FireApp",
                        url = "https://play.google.com/store/apps/details?id=com.firebase_expert.fireapp"

                    ),
                    App(
                        name = "BeerCounter",
                        url = "https://play.google.com/store/apps/details?id=app.beer_counter"

                    )
                )
            )
        }
    }
}