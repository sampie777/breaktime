package nl.sajansen.breaktime.events

import java.awt.Component

interface GuiEventListener {
    fun refreshNotifications() {}
    fun onConfigSettingsSaved() {}
    fun onStateUpdated() {}
    fun windowClosing(window: Component?) {}
}