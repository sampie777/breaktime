package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.Settings
import org.slf4j.LoggerFactory
import java.awt.Frame
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.floor

object ControlUtils {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    fun determineCurrentScreen(): Screen {
        if (MainControl.isOnBreak())
            return Screen.BreakTime

        if (MainControl.isWorking())
            return Screen.WorkTime

        return Screen.NewPeriod
    }

    fun getRemainingWorkTime(offset: Int = 700): Date? {
        if (MainControl.workTimeEnd() == null)
            return null

        val remainingTime = MainControl.workTimeEnd()!!.time - Date().time + offset
        return Date(remainingTime)
    }

    fun getRemainingBreakTime(offset: Int = 700): Date? {
        if (MainControl.breakTimeEnd() == null)
            return null

        val remainingTime = MainControl.breakTimeEnd()!!.time - Date().time + offset
        return Date(remainingTime)
    }

    fun dateToString(date: Date?, format: String = "H:mm:ss", localTimeZone: Boolean = false): String {
        if (date == null) {
            return ""
        }

        val dateFormat = SimpleDateFormat(format)
        dateFormat.timeZone = if (localTimeZone) TimeZone.getDefault() else TimeZone.getTimeZone("UTC")
        return dateFormat.format(date)
    }

    fun setAutoFocusser(frame: Frame): Timer {
        return timer("focusserTimer", daemon = true, initialDelay = 0, period = 200) {
//            frame.requestFocus()
            frame.extendedState = Frame.NORMAL
        }
    }

    fun getLastWorkTimeHours(): Int {
        return floor(Settings.lastWorkTimeInSeconds / 3600.0).toInt()
    }

    fun getLastWorkTimeMinutes(): Int {
        return (floor(Settings.lastWorkTimeInSeconds / 60.0) % 60).toInt()
    }

    fun isAfterHours(): Boolean {
        val now = Calendar.getInstance()
        return now.get(Calendar.HOUR_OF_DAY) * 3600 +
                now.get(Calendar.MINUTE) * 60 +
                now.get(Calendar.SECOND) > Settings.afterHoursStartTimeInSeconds
    }
}

enum class Screen {
    BreakTime,
    NewPeriod,
    WorkTime,
}