package nl.sajansen.breaktime

import nl.sajansen.breaktime.control.EventLogger
import nl.sajansen.breaktime.control.MainControl
import nl.sajansen.breaktime.events.EventsDispatcher
import nl.sajansen.breaktime.gui.mainFrame.MainFrame
import nl.sajansen.breaktime.utils.getCurrentJarDirectory
import org.slf4j.LoggerFactory
import java.awt.EventQueue
import kotlin.system.exitProcess


private val logger = LoggerFactory.getLogger("application")

fun main() {
    ApplicationRuntimeSettings.testing = false

    logger.info("Starting application ${ApplicationInfo.artifactId}:${ApplicationInfo.version}")
    logger.info("Executing JAR directory: " + getCurrentJarDirectory(ApplicationInfo).absolutePath)

    EventQueue.invokeLater {
        MainFrame.createAndShow()
    }

    MainControl.init()
    EventLogger.logApplicationStarted()
}

fun exitApplication() {
    logger.info("Shutting down application")


    try {
        EventLogger.logApplicationStopped()
    } catch (t: Throwable) {
        logger.error("Failed to write log")
        t.printStackTrace()
    }

    try {
        logger.info("Closing windows...")
        EventsDispatcher.windowClosing(MainFrame.getInstance())
    } catch (t: Throwable) {
        logger.warn("Failed to correctly close windows")
        t.printStackTrace()
    }

    logger.info("Shutdown finished")
    exitProcess(0)
}
