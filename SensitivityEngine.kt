package com.larp.sensitivitylab

import kotlin.math.roundToInt

data class SensitivitySettings(
    val camera: Int, val redDot: Int, val scope2x: Int, val scope4x: Int,
    val sniper: Int, val freeLook: Int, val suggestedMouseDpi: Int,
    val suggestedRefreshHz: Int, val notes: List<String>
)

object SensitivityEngine {
    private fun clamp(value: Int) = value.coerceIn(0, 200)

    fun generate(device: DeviceProfile): SensitivitySettings {
        val shortSide = minOf(device.widthPx, device.heightPx)
        val resolutionAdjustment = when {
            shortSide >= 1200 -> 3
            shortSide >= 900 -> 1
            else -> 0
        }
        val refreshAdjustment = when {
            device.refreshHz >= 120f -> 2
            device.refreshHz >= 90f -> 1
            else -> 0
        }
        val densityAdjustment = when {
            device.logicalDpi >= 500 -> -2
            device.logicalDpi >= 400 -> -1
            else -> 0
        }
        val adj = resolutionAdjustment + refreshAdjustment + densityAdjustment
        return SensitivitySettings(
            camera = clamp(145 + adj), redDot = clamp(135 + adj),
            scope2x = clamp(120 + adj), scope4x = clamp(105 + adj),
            sniper = clamp(75 + adj), freeLook = clamp(110 + adj),
            suggestedMouseDpi = 800, suggestedRefreshHz = device.refreshHz.roundToInt(),
            notes = listOf(
                "Starting values only; tune in training mode.",
                "Logical display DPI is not mouse hardware DPI.",
                "Touch sampling rate is not exposed reliably on all Android devices.",
                "Mouse/keymapper scaling must be configured separately."
            )
        )
    }
}
