package nl.sajansen.breaktime.gui.screens

import nl.sajansen.breaktime.control.MainControl
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.*

class NewPeriodScreen : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val hourInput = JSpinner()
    private val minuteInput = JSpinner()

    init {
        createGui()
    }

    private fun createGui() {
        val size = Dimension(60, 20)
        val border = BorderFactory.createLineBorder(Color(168, 168, 168))
        val font = Font("Dialog", Font.PLAIN, 14)

        hourInput.also {
            it.model = SpinnerNumberModel(0, 0, 23, 1)
            it.preferredSize = size
            it.border = border
            it.font = font
        }

        minuteInput.also {
            it.model = SpinnerNumberModel(20, 0, 59, 5)
            it.preferredSize = size
            it.border = border
            it.font = font
        }

        add(JLabel("Give me"))
        add(hourInput)
        add(JLabel("hour(s) and"))
        add(minuteInput)
        add(JLabel("minutes"))

        JButton("Please").also {
            it.addActionListener { startNewPeriod() }
            add(it)
        }
    }

    private fun startNewPeriod() {
        val hours = hourInput.value as Int
        val minutes = minuteInput.value as Int

        if (hours == 0 && minutes == 0) {
            return
        }

        MainControl.startNewPeriod(hours, minutes)
    }
}