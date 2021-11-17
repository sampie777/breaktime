package nl.sajansen.breaktime.control

import java.text.SimpleDateFormat
import java.util.*

object ControlUtils {
    fun determineCurrentScreen(): Screen {
        if (MainControl.isOnBreak())
            return Screen.BreakTime

        if (MainControl.isWorking())
            return Screen.WorkTime

        return Screen.NewPeriod
    }

    fun getRemainingWorkTime(): Date? {
        if (MainControl.workTimeEnd() == null)
            return null

        val remainingTime = MainControl.workTimeEnd()!! - Date().time
        return Date(remainingTime)
    }

    fun getRemainingBreakTime(): Date? {
        if (MainControl.breakTimeEnd() == null)
            return null

        val remainingTime = MainControl.breakTimeEnd()!! - Date().time
        return Date(remainingTime)
    }

    fun dateToString(date: Date?, format: String = "H:mm:ss"): String {
        if (date == null) {
            return ""
        }

        val dateFormat = SimpleDateFormat(format)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(date)
    }
}

enum class Screen {
    BreakTime,
    NewPeriod,
    WorkTime,
}