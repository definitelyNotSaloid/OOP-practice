package lab1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.IllegalArgumentException

internal class BookStuffKtTest {

    @Test
    fun test_getBookFromString_Basics() {
        val expected = BookInfo(
            name="Норма",
            authors = listOf("Владимир Сорокин"),
            year = 1979,
            id = 1
        )

        assertEquals(expected,
            getBookFromString("1. Норма ${Config.separator} Владимир Сорокин ${Config.separator} 1979"))

        assertEquals(expected,
            getBookFromString("1 Норма ${Config.separator} Владимир Сорокин ${Config.separator} 1979"))             // missing point

        assertEquals(expected,
            getBookFromString("    1.   Норма    ${Config.separator}         Владимир Сорокин       ${Config.separator}         1979"))                 // m     a    n    y    spaces


        assertEquals(                                                                                               //>1 author
            BookInfo(
                name = "Метода по матану",
                authors = listOf("Колпаков А. С.", "Железняк А. В.", "Поздняков С. Н."),
                year = 2011,
                id = 1),

            getBookFromString("1. Метода по матану ${Config.separator} Колпаков А. С., Железняк А. В., Поздняков С. Н. ${Config.separator} 2011")
            )

        assertEquals(                                                                                               // no authors
            BookInfo(
                name = "Сборник убогих народных анекдотов",
                authors = listOf(),
                year = 2019,
                id = 1
            ),

            getBookFromString("1. Сборник убогих народных анекдотов ${Config.separator} ${Config.separator} 2019")
        )
    }

    @Test
    fun test_getBookFromString_Exceptions() {
        assertThrows(
            IllegalArgumentException::class.java
        ) {
            getBookFromString("euofeuou ueuivnui e eipupni p pepenn p eej. ${Config.separator} fww we.g ewr// 34 er/ er.ye")                                     // meaningless arg
        }

        assertThrows(
            IllegalArgumentException::class.java
        ) {
            getBookFromString("   ")                                                                                                            //just whitespaces
        }

        assertThrows(
            IllegalArgumentException::class.java
        ) {
            getBookFromString("1. Норма ${Config.separator} Владимир Сорокин ${Config.separator} 1979 ${Config.separator} Рейтинг: 7.5")          // extra data
        }

        assertThrows(
            IllegalArgumentException::class.java
        ) {
            getBookFromString("1. ${Config.separator} Владимир Сорокин ${Config.separator} 1979")            // missing name
        }


        assertThrows(
            IllegalArgumentException::class.java
        ) {
            getBookFromString("1. Норма ${Config.separator} Владимир Сорокин 1979")               // bad separator placement
        }

        assertThrows(
            IllegalArgumentException::class.java
        ) {
            getBookFromString("1. ${Config.separator} Владимир Сорокин ${Config.separator}")               // missing year
        }
    }

    @Test
    fun test_getBookListFromString() {
        val expected1 = BookInfo(
            name="Норма",
            authors = listOf("Владимир Сорокин"),
            year = 1979,
            id = 1
        )
        val expected2 = BookInfo(
            name="Сборник убогих народных анекдотов",
            authors = listOf(),
            year = 2019,
            id = 2
        )
        val expected3 = BookInfo(
            name="Метода по матану",
            authors = listOf("Колпаков А. С.", "Железняк А. В.", "Поздняков С. Н."),
            year = 2011,
            id = 3
        )

        // used different asserts here on purpose
        // each string has different pattern, each can work in its own way

        assertTrue(
            getBookListFromString(
                """1. Норма ${Config.separator} Владимир Сорокин ${Config.separator} 1979
                    |2. Сборник убогих народных анекдотов ${Config.separator} ${Config.separator} 2019
                    |3. Метода по матану ${Config.separator} Колпаков А. С., Железняк А. В., Поздняков С. Н. ${Config.separator} 2011
                """.trimMargin()
            ).any{
                elem -> elem==expected1
            }
        )

        assertTrue(
            getBookListFromString(
                """1. Норма ${Config.separator} Владимир Сорокин ${Config.separator} 1979
                    |2. Сборник убогих народных анекдотов ${Config.separator} ${Config.separator} 2019
                    |3. Метода по матану ${Config.separator} Колпаков А. С., Железняк А. В., Поздняков С. Н. ${Config.separator} 2011
                """.trimMargin()
            ).any{
                    elem -> elem==expected2
            }
        )

        assertTrue(
            getBookListFromString(
                """1. Норма ${Config.separator} Владимир Сорокин ${Config.separator} 1979
                    |2. Сборник убогих народных анекдотов ${Config.separator} ${Config.separator} 2019
                    |3. Метода по матану ${Config.separator} Колпаков А. С., Железняк А. В., Поздняков С. Н. ${Config.separator} 2011
                """.trimMargin()
            ).any{
                    elem -> elem==expected3
            }
        )
    }
}