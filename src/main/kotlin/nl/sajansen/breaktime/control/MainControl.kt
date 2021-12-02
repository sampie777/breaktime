package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.ApplicationRuntimeSettings
import nl.sajansen.breaktime.Settings
import nl.sajansen.breaktime.control.penalties.EmailPenalty
import nl.sajansen.breaktime.events.EventsDispatcher
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.max
import kotlin.math.min

object MainControl {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    var workTimeEnd: Date? = null
    var breakTimeEnd: Date? = null
    private var workTimeLeft: Int = 0
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
    fun hasWorkTimeLeft() = workTimeLeft > 0

    fun startNewPeriod(hours: Int, minutes: Int, rememberValue: Boolean = true) = startNewPeriod(hours * 3600 + minutes * 60, rememberValue)

    fun startNewPeriod(seconds: Int, rememberValue: Boolean = true) {
        if (seconds < 0) {
            return logger.error("Can't start period with negative time")
        }

        if (rememberValue) {
            Settings.lastWorkTimeInSeconds = seconds
        }
        val useSeconds = if (ControlUtils.isAfterHours()) min(seconds, Settings.maxWorkTimeAfterHoursInSeconds) else seconds

        breakTimeEnd = null
        workTimeLeft = 0
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
        val breakTimeStart = (workTimeEnd ?: Date()).time
        breakTimeEnd = Date(breakTimeStart + Settings.lastBreakTimeInSeconds * 1000L)

        workTimeLeft = if (workTimeEnd == null) 0 else max(0, (workTimeEnd!!.time - Date().time) / 1000).toInt()
        workTimeEnd = null

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

    fun continueWork() {
        if (workTimeLeft <= 0) {
            return
        }

        startNewPeriod(workTimeLeft, rememberValue = false)
        EventLogger.log(EventLogger.Event.WorkContinued)
    }
}