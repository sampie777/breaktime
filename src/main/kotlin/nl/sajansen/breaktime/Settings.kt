package nl.sajansen.breaktime

import java.util.prefs.Preferences

object Settings {
    private val prefs = Preferences.userRoot().node(ApplicationInfo.artifactId + (if (ApplicationRuntimeSettings.testing) "-test" else ""))

    var lastWorkTimeInSeconds
        get() = prefs.getInt("lastWorkTimeInSeconds", 15 * 60)
        set(value) = prefs.putInt("lastWorkTimeInSeconds", value)

    fun clearAll() {
        prefs.clear()
    }
}