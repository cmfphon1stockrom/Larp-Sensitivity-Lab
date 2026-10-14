package com.larp.sensitivitylab

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager

data class DeviceProfile(
    val manufacturer: String, val model: String, val androidVersion: String,
    val soc: String, val cores: Int, val totalRamMb: Long,
    val widthPx: Int, val heightPx: Int, val logicalDpi: Int,
    val density: Float, val refreshHz: Float, val touchSamplingHz: Int? = null
)

object DeviceAnalyzer {
    fun scan(activity: Activity): DeviceProfile {
        val metrics = DisplayMetrics()
        val wm = activity.getSystemService(Activity.WINDOW_SERVICE) as WindowManager
        @Suppress("DEPRECATION")
        val display: Display? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            activity.display else wm.defaultDisplay

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = activity.windowManager.currentWindowMetrics.bounds
            metrics.widthPixels = bounds.width()
            metrics.heightPixels = bounds.height()
            metrics.density = activity.resources.displayMetrics.density
            metrics.densityDpi = activity.resources.displayMetrics.densityDpi
        } else {
            @Suppress("DEPRECATION")
            display?.getRealMetrics(metrics)
        }

        val am = activity.getSystemService(Activity.ACTIVITY_SERVICE) as android.app.ActivityManager
        val memory = android.app.ActivityManager.MemoryInfo()
        am.getMemoryInfo(memory)
        val soc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            Build.SOC_MODEL.takeIf { it.isNotBlank() } ?: "Not reported"
        else Build.HARDWARE ?: "Not reported"

        return DeviceProfile(
            manufacturer = Build.MANUFACTURER.orEmpty(),
            model = Build.MODEL.orEmpty(),
            androidVersion = Build.VERSION.RELEASE.orEmpty(),
            soc = soc,
            cores = Runtime.getRuntime().availableProcessors(),
            totalRamMb = memory.totalMem / (1024L * 1024L),
            widthPx = metrics.widthPixels,
            heightPx = metrics.heightPixels,
            logicalDpi = metrics.densityDpi,
            density = metrics.density,
            refreshHz = display?.refreshRate ?: 60f,
            touchSamplingHz = null
        )
    }
}
