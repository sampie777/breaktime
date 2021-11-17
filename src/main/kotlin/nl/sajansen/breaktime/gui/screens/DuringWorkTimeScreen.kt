package nl.sajansen.breaktime.gui.screens

import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.MainControl
import org.slf4j.LoggerFactory
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.Timer

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
        add(JLabel("Baby I'm working"))
        add(countDownLabel)

        JButton("Take a break").also {
            it.addActionListener { takeABrake() }
            add(it)
        }
    }

    override fun removeNotify() {
        super.removeNotify()
        timer.stop()
    }

    private fun takeABrake() {
        MainControl.takeABrake()
    }

    private fun timerStep() {
        val time = ControlUtils.getRemainingWorkTime()
        countDownLabel.text = ControlUtils.dateToString(time)
    }
}