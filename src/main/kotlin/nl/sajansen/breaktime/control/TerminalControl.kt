package nl.sajansen.breaktime.control

import nl.sajansen.breaktime.ApplicationInfo
import nl.sajansen.breaktime.Settings
import nl.sajansen.breaktime.exitApplication
import org.slf4j.LoggerFactory
import java.awt.event.KeyEvent

class TerminalControl(private val onClose: () -> Unit) {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    var terminalText = "> "
    var command = ""
    val history = arrayListOf<String>()
    var historySearchOffset = 0

    fun onKeyPressed(e: KeyEvent) {
        if (e.keyCode in 44..93 || e.keyCode == 32) {
            command += e.keyChar
            return
        }

        when (e.keyCode) {
            10 -> executeAction()
            8 -> command = command.dropLast(1)
            38 -> if (history.size > historySearchOffset) {
                command = history[history.size - (++historySearchOffset)]
            }
            40 -> if (historySearchOffset > 0) {
                command = if (historySearchOffset == 1) "" else history[history.size - (--historySearchOffset)]
            }
            else -> logger.info("Unknown key code: " + e.keyCode.toString())
        }
    }

    private fun handleCommand(command: String) {
        when (command) {
            "help" -> print(
                """
              Available commands: 
                help                    Show this message
                exit                    Close this popup
                quit                    Quit application
                version                 Show application version and info
                get worktime            Returns last work time value
                set worktime <value>    Set this to the value specified (number)
                get breaktime           Returns last break time value
                set breaktime <value>   Set this to the value specified (number)
                get minimize            Returns last break time value
                set minimize <value>    Set this to the value specified (true|false)
            """.trimIndent()
            )
            "exit" -> onClose()
            "quit" -> exitApplication()
            "version" -> print("${ApplicationInfo.name} - ${ApplicationInfo.version} (${ApplicationInfo.artifactId}) by ${ApplicationInfo.author}")
            "get worktime" -> print(Settings.lastWorkTimeInSeconds.toString())
            "get breaktime" -> print(Settings.lastBreakTimeInSeconds.toString())
            "get minimize" -> print(Settings.minimizeWorkTimeScreen.toString())
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

                "set minimize (true|false)".toRegex().also {
                    val match = it.find(command) ?: return@also
                    val value = match.groupValues[1]
                    Settings.minimizeWorkTimeScreen = value == "true"
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

    private fun print(text: String, end: String = "\n") {
        terminalText += text + end
    }
}