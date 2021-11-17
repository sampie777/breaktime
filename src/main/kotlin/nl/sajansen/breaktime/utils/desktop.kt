package nl.sajansen.breaktime.utils

import nl.sajansen.breaktime.utils.notifications.Notifications
import org.slf4j.LoggerFactory
import java.awt.Desktop
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.net.URI

private val logger = LoggerFactory.getLogger("utils.desktop")

fun copyString(string: String) {
    logger.info("Copying text to clipboard")
    val stringSelection = StringSelection(string)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(stringSelection, stringSelection)
}

fun pasteText(string: String, sendPasteDelayMs: Int = 100) {
    logger.info("Pasting text from clipboard")
    val robot = Robot()
    robot.delay(sendPasteDelayMs)
    robot.keyPress(KeyEvent.VK_CONTROL)
    robot.keyPress(KeyEvent.VK_V)
    robot.keyRelease(KeyEvent.VK_V)
    robot.keyRelease(KeyEvent.VK_CONTROL)
    robot.delay(100)
    robot.keyPress(KeyEvent.VK_ENTER)
    robot.keyRelease(KeyEvent.VK_ENTER)
}

fun openWebURL(url: String, subject: String = "Webbrowser"): Boolean {
    if (!Desktop.isDesktopSupported()) {
        logger.warn("Cannot open link '$url': not supported by host")
        return false
    }
    try {
        Desktop.getDesktop().browse(URI(url))
        return true
    } catch (t: Throwable) {
        logger.error("Error during opening link '$url'")
        t.printStackTrace()
        Notifications.popup("Failed to open link: $url", subject)
    }
    return false
}

fun screenshot(delay: Int = 0): BufferedImage {
    val robot = Robot()
    robot.delay(delay)
    val screenSize = Rectangle(Toolkit.getDefaultToolkit().screenSize)
    return robot.createScreenCapture(screenSize)
}

fun clickMouse(x: Int, y: Int) {
    val robot = Robot()
    robot.mouseMove(x, y)
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
}