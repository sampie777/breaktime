package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.Settings
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TerminalControlTest {
    @Test
    fun `test printing to terminal`() {
        val terminal = TerminalControl {}
        terminal.print("test")
        assertEquals("> test\n", terminal.terminalText)
    }

    @Test
    fun `test printing to terminal with custom end`() {
        val terminal = TerminalControl {}
        terminal.print("test", end = "X")
        assertEquals("> testX", terminal.terminalText)
    }

    @Test
    fun `test execute empty command does nothing`() {
        val terminal = TerminalControl {}
        terminal.handleCommand("")
        assertEquals("> ", terminal.terminalText)
    }

    @Test
    fun `test execute help command`() {
        val terminal = TerminalControl {}
        terminal.handleCommand("help")
        assertTrue(terminal.terminalText.contains("Available commands"))
    }

    @Test
    fun `test get break time`() {
        val terminal = TerminalControl {}
        Settings.lastBreakTimeInSeconds = 100

        terminal.handleCommand("get breaktime")
        assertEquals("> 100\n", terminal.terminalText)
    }

    @Test
    fun `test set break time`() {
        val terminal = TerminalControl {}
        Settings.lastBreakTimeInSeconds = 100

        terminal.handleCommand("set breaktime 200")
        assertEquals("> Done.\n", terminal.terminalText)
        assertEquals(200, Settings.lastBreakTimeInSeconds)
    }

    @Test
    fun `test get after hours start time`() {
        val terminal = TerminalControl {}
        // 21:15
        Settings.afterHoursStartTimeInSeconds = 21 * 3600 + 15 * 60

        terminal.handleCommand("get afterHoursStartTime")
        assertEquals("> 21:15 (${Settings.afterHoursStartTimeInSeconds})\n", terminal.terminalText)
    }

    @Test
    fun `test set after hours start time`() {
        val terminal = TerminalControl {}
        Settings.afterHoursStartTimeInSeconds = 0

        terminal.handleCommand("set afterHoursStartTime 21:15")
        assertEquals("> Done.\n", terminal.terminalText)
        assertEquals(21 * 3600 + 15 * 60, Settings.afterHoursStartTimeInSeconds)
    }

    @Test
    fun `test set after hours start time with overflow`() {
        val terminal = TerminalControl {}
        Settings.afterHoursStartTimeInSeconds = 0

        terminal.handleCommand("set afterHoursStartTime 40:90")
        assertEquals("> Done.\n", terminal.terminalText)
        assertEquals((16 + 1) * 3600 + 30 * 60, Settings.afterHoursStartTimeInSeconds)
    }
}