package nl.sajansen.breaktime.gui

import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.defaultFont
import nl.sajansen.breaktime.textColorLight
import org.slf4j.LoggerFactory
import java.util.*
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.Timer

class Clock : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val timer: Timer
    private val clockLabel = JLabel("")

    init {
        createGui()

        timer = Timer(1000) { timerStep() }
        timer.start()
        timerStep()
    }

    private fun createGui() {
        background = null

        clockLabel.font = defaultFont.deriveFont(16f)
        clockLabel.foreground = textColorLight
        add(clockLabel)
    }

    private fun timerStep() {
        clockLabel.text = ControlUtils.dateToString(Date(), format = "H:mm", localTimeZone = true)
    }
}