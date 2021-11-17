package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.Settings
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

class ControlUtilsTest {
    @After
    fun after() {
        Settings.clearAll()
    }

    @Test
    fun `test converting last work time in seconds to hours`() {
        Settings.lastWorkTimeInSeconds = 0
        assertEquals(ControlUtils.getLastWorkTimeHours(), 0)

        Settings.lastWorkTimeInSeconds = 3599
        assertEquals(ControlUtils.getLastWorkTimeHours(), 0)

        Settings.lastWorkTimeInSeconds = 3600
        assertEquals(ControlUtils.getLastWorkTimeHours(), 1)

        Settings.lastWorkTimeInSeconds = 3601
        assertEquals(ControlUtils.getLastWorkTimeHours(), 1)

        Settings.lastWorkTimeInSeconds = 2 * 3600 - 1
        assertEquals(ControlUtils.getLastWorkTimeHours(), 1)

        Settings.lastWorkTimeInSeconds = 2 * 3600
        assertEquals(ControlUtils.getLastWorkTimeHours(), 2)
    }

    @Test
    fun `test converting last work time in seconds to minutes`() {
        Settings.lastWorkTimeInSeconds = 0
        assertEquals(ControlUtils.getLastWorkTimeMinutes(), 0)

        Settings.lastWorkTimeInSeconds = 59
        assertEquals(ControlUtils.getLastWorkTimeMinutes(), 0)

        Settings.lastWorkTimeInSeconds = 60
        assertEquals(ControlUtils.getLastWorkTimeMinutes(), 1)

        Settings.lastWorkTimeInSeconds = 61
        assertEquals(ControlUtils.getLastWorkTimeMinutes(), 1)

        Settings.lastWorkTimeInSeconds = 2 * 60 - 1
        assertEquals(ControlUtils.getLastWorkTimeMinutes(), 1)

        Settings.lastWorkTimeInSeconds = 2 * 60
        assertEquals(ControlUtils.getLastWorkTimeMinutes(), 2)

        Settings.lastWorkTimeInSeconds = 3600
        assertEquals(ControlUtils.getLastWorkTimeMinutes(), 0)

        Settings.lastWorkTimeInSeconds = 3600 + 60
        assertEquals(ControlUtils.getLastWorkTimeMinutes(), 1)

        Settings.lastWorkTimeInSeconds = 2 * 3600 + 60
        assertEquals(ControlUtils.getLastWorkTimeMinutes(), 1)
    }
}