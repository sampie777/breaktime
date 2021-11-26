package nl.sajansen.breaktime.gui

import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.Screen
import nl.sajansen.breaktime.events.EventsDispatcher
import nl.sajansen.breaktime.events.GuiEventListener
import nl.sajansen.breaktime.gui.mainFrame.MainFrame
import nl.sajansen.breaktime.textColor
import nl.sajansen.breaktime.utils.gui.getMainFrameComponent
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JPanel

class TopBar(private val openPopup: () -> Unit) : JPanel(), GuiEventListener {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val minimizeButton = JButton("_")

    init {
        EventsDispatcher.register(this)

        createGui()

        onStateUpdated()
    }

    private fun createGui() {
        layout = BorderLayout()
        maximumSize = Dimension(9999, 0)
        background = null

        JPanel().also {
            it.background = null
            it.preferredSize = Dimension(100, 0)
            add(it, BorderLayout.LINE_START)
        }

        JPanel().also {
            it.background = null
            it.add(Clock())
            add(it, BorderLayout.CENTER)
        }

        JPanel().also {
            it.layout = FlowLayout(FlowLayout.RIGHT)
            it.background = null
            it.preferredSize = Dimension(100, 0)
            val font = Font("Courier", Font.PLAIN, 16)
            val border = BorderFactory.createEmptyBorder(5, 10, 5, 10)

            minimizeButton.also { button ->
                button.toolTipText = "Minimize window"
                button.foreground = textColor
                button.background = null
                button.font = font
                button.border = border
                button.isFocusable = false
                button.addActionListener { minimize() }
                it.add(button)
            }

            JButton("#").also { button ->
                button.toolTipText = "Hacker screen"
                button.foreground = textColor
                button.background = null
                button.font = font
                button.border = border
                button.isFocusable = false
                button.addActionListener { openPopup() }
                it.add(button)
            }

            add(it, BorderLayout.LINE_END)
        }

    }

    private fun minimize() {
        (getMainFrameComponent() as MainFrame?)?.minimize(true)
    }

    override fun onStateUpdated() {
        minimizeButton.isVisible = ControlUtils.determineCurrentScreen() == Screen.WorkTime
    }
}