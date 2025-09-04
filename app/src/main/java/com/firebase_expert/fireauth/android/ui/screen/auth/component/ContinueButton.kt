package com.firebase_expert.fireauth.android.ui.screen.auth.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase_expert.fireauth.android.R

@Composable
fun ContinueButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(
            size = 6.dp
        ),
        elevation = ButtonDefaults.buttonElevation(5.dp),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 12.dp
        )
    ) {
        if (isLoading) {
            LoadingIndicator()
        } else {
            Text(
                text = stringResource(R.string.continue_button),
                color = LocalContentColor.current,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun LoadingIndicator(
    numberOfCircles: Int = 3,
    circleSize: Dp = 6.dp,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    spaceBetween: Dp = 4.dp,
    travelDistance: Dp = 12.dp
) {
    val distance = with(LocalDensity.current) { travelDistance.toPx() }
    val animation = rememberInfiniteTransition()

    Row(
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        repeat(numberOfCircles) { index ->
            val delay = index * 100
            val animValue by animation.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 using LinearOutSlowInEasing
                        1.0f at 300 using LinearOutSlowInEasing
                        0.0f at 600 using LinearOutSlowInEasing
                        0.0f at 1200 using LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart,
                    initialStartOffset = StartOffset(delay)
                )
            )

            Box(
                modifier = Modifier.size(circleSize).graphicsLayer {
                    translationY = -animValue * distance
                }.background(
                    color = circleColor,
                    shape = CircleShape
                )
            )
        }
    }
}