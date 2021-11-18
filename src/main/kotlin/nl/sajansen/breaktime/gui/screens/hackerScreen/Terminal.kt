package nl.sajansen.breaktime.gui.screens.hackerScreen

import nl.sajansen.breaktime.control.TerminalControl
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

class Terminal(private val onClose: () -> Unit) : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val textArea = JTextArea()
    private val scrollPane = JScrollPane(textArea)
    private val terminal = TerminalControl(onClose)

    init {
        createGui()
        updateTerminalText()

        textArea.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent) {
            }

            override fun keyPressed(e: KeyEvent) {
                onKeyPressed(e)
            }

            override fun keyReleased(e: KeyEvent) {
            }
        })
    }

    private fun createGui() {
        layout = BorderLayout()
        background = null

        textArea.background = Color(0, 50, 0)
        textArea.foreground = Color(0, 200, 0)
        textArea.isEditable = false
        textArea.font = Font("Courier", Font.PLAIN, 15)
        textArea.lineWrap = true
        textArea.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        scrollPane.border = null
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_NEVER

        add(scrollPane, BorderLayout.CENTER)
    }

    private fun updateTerminalText() {
        textArea.text = terminal.terminalText + terminal.command + "_"
        scrollPane.verticalScrollBar.also {
            it.value = it.maximum
        }
    }

    private fun onKeyPressed(e: KeyEvent) {
        terminal.onKeyPressed(e)
        updateTerminalText()
    }
}