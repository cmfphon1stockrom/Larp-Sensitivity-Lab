package com.larp.sensitivitylab

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var deviceInfo: TextView
    private lateinit var settingsInfo: TextView
    private var currentReport = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        deviceInfo = findViewById(R.id.deviceInfo)
        settingsInfo = findViewById(R.id.settingsInfo)
        findViewById<Button>(R.id.copyButton).setOnClickListener { copySettings() }
        findViewById<Button>(R.id.rescanButton).setOnClickListener { scanAndRender() }
        scanAndRender()
    }

    private fun scanAndRender() {
        val device = DeviceAnalyzer.scan(this)
        val settings = SensitivityEngine.generate(device)
        val ramGb = device.totalRamMb / 1024.0
        deviceInfo.text = """
            DEVICE PROFILE

            Phone: ${device.manufacturer} ${device.model}
            Android: ${device.androidVersion}
            SoC / hardware: ${device.soc}
            CPU cores: ${device.cores}
            Total RAM: ${String.format(Locale.getDefault(), "%.1f", ramGb)} GB
            Resolution: ${device.widthPx} x ${device.heightPx}
            Logical density: ${device.logicalDpi} dpi
            Refresh rate: ${String.format(Locale.getDefault(), "%.1f", device.refreshHz)} Hz
            Touch sampling: ${device.touchSamplingHz?.let { "$it Hz" } ?: "Unavailable"}
        """.trimIndent()

        currentReport = """
            LARP SENSITIVITY LAB

            Camera: ${settings.camera}
            Red Dot: ${settings.redDot}
            2x Scope: ${settings.scope2x}
            4x Scope: ${settings.scope4x}
            Sniper Scope: ${settings.sniper}
            Free Look: ${settings.freeLook}

            Suggested mouse DPI: ${settings.suggestedMouseDpi}
            Display refresh rate: ${settings.suggestedRefreshHz} Hz

            NOTES
            ${settings.notes.joinToString("\n") { "• $it" }}
        """.trimIndent()
        settingsInfo.text = currentReport
    }

    private fun copySettings() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Sensitivity settings", currentReport))
        Toast.makeText(this, "Settings copied. Enter them manually.", Toast.LENGTH_LONG).show()
    }
}
