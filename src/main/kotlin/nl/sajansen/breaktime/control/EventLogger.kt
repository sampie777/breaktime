package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.ApplicationInfo
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

object EventLogger {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    var fileName = "${ApplicationInfo.name}.csv"

    fun logWorkTimeStarted() {
        log("WorkTimeStarted")
    }

    fun logWorkTimeEnded() {
        log("WorkTimeEnded")
    }

    fun logApplicationStarted() {
        log("ApplicationStarted")
    }

    fun logApplicationStopped() {
        log("ApplicationStopped")
    }

    fun clearAll() {
        File(fileName).delete()
    }

    private fun log(action: String) {
        try {
            openFile().also {
                it.appendText("${Date().time},$action\n")
            }
        } catch (t: Throwable) {
            logger.error("Failed to write log")
            t.printStackTrace()
        }
    }

    private fun openFile(): File {
        val file = File(fileName)
        if (file.exists() && file.length() > 10) {
            return file
        }

        file.createNewFile()
        writeHeader(file)
        return file
    }

    private fun writeHeader(file: File) {
        file.writeText("timestamp,action\n")
    }
}