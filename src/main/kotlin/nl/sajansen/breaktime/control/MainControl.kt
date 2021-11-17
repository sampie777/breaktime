package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.Settings
import nl.sajansen.breaktime.events.EventsDispatcher
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.concurrent.schedule

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
        logger.info("Start new period")
        isOnBreak = false
        workTimer = Timer("workTimer", true).schedule(delay = seconds * 1000L) {
            forceBreak()
        }
        Settings.lastWorkTimeInSeconds = seconds
        EventsDispatcher.onStateUpdated()
    }

    fun forceBreak() {
        logger.info("Ending work period")
        takeABrake()
    }

    fun takeABrake() {
        logger.info("Taking a break")
        isOnBreak = true
        workTimer?.cancel()
        workTimer = null
        breakTimer = Timer("breakTimer", true).schedule(delay = 5 * 60 * 1000L) {
            endBreak()
        }
        EventsDispatcher.onStateUpdated()
    }

    fun endBreak() {
        logger.info("Break ended")
        isOnBreak = false
        breakTimer?.cancel()
        breakTimer = null
        EventsDispatcher.onStateUpdated()
    }
}