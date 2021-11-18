package nl.sajansen.breaktime.gui.screens.hackerScreen

import nl.sajansen.breaktime.buttonBackgroundColor
import nl.sajansen.breaktime.defaultFont
import nl.sajansen.breaktime.textColor
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JPanel

class HackerScreen(private val onClose: () -> Unit) : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    init {
        createGui()
    }

    private fun createGui() {
        layout = BorderLayout()
        background = null
        preferredSize = Dimension(999, 400)
        maximumSize = Dimension(700, 999)

        val topPanel = JPanel().also {
            it.background = Color(70, 70, 70)
            it.layout = BorderLayout()
            add(it, BorderLayout.PAGE_START)
        }
        val mainPanel = JPanel().also {
            it.background = Color(60, 60, 60)
            it.layout = BorderLayout()
            add(it, BorderLayout.CENTER)
        }

        JButton("X").also {
            it.font = defaultFont.deriveFont(24f)
            it.foreground = textColor
            it.background = buttonBackgroundColor
            it.addActionListener { onClose() }
            it.isFocusable = false
            topPanel.add(it, BorderLayout.LINE_END)
        }

        mainPanel.add(Terminal { onClose() }, BorderLayout.CENTER)
    }
}