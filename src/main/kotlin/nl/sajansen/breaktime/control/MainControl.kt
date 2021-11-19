package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.Settings
import nl.sajansen.breaktime.control.penalties.EmailPenalty
import nl.sajansen.breaktime.events.EventsDispatcher
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.min

object MainControl {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private var isOnBreak = false
    private var workTimer: TimerTask? = null
    private var breakTimer: TimerTask? = null

    fun init() {}

    fun isOnBreak(): Boolean {
        return isOnBreak
    }

    fun isWorking(): Boolean {
        return workTimer != null
    }

    fun workTimeEnd() = workTimer?.scheduledExecutionTime()
    fun breakTimeEnd() = breakTimer?.scheduledExecutionTime()

    fun startNewPeriod(hours: Int, minutes: Int) = startNewPeriod(hours * 3600 + minutes * 60)

    fun startNewPeriod(seconds: Int) {
        Settings.lastWorkTimeInSeconds = seconds
        val useSeconds = if (ControlUtils.isAfterHours()) min(seconds, Settings.maxWorkTimeAfterHoursInSeconds) else seconds

        isOnBreak = false
        workTimer = Timer("workTimer", true).schedule(delay = useSeconds * 1000L) {
            forceBreak()
        }
        EventsDispatcher.onStateUpdated()
        EventLogger.logWorkTimeStarted()
    }

    fun forceBreak() {
        takeABrake()
    }

    fun takeABrake() {
        isOnBreak = true
        workTimer?.cancel()
        workTimer = null
        breakTimer = Timer("breakTimer", true).schedule(delay = Settings.lastBreakTimeInSeconds * 1000L) {
            endBreak()
        }
        EventsDispatcher.onStateUpdated()
        EventLogger.logWorkTimeEnded()
    }

    fun skipBreak(): Boolean {
        val penalty = EmailPenalty()
        if (!penalty.execute()) {
            logger.error("Cannot skip break: penalty not completed")
            return false
        }

        endBreak()
        return true
    }

    fun endBreak() {
        isOnBreak = false
        breakTimer?.cancel()
        breakTimer = null
        EventsDispatcher.onStateUpdated()
    }
}