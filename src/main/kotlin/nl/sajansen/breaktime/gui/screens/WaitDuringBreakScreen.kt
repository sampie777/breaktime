package nl.sajansen.breaktime.gui.screens

import nl.sajansen.breaktime.buttonBackgroundColor
import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.MainControl
import nl.sajansen.breaktime.textColor
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.Font
import javax.swing.*

class WaitDuringBreakScreen : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val timer: Timer
    private val countDownLabel = JLabel("")

    init {
        createGui()

        timer = Timer(100) { timerStep() }
        timer.start()
        timerStep()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        background = null

        val clockFont = Font("Courier", Font.PLAIN, 120)
        val textFont = Font("Dialog", Font.PLAIN, 24)

        val clockPanel = JPanel().also {
            it.maximumSize = Dimension(9999, 0)
            it.background = null
        }
        val actionPanel = JPanel().also {
            it.maximumSize = Dimension(9999, 0)
            it.background = null
        }

        countDownLabel.font = clockFont
        countDownLabel.foreground = textColor

        clockPanel.add(countDownLabel)

        JButton("Skip").also {
            it.addActionListener { MainControl.endBreak() }
            it.font = textFont
            it.foreground = textColor
            it.background = buttonBackgroundColor
            actionPanel.add(it)
        }

        add(Box.createVerticalGlue())
        add(JLabel("Take a break").also {
            it.font = textFont
            it.foreground = textColor
        })
        add(Box.createVerticalStrut(15))
        add(clockPanel)
        add(Box.createVerticalStrut(70))
        add(actionPanel)
        add(Box.createVerticalGlue())
    }

    private fun timerStep() {
        val time = ControlUtils.getRemainingBreakTime()
        countDownLabel.text = ControlUtils.dateToString(time)
    }
}