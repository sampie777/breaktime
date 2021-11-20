package nl.sajansen.breaktime.control

import org.junit.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MainControlTest {
    @Test
    fun `test checktimers ends work time`() {
        MainControl.workTimeEnd = Date(Date().time - 60 * 1000L)
        MainControl.endBreak()
        assertTrue(MainControl.isWorking())
        assertFalse(MainControl.isOnBreak())

        MainControl.checkTimers()

        assertTrue(MainControl.isOnBreak())
        assertFalse(MainControl.isWorking())
        assertNull(MainControl.workTimeEnd())
    }

    @Test
    fun `test checktimers starts break time`() {
        MainControl.workTimeEnd = Date(Date().time - 60 * 1000L)
        MainControl.endBreak()
        assertTrue(MainControl.isWorking())
        assertFalse(MainControl.isOnBreak())

        MainControl.checkTimers()

        assertTrue(MainControl.isOnBreak())
        assertFalse(MainControl.isWorking())
        assertNotNull(MainControl.breakTimeEnd())
    }

    @Test
    fun `test checktimers continues work time`() {
        MainControl.startNewPeriod(60 * 1000)
        assertTrue(MainControl.isWorking())
        assertFalse(MainControl.isOnBreak())

        MainControl.checkTimers()

        assertFalse(MainControl.isOnBreak())
        assertTrue(MainControl.isWorking())
        assertNotNull(MainControl.workTimeEnd())
        assertNull(MainControl.breakTimeEnd())
    }

    @Test
    fun `test checktimers ends break time`() {
        MainControl.workTimeEnd = null
        MainControl.breakTimeEnd = Date(Date().time - 60 * 1000L)
        assertFalse(MainControl.isWorking())
        assertTrue(MainControl.isOnBreak())

        MainControl.checkTimers()

        assertFalse(MainControl.isOnBreak())
        assertFalse(MainControl.isWorking())
        assertNull(MainControl.breakTimeEnd())
    }

    @Test
    fun `test checktimers continues break time`() {
        MainControl.workTimeEnd = null
        MainControl.breakTimeEnd = Date(Date().time + 60 * 1000L)
        assertFalse(MainControl.isWorking())
        assertTrue(MainControl.isOnBreak())

        MainControl.checkTimers()

        assertTrue(MainControl.isOnBreak())
        assertFalse(MainControl.isWorking())
        assertNotNull(MainControl.breakTimeEnd())
    }
}