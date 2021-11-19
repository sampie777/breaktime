package nl.sajansen.breaktime.control.penalties

import nl.sajansen.breaktime.Settings
import nl.sajansen.breaktime.utils.format
import org.slf4j.LoggerFactory
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

class EmailPenalty : PenaltyInterface {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    var blocking = false

    override fun execute(): Boolean {
        val iftttUrl = URL("https://maker.ifttt.com/trigger/${Settings.iftttWebHookEmailPenaltyEvent}/with/key/${Settings.iftttWebHookKey}")

        if (!validate()) {
            return false
        }

        Thread {
            logger.info("Sending data to $iftttUrl")
            (iftttUrl.openConnection() as HttpURLConnection).also {
                it.requestMethod = "POST"
                it.doOutput = true
                it.setRequestProperty("Accept", "application/json")
                it.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

                val bytes = getData().toByteArray(StandardCharsets.UTF_8)
                it.outputStream.write(bytes, 0, bytes.size)

                logger.info("Response: [${it.responseCode}] ${it.responseMessage}")
            }
        }.also {
            it.start()
            if (blocking) {
                it.join()
            }
        }
        return true
    }

    fun getData(): String = """
                {
                  "value1": "${Settings.penaltyEmailUserName}",
                  "value2": "${Date().format("H:mm")}",
                  "value3": "${Settings.penaltyEmailToEmail}"
                }
            """.trimIndent()

    fun validate(): Boolean {
        if (Settings.iftttWebHookEmailPenaltyEvent.isBlank()) {
            logger.error("Cannot send penalty email: IFTTT not set up, missing iftttWebHookEmailPenaltyEvent")
            return false
        }
        if (Settings.iftttWebHookKey.isBlank()) {
            logger.error("Cannot send penalty email: IFTTT not set up, missing iftttWebHookKey")
            return false
        }
        if (Settings.penaltyEmailToEmail.isBlank()) {
            logger.error("Cannot send penalty email: no receiver email is set")
            return false
        }
        return true
    }
}