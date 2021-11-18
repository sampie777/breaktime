package nl.sajansen.breaktime.gui.mainFrame

import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.Screen
import nl.sajansen.breaktime.events.EventsDispatcher
import nl.sajansen.breaktime.events.GuiEventListener
import nl.sajansen.breaktime.textColor
import nl.sajansen.breaktime.utils.gui.getMainFrameComponent
import org.slf4j.LoggerFactory
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
        layout = FlowLayout(FlowLayout.RIGHT)
        maximumSize = Dimension(9999, 0)
        background = null
        val font = Font("Courier", Font.PLAIN, 16)
        val border = BorderFactory.createEmptyBorder(5, 10, 5, 10)

        minimizeButton.also {
            it.toolTipText = "Minimize window"
            it.foreground = textColor
            it.background = null
            it.font = font
            it.border = border
            it.isFocusable = false
            it.addActionListener { minimize() }
            add(it)
        }

        JButton("#").also {
            it.toolTipText = "Hacker screen"
            it.foreground = textColor
            it.background = null
            it.font = font
            it.border = border
            it.isFocusable = false
            it.addActionListener { openPopup() }
            add(it)
        }
    }

    private fun minimize() {
        (getMainFrameComponent() as MainFrame?)?.minimize(true)
    }

    override fun onStateUpdated() {
        minimizeButton.isVisible = ControlUtils.determineCurrentScreen() == Screen.WorkTime
    }
}