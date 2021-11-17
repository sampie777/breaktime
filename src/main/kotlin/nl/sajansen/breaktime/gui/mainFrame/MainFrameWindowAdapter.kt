package nl.sajansen.breaktime.gui.mainFrame

import nl.sajansen.breaktime.events.EventsDispatcher
import nl.sajansen.breaktime.exitApplication
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent


class MainFrameWindowAdapter(private val frame: MainFrame) : WindowAdapter() {
    override fun windowClosing(winEvt: WindowEvent) {
        exitApplication()
    }

    override fun windowActivated(e: WindowEvent?) {
        super.windowActivated(e)
        EventsDispatcher.currentFrame = frame
    }
}