package com.amartya.onboardingtour

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider

class TooltipPopupPositionProvider(
    private val targetBounds: Rect,
    private val tooltipPadding: Dp = 16.dp,
    private val density: Density
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val tooltipPaddingPx = with(density) { tooltipPadding.toPx() }

        val popupX = targetBounds.left + (targetBounds.width - popupContentSize.width) / 2

        val spaceAbove = targetBounds.top
        val spaceBelow = windowSize.height - targetBounds.bottom

        val popupY = if (spaceAbove > spaceBelow) {
            targetBounds.top - popupContentSize.height - tooltipPaddingPx
        } else {
            targetBounds.bottom + tooltipPaddingPx
        }

        val coercedX = popupX.coerceIn(0f, windowSize.width - popupContentSize.width.toFloat())
        val coercedY = popupY.coerceIn(0f, windowSize.height - popupContentSize.height.toFloat())

        return IntOffset(coercedX.toInt(), coercedY.toInt())
    }
}