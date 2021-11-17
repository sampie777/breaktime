package nl.sajansen.breaktime.gui.mainFrame

import nl.sajansen.breaktime.control.ControlUtils
import nl.sajansen.breaktime.control.Screen
import nl.sajansen.breaktime.events.EventsDispatcher
import nl.sajansen.breaktime.events.GuiEventListener
import nl.sajansen.breaktime.gui.screens.DuringWorkTimeScreen
import nl.sajansen.breaktime.gui.screens.NewPeriodScreen
import nl.sajansen.breaktime.gui.screens.WaitDuringBreakScreen
import org.slf4j.LoggerFactory
import javax.swing.BoxLayout
import javax.swing.JPanel


class MainFramePanel : JPanel(), GuiEventListener {
    private val logger = LoggerFactory.getLogger(MainFramePanel::class.java.name)

    init {
        EventsDispatcher.register(this)

        refreshGui()
    }

    private fun refreshGui() {
        border = null
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        removeAll()

        when (ControlUtils.determineCurrentScreen()) {
            Screen.BreakTime -> add(WaitDuringBreakScreen())
            Screen.WorkTime -> add(DuringWorkTimeScreen())
            else -> add(NewPeriodScreen())
        }
        revalidate()
    }

    override fun removeNotify() {
        super.removeNotify()
        EventsDispatcher.unregister(this)
    }

    override fun onStateUpdated() {
        refreshGui()
    }
}