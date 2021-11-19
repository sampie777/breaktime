package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.ApplicationInfo
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EventLoggerTest {
    private val tempFile = File.createTempFile(ApplicationInfo.name + "-test", ".csv")

    @Before
    fun before() {
        tempFile.deleteOnExit()
        EventLogger.fileName = tempFile.absolutePath
    }

    @Test
    fun `test logger writes logs to file`() {
        EventLogger.logApplicationStarted()
        EventLogger.logApplicationStopped()

        val lines = tempFile.readLines().map { it.split(",").last() }
        assertEquals(3, lines.size)
        assertEquals("timestamp,action", tempFile.readLines()[0])
        assertEquals("ApplicationStarted", lines[1])
        assertEquals("ApplicationStopped", lines[2])
    }

    @Test
    fun `test clear all deletes log file`() {
        assertTrue(tempFile.exists())
        EventLogger.clearAll()
        assertFalse(tempFile.exists())
    }
}