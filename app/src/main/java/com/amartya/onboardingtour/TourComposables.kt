package com.amartya.onboardingtour

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.CompositingStrategy

fun Modifier.tourTarget(tourState: TourState, id: String): Modifier = this.onGloballyPositioned {
    tourState.registerTarget(id, it)
}

@Composable
fun TourHost(
    tourState: TourState,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        val currentStep = tourState.currentStep
        val targetBounds = tourState.currentTargetBounds
        if (currentStep != null && targetBounds != null) {
            HighlightOverlay(
                targetBounds = targetBounds,
                targetPadding = tourState.targetPadding
            )
            TooltipPopup(
                targetBounds = targetBounds,
                step = currentStep,
                onNext = tourState::next,
                onDismiss = tourState::dismiss
            )
        }
    }
}

@Composable
private fun HighlightOverlay(targetBounds: Rect, targetPadding: Dp) {
    val scrimColor = Color.Black.copy(alpha = 0.7f)
    val cornerRadius = with(LocalDensity.current) { (targetPadding * 2).toPx() }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .clickable { }
    ) {
        drawRect(color = scrimColor)
        drawRoundRect(
            topLeft = targetBounds.topLeft,
            size = targetBounds.size,
            color = Color.Transparent,
            blendMode = BlendMode.Clear,
            cornerRadius = CornerRadius(cornerRadius)
        )
    }
}

@Composable
private fun TooltipPopup(
    targetBounds: Rect,
    step: TourStep,
    onNext: () -> Unit,
    onDismiss: () -> Unit
) {
    val density = LocalDensity.current
    val popupPositionProvider = TooltipPopupPositionProvider(
        targetBounds = targetBounds,
        tooltipPadding = 16.dp,
        density = density
    )
    Popup(
        popupPositionProvider = popupPositionProvider,
        onDismissRequest = onDismiss
    ) {
        Tooltip(
            step = step,
            onNext = onNext
        )
    }
}

@Composable
private fun Tooltip(
    step: TourStep,
    onNext: () -> Unit
) {
    Card(
        modifier = Modifier.widthIn(max = 300.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = step.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(text = step.message, fontSize = 16.sp)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onNext) {
                Text("Next")
            }
        }
    }
}