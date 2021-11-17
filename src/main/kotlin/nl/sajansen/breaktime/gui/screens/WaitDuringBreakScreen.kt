package nl.sajansen.breaktime.gui.screens

import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.MainControl
import org.slf4j.LoggerFactory
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.Timer

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
        add(JLabel("Take a break"))
        add(countDownLabel)

        JButton("Done").also {
            it.addActionListener { MainControl.endBreak() }
            add(it)
        }
    }

    private fun timerStep() {
        val time = ControlUtils.getRemainingBreakTime()
        countDownLabel.text = ControlUtils.dateToString(time)
    }
}