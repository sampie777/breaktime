package nl.sajansen.breaktime

import java.util.prefs.Preferences

object Settings {
    private val prefs = Preferences.userRoot().node(ApplicationInfo.artifactId + (if (ApplicationRuntimeSettings.testing) "-test" else ""))

    var lastWorkTimeInSeconds
        get() = prefs.getInt("lastWorkTimeInSeconds", 15 * 60)
        set(value) = prefs.putInt("lastWorkTimeInSeconds", value)

    var lastBreakTimeInSeconds
        get() = prefs.getInt("lastBreakTimeInSeconds", 5 * 60)
        set(value) = prefs.putInt("lastBreakTimeInSeconds", value)

    var maxWorkTimeAfterHoursInSeconds
        get() = prefs.getInt("maxWorkTimeAfterHoursInSeconds", 5 * 60)
        set(value) = prefs.putInt("maxWorkTimeAfterHoursInSeconds", value)

    var afterHoursStartTimeInSeconds
        get() = prefs.getInt("afterHoursStartTimeInSeconds", 21 * 60 * 60)
        set(value) = prefs.putInt("afterHoursStartTimeInSeconds", value)

    var minimizeWorkTimeScreen
        get() = prefs.getBoolean("minimizeWorkTimeScreen", false)
        set(value) = prefs.putBoolean("minimizeWorkTimeScreen", value)

    fun clearAll() {
        prefs.clear()
    }
}