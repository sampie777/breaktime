package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.ApplicationInfo
import nl.sajansen.breaktime.Settings
import nl.sajansen.breaktime.exitApplication
import org.slf4j.LoggerFactory
import java.awt.event.KeyEvent
import java.util.*

class TerminalControl(private val onClose: () -> Unit) {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    var terminalText = "> "
    var command = ""
    val history = arrayListOf<String>()
    var historySearchOffset = 0

    fun onKeyPressed(e: KeyEvent) {
        if (e.keyCode in 44..111 || e.keyCode == KeyEvent.VK_SPACE
            || e.keyCode in 151..153 || e.keyCode in 160..222 || e.keyCode in 512..523
        ) {
            command += e.keyChar
            return
        }

        when (e.keyCode) {
            KeyEvent.VK_ENTER -> executeAction()
            KeyEvent.VK_BACK_SPACE -> command = command.dropLast(1)
            KeyEvent.VK_UP -> if (history.size > historySearchOffset) {
                command = history[history.size - (++historySearchOffset)]
            }
            KeyEvent.VK_DOWN -> if (historySearchOffset > 0) {
                command = if (historySearchOffset == 1) "" else history[history.size - (--historySearchOffset)]
            }
        }
    }

    fun handleCommand(command: String) {
        when (command) {
            "help" -> print(
                """
              Available commands: 
                help                    Show this message
                exit                    Close this popup
                quit                    Quit application
                version                 Show application version and info
                log file                Get time log file name
                log clear               Clear time log
                get worktime            Returns last work time value
                set worktime <seconds>  Set this to the value specified
                get breaktime           Returns last break time value
                set breaktime <seconds> Set this to the value specified
                get afterHoursStartTime Returns time when after hours starts
                set afterHoursStartTime <hours>:<minutes>   Set this to the value specified
                get minimize            Returns last break time value
                set minimize <true|false>    Set this to the value specified
                get penaltyemail name   The name in the email for this penalty
                set penaltyemail name <value>
                get penaltyemail to     The email for the penalty to be sent to
                set penaltyemail to <value>
                get penaltyemail IFTTT event
                set penaltyemail IFTTT event <value>
                get IFTTT webhookkey
                set IFTTT webhookkey <value>
            """.trimIndent()
            )
            "exit" -> onClose()
            "quit" -> exitApplication()
            "version" -> print("${ApplicationInfo.name} - ${ApplicationInfo.version} (${ApplicationInfo.artifactId}) by ${ApplicationInfo.author}")
            "get worktime" -> print(Settings.lastWorkTimeInSeconds.toString())
            "get breaktime" -> print(Settings.lastBreakTimeInSeconds.toString())
            "get afterHoursStartTime" -> print(ControlUtils.dateToString(Date(Settings.afterHoursStartTimeInSeconds * 1000L), "H:mm") + " (${Settings.afterHoursStartTimeInSeconds})")
            "get minimize" -> print(Settings.minimizeWorkTimeScreen.toString())
            "get penaltyemail name" -> print(Settings.penaltyEmailUserName)
            "get penaltyemail to" -> print(Settings.penaltyEmailToEmail)
            "get penaltyemail IFTTT event" -> print(Settings.iftttWebHookEmailPenaltyEvent)
            "get IFTTT webhookkey" -> print(Settings.iftttWebHookKey)
            "log file" -> print(EventLogger.fileName)
            "log clear" -> {
                EventLogger.clearAll()
                print("Deleted log file")
            }
            "" -> {
            }
            else -> {
                "set worktime (-?\\d+)".toRegex().also {
                    val match = it.find(command) ?: return@also
                    val value = match.groupValues[1].toIntOrNull()

                    if (value == null) {
                        return print("Could not convert value to int: $value")
                    }
                    if (value <= 1) {
                        return print("Value cannot be 0 or less")
                    }

                    Settings.lastWorkTimeInSeconds = value
                    return print("Done.")
                }

                "set breaktime (-?\\d+)".toRegex().also {
                    val match = it.find(command) ?: return@also
                    val value = match.groupValues[1].toIntOrNull()

                    if (value == null) {
                        return print("Could not convert value to int: $value")
                    }
                    if (value <= 1) {
                        return print("Value cannot be 0 or less")
                    }

                    Settings.lastBreakTimeInSeconds = value
                    return print("Done.")
                }

                "set afterHoursStartTime (\\d+):(\\d+)".toRegex().also {
                    val match = it.find(command) ?: return@also
                    val hours = match.groupValues[1].toIntOrNull()
                    val minutes = match.groupValues[2].toIntOrNull()

                    if (hours == null) {
                        return print("Could not convert value to int: $minutes")
                    }
                    if (minutes == null) {
                        return print("Could not convert value to int: $minutes")
                    }

                    var totalSeconds = hours * 3600 + minutes * 60
                    // Handle overflow
                    totalSeconds %= (24 * 3600)

                    Settings.afterHoursStartTimeInSeconds = totalSeconds
                    return print("Done.")
                }

                "set minimize (true|false)".toRegex().also {
                    val match = it.find(command) ?: return@also
                    val value = match.groupValues[1]
                    Settings.minimizeWorkTimeScreen = value == "true"
                    return print("Done.")
                }

                "getpenalty email name (.*)".toRegex().also {
                    val match = it.find(command) ?: return@also
                    var value = match.groupValues[1].trim()
                    value = if (value == "") "Someone" else value
                    Settings.penaltyEmailUserName = value
                    return print("Done.")
                }

                "getpenalty email to (.*)".toRegex().also {
                    val match = it.find(command) ?: return@also
                    val value = match.groupValues[1].trim()
                    Settings.penaltyEmailToEmail = value
                    return print("Done.")
                }

                "getpenalty email IFTTT event (.*)".toRegex().also {
                    val match = it.find(command) ?: return@also
                    val value = match.groupValues[1].trim()
                    Settings.iftttWebHookEmailPenaltyEvent = value
                    return print("Done.")
                }

                "get IFTTT webhookkey (.*)".toRegex().also {
                    val match = it.find(command) ?: return@also
                    val value = match.groupValues[1].trim()
                    Settings.iftttWebHookKey = value
                    return print("Done.")
                }

                print("Unknown command: $command")
            }
        }
    }

    private fun executeAction() {
        terminalText += command + "\n"
        handleCommand(command)
        history.add(command)
        historySearchOffset = 0
        terminalText += "> "
        command = ""
    }

    fun print(text: String, end: String = "\n") {
        terminalText += text + end
    }
}