package nl.sajansen.breaktime.gui.mainFrame

import nl.sajansen.breaktime.ApplicationInfo
import nl.sajansen.breaktime.Settings
import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.Screen
import nl.sajansen.breaktime.events.EventsDispatcher
import nl.sajansen.breaktime.events.GuiEventListener
import nl.sajansen.breaktime.utils.gui.loadImageResource
import nl.sajansen.breaktime.utils.gui.setFullscreen
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.util.*
import javax.swing.JFrame
import javax.swing.JOptionPane


class MainFrame : JFrame(), GuiEventListener {
    private val logger = LoggerFactory.getLogger(MainFrame::class.java.name)

    private var autoFocusTimer: Timer? = null
    var isLocked: Boolean = true

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

        add(MainFramePanel(), BorderLayout.CENTER)

        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = loadImageResource("images/icon-512.png")
        title = ApplicationInfo.name
        isUndecorated = true
    }

    override fun onStateUpdated() {
        isLocked = ControlUtils.determineCurrentScreen() != Screen.WorkTime
        handleLockScreen()
    }

    private fun handleLockScreen() {
        if (!setFullscreen(isLocked)) {
            JOptionPane.showMessageDialog(
                this,
                "Fullscreen not supported on this graphics device",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
        }

        isAlwaysOnTop = isLocked
        defaultCloseOperation = if (isLocked) DO_NOTHING_ON_CLOSE else EXIT_ON_CLOSE
        minimize(!isLocked && Settings.minimizeWorkTimeScreen)

        autoFocusTimer?.cancel()
        autoFocusTimer?.purge()
        if (isLocked) {
            autoFocusTimer = ControlUtils.setAutoFocusser(this)
        } else {
            setSize(695, 450)
            setLocationRelativeTo(null)
        }
    }

    fun minimize(setMinimized: Boolean) {
        extendedState = if (setMinimized) ICONIFIED else NORMAL
    }
}