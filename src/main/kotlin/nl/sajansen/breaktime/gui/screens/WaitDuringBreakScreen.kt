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
    private val continueButton = JButton("Continue")
    private var skipClickCount = 0
    private var startTime = -1L

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
            it.isFocusable = false
            actionPanel.add(it)
        }

        actionPanel.add(Box.createHorizontalStrut(15))

        continueButton.also {
            it.toolTipText = "Continue on the work time left"
            it.font = textFont
            it.preferredSize = Dimension(200, 50)
            it.foreground = textColor
            it.background = buttonBackgroundColor
            it.addActionListener { continueWork() }
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

        continueButton.isVisible = MainControl.hasWorkTimeLeft()

        if (time == null) {
            skipButton.isVisible = true
            return
        }

        if (startTime < 0) {
            startTime = time.time
        }
        // Wait a second before showing the button
        skipButton.isVisible = startTime - time.time > 1000
    }

    private fun skipBreak() {
        skipClickCount++

        if (skipClickCount <= 1) {
            skipButton.text = "Are you sure?"
            return
        }
        if (skipClickCount == 2) {
            skipButton.text = "Really?"
            return
        }

        if (MainControl.skipBreak()) {
            return
        }

        skipButton.text = "Penalty failed"
        skipButton.toolTipText = "Is the penalty set up correctly?"

        if (skipClickCount > 10) {
            MainControl.endBreak()
        }
    }

    private fun continueWork() {
        MainControl.continueWork()
    }
}