package nl.sajansen.breaktime.gui.mainFrame

import nl.sajansen.breaktime.events.EventsDispatcher
import nl.sajansen.breaktime.events.GuiEventListener
import org.slf4j.LoggerFactory
import javax.swing.BoxLayout
import javax.swing.JPanel


class MainFramePanel : JPanel(), GuiEventListener {
    private val logger = LoggerFactory.getLogger(MainFramePanel::class.java.name)

    init {
        EventsDispatcher.register(this)

        createGui()

        refreshNotifications()
    }

    private fun createGui() {
        border = null
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)

//        add(ClockPanel())
//        add(ActionsPanel())
    }

    override fun removeNotify() {
        super.removeNotify()
        EventsDispatcher.unregister(this)
    }

}