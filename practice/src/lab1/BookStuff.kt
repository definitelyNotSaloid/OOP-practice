package lab1

import java.lang.IllegalArgumentException


object Config {
    // technically, this is not a const. lets keep in mind it *might* be changed
    val separator: String
        get() = "//"

}

data class BookInfo(
    val title: String,
    val authors: List<String>,
    val year: Int,
    val id: Int
)

// format : $ID. $TITLE // [AUTHOR1], [AUTHOR2],... // $YEAR
@Throws(IllegalArgumentException::class)
fun getBookFromString(string: String): BookInfo {
    val blocks = string.trim().split(Config.separator)

    require(blocks.size == 3)           // magic const for sure, but making editable template for book info str is a bit of overkill
    { "bad formatting" }                      // naming it also seems pointless, it is not used anywhere else


    val idAndName = blocks[0]
    val idString = idAndName.substringBefore(' ', "")
    val id =
        (if (idString.last() == '.')
            idString.dropLast(1) else idString)
            .toIntOrNull() ?: throw IllegalArgumentException("Non-valid book id: $idString")

    val title = idAndName.substringAfter(' ', "").trim()
    require(title.isNotBlank()) { "missing book title" }

    val authors = ArrayList<String>()
    blocks[1].splitToSequence(',')
        .forEach { str ->
            val strTrimmed = str.trim()                         // we don't want to add blank strings
            if (strTrimmed.isNotEmpty())
                authors.add(strTrimmed)
        }

    val year = blocks[2].trim()
        .toIntOrNull() ?: throw IllegalArgumentException("invalid year : ${blocks[2].trim()}")

    return BookInfo(
        title = title,
        authors = authors,
        year = year,
        id = id
    )
}


fun getBookListFromString(string: String): List<BookInfo> {
    val list = mutableListOf<BookInfo>()
    for (line in string.splitToSequence('\n'))
        if (line.isNotBlank())
            list.add(getBookFromString(line))

    return list

}
