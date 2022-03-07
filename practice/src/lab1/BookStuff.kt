package lab1

import java.lang.IllegalArgumentException
import java.lang.StringBuilder


object Config {
    // technically, this is not a const. lets keep in mind it *might* be changed
    val separator : String
        get() = "//"

}

data class BookInfo(
    val name: String,
    val authors: List<String>,
    val year: Int,
    val id: Int
)

// format : $ID. $NAME // [AUTHOR1], [AUTHOR2],... // $YEAR
@Throws(IllegalArgumentException::class)
fun getBookFromString(string: String): BookInfo {
    // TODO better algorithm:
    // split by separator
    // split authors by ,
    // get rid of damn whitespaces
    // rest is ez
    // but for now, it just works


    val words = string.split(' ')
        .filter { str -> str.isNotBlank() }         // not as effective as it should be, double array creation
    // for reason beyond me split returns empty sequences for 2+ separators in row

    require(words.isNotEmpty()) { "string is empty or whitespaces only" }

    val id =
        (if (words[0].last() == '.')
            words[0].dropLast(1) else words[0]).toIntOrNull() ?: throw IllegalArgumentException("Non-valid book id")


    var index = 1                                       // +1 for id word
    val nameBuilder = StringBuilder()                   // mb count name size and reserve size needed?
    while (index < words.size) {
        if (words[index] == Config.separator) {
            index++
            break
        }
        nameBuilder.append(words[index])
        nameBuilder.append(" ")

        index++
    }


    require(nameBuilder.isNotBlank()) { "No book name specified" }

    nameBuilder.deleteCharAt(nameBuilder.length - 1)      // removing ending whitespace
    val name = nameBuilder.toString()

    val authors = ArrayList<String>()
    val authorBuilder = StringBuilder()
    // a single author is split into 2-3 words, have to stick them together again
    while (index < words.size) {
        if (words[index] == Config.separator) {
            if (authorBuilder.isNotBlank()) {
                authorBuilder.deleteCharAt(authorBuilder.length - 1)                  // there is a whitespace left after adding a word
                authors.add(authorBuilder.toString())
            }

            index++
            break
        }


        authorBuilder.append(words[index])

        if (authorBuilder.last() == ',') {        //if end of author's name
            authorBuilder.deleteCharAt(authorBuilder.length - 1)
            authors.add(authorBuilder.toString())
            authorBuilder.clear()
        } else
            authorBuilder.append(" ")

        index++
    }
    require(index + 1 == words.size) { "bad formatting" }

    val year = words[index].toIntOrNull() ?: throw IllegalArgumentException("invalid year: ${words[index]}")


    return BookInfo(
        name = name,
        authors = authors,
        year = year,
        id = id
    )


    //error("not implemented")
}

fun getBookListFromString(string: String): List<BookInfo> {
    val list = ArrayList<BookInfo>()
    for (line in string.splitToSequence('\n'))
        if (line.isNotBlank())                                          // exception on empty line is a bit of overkill
            list.add(getBookFromString(line))

    return list

}
