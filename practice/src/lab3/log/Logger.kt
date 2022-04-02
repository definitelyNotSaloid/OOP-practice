package lab3.log

import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Logger {
    var outputPath: String = "Logs/log.txt"
    private var format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun message(text: String) {
        val file = FileWriter(outputPath, true)
        file.write("INFO [${LocalDateTime.now().format(format)}]: $text\n")
        file.close()
    }
}
