package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.ApplicationRuntimeSettings
import nl.sajansen.breaktime.Settings
import nl.sajansen.breaktime.control.penalties.EmailPenalty
import nl.sajansen.breaktime.events.EventsDispatcher
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.min

object MainControl {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    var workTimeEnd: Date? = null
    var breakTimeEnd: Date? = null
    private var timer: TimerTask? = null

    fun init() {
        if (ApplicationRuntimeSettings.testing) {
            return
        }

        timer = Timer("mainTimer", true)
            .schedule(period = 1000L, delay = 0L) { checkTimers() }
    }

    fun checkTimers() {
        if (workTimeEnd != null && Date().after(workTimeEnd)) {
            forceBreak()
        }
        if (breakTimeEnd != null && Date().after(breakTimeEnd)) {
            endBreak()
        }
    }

    fun isWorking(): Boolean {
        return workTimeEnd != null
    }

    fun isOnBreak(): Boolean {
        return breakTimeEnd != null
    }

    fun workTimeEnd() = workTimeEnd
    fun breakTimeEnd() = breakTimeEnd

    fun startNewPeriod(hours: Int, minutes: Int) = startNewPeriod(hours * 3600 + minutes * 60)

    fun startNewPeriod(seconds: Int) {
        if (seconds < 0) {
            return logger.error("Can't start period with negative time")
        }

        Settings.lastWorkTimeInSeconds = seconds
        val useSeconds = if (ControlUtils.isAfterHours()) min(seconds, Settings.maxWorkTimeAfterHoursInSeconds) else seconds

        breakTimeEnd = null
        workTimeEnd = Date(Date().time + useSeconds * 1000L)

        EventsDispatcher.onStateUpdated()
        EventLogger.logWorkTimeStarted()
    }

    fun forceBreak() {
        startBreak()
    }

    fun takeABrake() {
        startBreak()
        EventLogger.logTookABreak()
    }

    fun startBreak() {
        workTimeEnd = null
        breakTimeEnd = Date(Date().time + Settings.lastBreakTimeInSeconds * 1000L)
        EventsDispatcher.onStateUpdated()
        EventLogger.logWorkTimeEnded()
    }

    fun skipBreak(): Boolean {
        val penalty = EmailPenalty()
        if (!penalty.execute()) {
            logger.error("Cannot skip break: penalty not completed")
            EventLogger.logBreakPenaltyFailed()
            return false
        }

        endBreak()
        EventLogger.logBreakSkipped()
        return true
    }

    fun endBreak() {
        breakTimeEnd = null
        EventsDispatcher.onStateUpdated()
    }
}