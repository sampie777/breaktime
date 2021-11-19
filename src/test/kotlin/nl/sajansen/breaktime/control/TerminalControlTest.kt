package nl.sajansen.breaktime.control

import org.junit.Test
import kotlin.test.assertEquals

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
}