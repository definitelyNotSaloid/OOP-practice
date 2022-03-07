import lab1.*

fun main(){
    val dataString =
        """1. Норма // Владимир Сорокин // 1979
        |2. Сборник убогих народных анекдотов // // 2019
        |3. Одиссея // Гомер // -770
        |4. Книга о вкусной и здоровой пище // // 1939
        |5. Kotlin в действии // Жемеров Дмитрий, Исакова Светлана // 2016
        |6. Прикладная психокинетика // HAL9000 // 2563
        |7. Петербург // Андрей Белый // 1922
    """.trimMargin()

    val bookList = getBookListFromString(dataString)

    // not going to check for empty list here. this is only a demonstration
    var newestBook = bookList[0]
    var oldestBook = bookList[0]
    var longestNameBook = bookList[0]
    var shortestNameBook = bookList[0]

    for (book in bookList)
    {
        if (book.name.length>longestNameBook.name.length)
            longestNameBook=book

        if (book.name.length<shortestNameBook.name.length)
            shortestNameBook=book

        if (book.year>newestBook.year)
            newestBook = book

        if (book.year<oldestBook.year)
            oldestBook = book
    }

    println("Новейшая книга: ${newestBook.name}")
    println("Старейшая книга: ${oldestBook.name}")
    println("Самое короткое название: ${shortestNameBook.name}")
    println("Самое длинное название: ${longestNameBook.name}")
}