package com.amartya.onboardingtour

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class TourStep(
    val id: String,
    val title: String,
    val message: String
)

@Stable
class TourState(
    private val steps: List<TourStep>,
    private val coroutineScope: CoroutineScope,
    onTourFinish: () -> Unit
) {
    private val targetCoordinates = mutableStateMapOf<String, LayoutCoordinates>()
    private var currentStepIndex by mutableStateOf<Int?>(null)

    val currentStep: TourStep?
        get() = currentStepIndex?.let { steps.getOrNull(it) }

    val targetPadding: Dp = 8.dp

    val currentTargetBounds: Rect?
        get() {
            val stepId = currentStep?.id ?: return null
            val coordinates = targetCoordinates[stepId] ?: return null
            if (!coordinates.isAttached) return null
            val size = coordinates.size
            val position = coordinates.positionInRoot()
            val paddingPx = targetPadding.value
            return Rect(
                left = position.x - paddingPx,
                top = position.y - paddingPx,
                right = position.x + size.width + paddingPx,
                bottom = position.y + size.height + paddingPx
            )
        }

    private val onFinish = {
        currentStepIndex = null
        onTourFinish()
    }

    fun registerTarget(id: String, coordinates: LayoutCoordinates) {
        targetCoordinates[id] = coordinates
    }

    fun start() {
        coroutineScope.launch {
            currentStepIndex = 0
        }
    }

    fun next() {
        coroutineScope.launch {
            val nextIndex = (currentStepIndex ?: -1) + 1
            if (nextIndex < steps.size) {
                currentStepIndex = nextIndex
            } else {
                onFinish()
            }
        }
    }

    fun dismiss() {
        coroutineScope.launch {
            onFinish()
        }
    }
}

@Composable
fun rememberTourState(
    steps: List<TourStep>,
    coroutineScope: CoroutineScope,
    onTourFinish: () -> Unit
): TourState {
    return remember(steps, coroutineScope) {
        TourState(steps, coroutineScope, onTourFinish)
    }
}