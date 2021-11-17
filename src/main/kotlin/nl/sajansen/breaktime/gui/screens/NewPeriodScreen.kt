package nl.sajansen.breaktime.gui.screens

import nl.sajansen.breaktime.buttonBackgroundColor
import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.MainControl
import nl.sajansen.breaktime.defaultFont
import nl.sajansen.breaktime.textColor
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Dimension
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
        background = null

        val spinnerSize = Dimension(60, 35)
        val spinnerBorder = BorderFactory.createLineBorder(Color(168, 168, 168))
        val spinnerFont = defaultFont.deriveFont(28f)
        val textFont = defaultFont.deriveFont(32f)

        val mainPanel = JPanel().also {
            it.maximumSize = Dimension(9999, 0)
            it.background = null
        }
        val actionPanel = JPanel().also {
            it.maximumSize = Dimension(9999, 0)
            it.background = null
        }

        hourInput.also {
            it.model = SpinnerNumberModel(ControlUtils.getLastWorkTimeHours(), 0, 23, 1)
            it.preferredSize = spinnerSize
            it.border = spinnerBorder
            it.font = spinnerFont
        }

        minuteInput.also {
            it.model = SpinnerNumberModel(ControlUtils.getLastWorkTimeMinutes(), 0, 59, 5)
            it.preferredSize = spinnerSize
            it.border = spinnerBorder
            it.font = spinnerFont
        }

        mainPanel.add(JLabel("Give me").also {
            it.font = textFont
            it.foreground = textColor
        })
        mainPanel.add(Box.createHorizontalStrut(5))
        mainPanel.add(hourInput)
        mainPanel.add(Box.createHorizontalStrut(5))
        mainPanel.add(JLabel("hour(s) and").also {
            it.font = textFont
            it.foreground = textColor
        })
        mainPanel.add(Box.createHorizontalStrut(5))
        mainPanel.add(minuteInput)
        mainPanel.add(Box.createHorizontalStrut(5))
        mainPanel.add(JLabel("minutes...").also {
            it.font = textFont
            it.foreground = textColor
        })

        JButton("please").also {
            it.font = textFont
            it.preferredSize = Dimension(200, 50)
            it.foreground = textColor
            it.background = buttonBackgroundColor
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