package nl.sajansen.breaktime.gui.mainFrame

import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.Screen
import nl.sajansen.breaktime.events.EventsDispatcher
import nl.sajansen.breaktime.events.GuiEventListener
import nl.sajansen.breaktime.gui.screens.DuringWorkTimeScreen
import nl.sajansen.breaktime.gui.screens.NewPeriodScreen
import nl.sajansen.breaktime.gui.screens.WaitDuringBreakScreen
import nl.sajansen.breaktime.gui.screens.hackerScreen.HackerScreen
import org.slf4j.LoggerFactory
import java.awt.Color
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JPanel


class MainFramePanel : JPanel(), GuiEventListener {
    private val logger = LoggerFactory.getLogger(MainFramePanel::class.java.name)

    private val contentPanel = JPanel()
    private var popup: JPanel? = null

    init {
        EventsDispatcher.register(this)

        initGui()
        refreshGui()
    }

    private fun initGui() {
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)

        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        background = Color(50, 50, 50)

        add(TopBar { openPopup() })
        add(Box.createVerticalGlue())
        add(contentPanel)
        add(Box.createVerticalGlue())
    }

    private fun refreshGui() {
        contentPanel.removeAll()
        contentPanel.add(Box.createVerticalGlue())

        if (popup != null) {
            contentPanel.background = null
            contentPanel.add(popup)
        } else {
            contentPanel.background = Color(60, 60, 60)
            when (ControlUtils.determineCurrentScreen()) {
                Screen.BreakTime -> contentPanel.add(WaitDuringBreakScreen())
                Screen.WorkTime -> contentPanel.add(DuringWorkTimeScreen())
                Screen.NewPeriod -> contentPanel.add(NewPeriodScreen())
            }
        }

        contentPanel.add(Box.createVerticalGlue())
        contentPanel.revalidate()
    }

    override fun removeNotify() {
        super.removeNotify()
        EventsDispatcher.unregister(this)
    }

    override fun onStateUpdated() {
        refreshGui()
    }

    private fun openPopup() {
        popup = HackerScreen { closePopup() }
        refreshGui()
    }

    private fun closePopup() {
        popup = null
        refreshGui()
    }
}