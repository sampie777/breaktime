package nl.sajansen.breaktime.gui.screens

import nl.sajansen.breaktime.buttonBackgroundColor
import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.MainControl
import nl.sajansen.breaktime.defaultFont
import nl.sajansen.breaktime.textColor
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.Font
import javax.swing.*

class WaitDuringBreakScreen : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val timer: Timer
    private val countDownLabel = JLabel("")
    private val skipButton = JButton("Skip")
    private var skipClickCount = 0

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
        val textFont = defaultFont.deriveFont(32f)

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

        skipButton.also {
            it.toolTipText = "Take a penalty to skip this break"
            it.font = textFont
            it.preferredSize = Dimension(300, 50)
            it.foreground = textColor
            it.background = buttonBackgroundColor
            it.addActionListener { skipBreak() }
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

    private fun skipBreak() {
        if (MainControl.skipBreak()) {
            return
        }

        skipButton.text = "Penalty failed"
        skipButton.toolTipText = "Is the penalty set up correctly?"

        if (skipClickCount++ > 10) {
            MainControl.endBreak()
        }
    }
}