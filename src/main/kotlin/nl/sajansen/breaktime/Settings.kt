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

    var penaltyEmailUserName: String
        get() = prefs.get("penaltyEmailUserName", "Someone")
        set(value) = prefs.put("penaltyEmailUserName", value)

    var penaltyEmailToEmail: String
        get() = prefs.get("penaltyEmailToEmail", "")
        set(value) = prefs.put("penaltyEmailToEmail", value)

    var iftttWebHookKey: String
        get() = prefs.get("iftttWebHookKey", "")
        set(value) = prefs.put("iftttWebHookKey", value)

    var iftttWebHookEmailPenaltyEvent: String
        get() = prefs.get("iftttWebHookEmailPenaltyEvent", "")
        set(value) = prefs.put("iftttWebHookEmailPenaltyEvent", value)

    fun clearAll() {
        prefs.clear()
    }
}