package nl.sajansen.breaktime.gui.mainFrame

import nl.sajansen.breaktime.ApplicationInfo
import nl.sajansen.breaktime.utils.gui.loadImageResource
import org.slf4j.LoggerFactory
import javax.swing.JFrame


class MainFrame : JFrame() {
    private val logger = LoggerFactory.getLogger(MainFrame::class.java.name)

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

        addWindowListener(MainFrameWindowAdapter(this))

        initGUI()
    }

    private fun initGUI() {
        setSize(695, 450)
        setLocationRelativeTo(null)

        add(MainFramePanel())

        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = loadImageResource("images/icon-512.png")
        title = ApplicationInfo.name
    }

    fun saveWindowPosition() {
    }
}