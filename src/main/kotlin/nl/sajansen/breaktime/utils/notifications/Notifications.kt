package nl.sajansen.breaktime.utils.notifications

import nl.sajansen.breaktime.events.EventsDispatcher
import java.util.logging.Logger
import javax.swing.JOptionPane

object Notifications {
    private val logger = Logger.getLogger(Notifications.toString())

    val list: ArrayList<Notification> = ArrayList()
    var unreadNotifications: Int = 0

    fun add(notification: Notification, markAsRead: Boolean = false) {
        logger.info("Adding new notification: $notification")
        list.add(notification)

        if (!markAsRead) {
            unreadNotifications++
        }

        EventsDispatcher.refreshNotifications()
    }

    fun add(message: String, subject: String = "", markAsRead: Boolean = false) {
        add(Notification(message, subject), markAsRead)
    }

    fun markAllAsRead() {
        logger.info("Marking all notifications as read")
        unreadNotifications = 0
        EventsDispatcher.refreshNotifications()
    }

    fun clear() {
        list.clear()
        markAllAsRead()
    }

    fun popup(notification: Notification, markAsRead: Boolean = true) {
        add(notification, markAsRead)

        JOptionPane.showMessageDialog(
            EventsDispatcher.currentFrame,
            notification.message,
            notification.subject,
            JOptionPane.PLAIN_MESSAGE
        )
    }

    fun popup(message: String, subject: String = "", markAsRead: Boolean = true) {
        popup(Notification(message, subject), markAsRead)
    }
}