package nl.sajansen.breaktime.gui.screens

import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.MainControl
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.Font
import javax.swing.*

class DuringWorkTimeScreen : JPanel() {
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

        val clockFont = Font("Courier", Font.PLAIN, 80)
        val textFont = Font("Dialog", Font.PLAIN, 16)

        val clockPanel = JPanel().also {
            it.maximumSize = Dimension(9999, 0)
        }
        val actionPanel = JPanel().also {
            it.maximumSize = Dimension(9999, 0)
        }

        countDownLabel.font = clockFont

        clockPanel.add(countDownLabel)

        JButton("Take a break").also {
            it.addActionListener { MainControl.takeABrake() }
            it.font = textFont
            actionPanel.add(it)
        }

        add(Box.createVerticalGlue())
        add(JLabel("Baby I'm working").also { it.font = textFont })
        add(Box.createVerticalStrut(15))
        add(clockPanel)
        add(Box.createVerticalStrut(70))
        add(actionPanel)
        add(Box.createVerticalGlue())
    }

    override fun removeNotify() {
        super.removeNotify()
        timer.stop()
    }

    private fun timerStep() {
        val time = ControlUtils.getRemainingWorkTime()
        countDownLabel.text = ControlUtils.dateToString(time)
    }
}