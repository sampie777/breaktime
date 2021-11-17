package nl.sajansen.breaktime.gui.mainFrame

import nl.sajansen.breaktime.ApplicationInfo
import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.Screen
import nl.sajansen.breaktime.events.EventsDispatcher
import nl.sajansen.breaktime.events.GuiEventListener
import nl.sajansen.breaktime.utils.gui.loadImageResource
import nl.sajansen.breaktime.utils.gui.setFullscreen
import org.slf4j.LoggerFactory
import java.util.*
import javax.swing.JFrame
import javax.swing.JOptionPane


class MainFrame : JFrame(), GuiEventListener {
    private val logger = LoggerFactory.getLogger(MainFrame::class.java.name)

    private var autoFocusTimer: Timer? = null

    companion object {
        private var instance: MainFrame? = null
        fun getInstance() = instance

        fun create(): MainFrame = MainFrame()

        fun createAndShow(): MainFrame {
            val frame = create()
            frame.isVisible = true
            return frame
        }
    }

    init {
        instance = this

        EventsDispatcher.register(this)
        addWindowListener(MainFrameWindowAdapter(this))

        initGUI()
        onStateUpdated()
    }

    private fun initGUI() {
        setSize(695, 450)
        setLocationRelativeTo(null)

        add(MainFramePanel())

        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = loadImageResource("images/icon-512.png")
        title = ApplicationInfo.name
    }

    override fun onStateUpdated() {
        if (ControlUtils.determineCurrentScreen() == Screen.WorkTime) {
            lockScreen(false)
        } else {
            lockScreen(true)
        }
    }

    private fun lockScreen(value: Boolean) {
        isAlwaysOnTop = value
        defaultCloseOperation = if (value) DO_NOTHING_ON_CLOSE else EXIT_ON_CLOSE
        extendedState = if (value) NORMAL else ICONIFIED

        autoFocusTimer?.cancel()
        autoFocusTimer?.purge()
        if (value) {
            autoFocusTimer = ControlUtils.setAutoFocusser(this)
        }

        if (!setFullscreen(value)) {
            JOptionPane.showMessageDialog(
                this,
                "Fullscreen not supported on this graphics device",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }
}