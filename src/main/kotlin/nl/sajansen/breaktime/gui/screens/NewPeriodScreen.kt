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
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        val spinnerSize = Dimension(60, 20)
        val spinnerBorder = BorderFactory.createLineBorder(Color(168, 168, 168))
        val spinnerFont = Font("Dialog", Font.PLAIN, 22)
        val textFont = Font("Dialog", Font.PLAIN, 24)

        val mainPanel = JPanel().also {
            it.maximumSize = Dimension(9999, 0)
        }
        val actionPanel = JPanel().also {
            it.maximumSize = Dimension(9999, 0)
        }

        hourInput.also {
            it.model = SpinnerNumberModel(0, 0, 23, 1)
            it.preferredSize = spinnerSize
            it.border = spinnerBorder
            it.font = spinnerFont
        }

        minuteInput.also {
            it.model = SpinnerNumberModel(20, 0, 59, 5)
            it.preferredSize = spinnerSize
            it.border = spinnerBorder
            it.font = spinnerFont
        }

        mainPanel.add(JLabel("Give me").also { it.font = textFont })
        mainPanel.add(hourInput)
        mainPanel.add(JLabel("hour(s) and").also { it.font = textFont })
        mainPanel.add(minuteInput)
        mainPanel.add(JLabel("minutes").also { it.font = textFont })

        JButton("Please").also {
            it.font = textFont
            it.addActionListener { startNewPeriod() }
            actionPanel.add(it)
        }

        add(Box.createVerticalGlue())
        add(mainPanel)
        add(Box.createVerticalStrut(10))
        add(actionPanel)
        add(Box.createVerticalGlue())
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