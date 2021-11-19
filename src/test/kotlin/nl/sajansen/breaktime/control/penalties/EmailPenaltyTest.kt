package nl.sajansen.breaktime.control.penalties

import nl.sajansen.breaktime.Settings
import nl.sajansen.breaktime.utils.format
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EmailPenaltyTest {
    @Test
    fun `test validation without key`() {
        Settings.iftttWebHookKey = ""
        Settings.iftttWebHookEmailPenaltyEvent = "x"
        Settings.penaltyEmailToEmail = "x@gmail.com"
        Settings.penaltyEmailUserName = "x"
        val penalty = EmailPenalty()
        assertFalse(penalty.validate())
    }

    @Test
    fun `test validation without event`() {
        Settings.iftttWebHookKey = "x"
        Settings.iftttWebHookEmailPenaltyEvent = ""
        Settings.penaltyEmailToEmail = "x@gmail.com"
        Settings.penaltyEmailUserName = "x"
        val penalty = EmailPenalty()
        assertFalse(penalty.validate())
    }

    @Test
    fun `test validation without email`() {
        Settings.iftttWebHookKey = "x"
        Settings.iftttWebHookEmailPenaltyEvent = "x"
        Settings.penaltyEmailToEmail = ""
        Settings.penaltyEmailUserName = "x"
        val penalty = EmailPenalty()
        assertFalse(penalty.validate())
    }

    @Test
    fun `test validation without user name`() {
        Settings.iftttWebHookKey = "x"
        Settings.iftttWebHookEmailPenaltyEvent = "x"
        Settings.penaltyEmailToEmail = "x@gmail.com"
        Settings.penaltyEmailUserName = ""
        val penalty = EmailPenalty()
        assertTrue(penalty.validate())
    }

    @Test
    fun `test get data`() {
        Settings.iftttWebHookKey = "x"
        Settings.iftttWebHookEmailPenaltyEvent = "x"
        Settings.penaltyEmailToEmail = "x@gmail.com"
        Settings.penaltyEmailUserName = "x"
        val penalty = EmailPenalty()
        assertEquals("""
                {
                  "value1": "x",
                  "value2": "${Date().format("H:mm")}",
                  "value3": "x@gmail.com"
                }
        """.trimIndent(), penalty.getData())
    }
}