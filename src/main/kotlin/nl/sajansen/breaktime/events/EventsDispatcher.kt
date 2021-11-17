package nl.sajansen.breaktime.events

import org.slf4j.LoggerFactory
import java.awt.Component
import javax.swing.JFrame

object EventsDispatcher : GuiEventListener, DataEventListener {
    private val logger = LoggerFactory.getLogger(EventsDispatcher::class.java.name)

    var currentFrame: JFrame? = null

    private val guiEventListeners: HashSet<GuiEventListener> = HashSet()
    private val dataEventListeners: HashSet<DataEventListener> = HashSet()

    /*
        GuiEventListener events
     */
    override fun refreshNotifications() {
        guiEventListeners.toTypedArray().forEach {
            it.refreshNotifications()
        }
    }

    override fun onConfigSettingsSaved() {
        guiEventListeners.toTypedArray().forEach {
            it.onConfigSettingsSaved()
        }
    }

    override fun onComponentsListUpdated() {
        guiEventListeners.toTypedArray().forEach {
            it.onComponentsListUpdated()
        }
    }

    override fun windowClosing(window: Component?) {
        guiEventListeners.toTypedArray().forEach {
            it.windowClosing(window)
        }
    }

    /*
        DataEventListener
     */

    override fun onActionsUpdated() {
        dataEventListeners.toTypedArray().forEach {
            it.onActionsUpdated()
        }
    }

    override fun onRunningStateUpdated() {
        dataEventListeners.toTypedArray().forEach {
            it.onRunningStateUpdated()
        }
    }

    fun register(component: Any) {
        if (component is GuiEventListener) {
            logger.info("Registering GuiEventListener: ${component::class.java}")
            guiEventListeners.add(component)
        }
        if (component is DataEventListener) {
            logger.info("Registering SerialEventListener: ${component::class.java}")
            dataEventListeners.add(component)
        }
    }

    fun unregister(component: Any) {
        if (component is GuiEventListener) {
            logger.info("Unregistering GuiEventListener: ${component::class.java}")
            guiEventListeners.remove(component)
        }
        if (component is DataEventListener) {
            logger.info("Unregistering SerialEventListener: ${component::class.java}")
            dataEventListeners.remove(component)
        }
    }

    fun registeredComponents() = guiEventListeners
}