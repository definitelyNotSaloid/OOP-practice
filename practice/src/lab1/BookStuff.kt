package lab1

import java.lang.IllegalArgumentException


object Config {
    // technically, this is not a const. lets keep in mind it *might* be changed
    val separator: String
        get() = "//"

}

data class BookInfo(
    val name: String,
    val authors: List<String>,
    val year: Int,
    val id: Int
)

private fun withoutSideWhitespaces(string: String): String {
    val left = string.indexOfFirst { ch -> !ch.isWhitespace() }
    if (left == -1)                               // if string is blank
        return ""

    val right = string.indexOfLast { ch -> !ch.isWhitespace() }
    return string.substring(left, right + 1)
}

// format : $ID. $NAME // [AUTHOR1], [AUTHOR2],... // $YEAR
@Throws(IllegalArgumentException::class)
fun getBookFromString(string: String): BookInfo {
    val blocks = withoutSideWhitespaces(string).split(Config.separator)

    require(blocks.size == 3)           // magic const for sure, but making editable template for book info str is a bit of overkill
    { "bad formatting" }                    // naming it also seems pointless, it is not used anywhere else


    val idAndName = blocks[0]
    val idString = idAndName.substringBefore(' ', "")
    val id =
        (if (idString.last() == '.')
            idString.dropLast(1) else idString)
            .toIntOrNull() ?: throw IllegalArgumentException("Non-valid book id: $idString")

    val name = withoutSideWhitespaces(idAndName.substringAfter(' ', ""))
    require(name.isNotBlank()) { "missing book name" }

    val authors = ArrayList<String>()
    blocks[1].splitToSequence(',')
        .filter { str -> str.isNotBlank() }                         //avoiding adding empty strings
        .forEach { str -> authors.add(withoutSideWhitespaces(str)) }

    val year = withoutSideWhitespaces(blocks[2])
        .toIntOrNull() ?: throw IllegalArgumentException("invalid year : ${withoutSideWhitespaces(blocks[2])}")

    return BookInfo(
        name = name,
        authors = authors,
        year = year,
        id = id
    )
}


fun getBookListFromString(string: String): List<BookInfo> {
    val list = ArrayList<BookInfo>()
    for (line in string.splitToSequence('\n'))
        if (line.isNotBlank())                                          // exception on empty line is a bit of overkill
            list.add(getBookFromString(line))

    return list

}
