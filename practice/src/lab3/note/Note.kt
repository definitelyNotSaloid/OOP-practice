package lab3.note

import lab3.misc.TextTask
import java.net.URL
import java.time.LocalDateTime

interface INote<T> {
    val title: String
    val created: LocalDateTime
    val content: T
}

class TaskNote(
    override val title: String,
    override val content: TextTask
) : INote<TextTask> {
    override val created: LocalDateTime = LocalDateTime.now()
}

class UrlNote(
    override val title: String,
    override val content: URL
) : INote<URL> {
    override val created: LocalDateTime = LocalDateTime.now()
}

class TextNote(
    override val title: String,
    override val content: String
) : INote<String> {
    override val created: LocalDateTime = LocalDateTime.now()
}

